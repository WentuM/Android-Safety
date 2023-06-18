package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class StorageInternalMemory(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private val ruleMessage =
        "It is necessary to store all sensitive data in the internal memory of the device, isolated for each application. Other applications cannot access it. When the user deletes the application, all data stored in the internal memory is deleted. " +
                "You must use cryptographic keys to encrypt sensitive data. Writing to the internal memory can be done as follows in such a case: “encryptedFile = EncryptedFile.Builder(File(directory, fileToWriteName), applicationContext, mainKeyAlias, EncryptedFile.FileEncryptedScheme.AES256_GCM_HKDF_4KB).build()”"

    private val keyAlias = "val keyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)\n"
    private val fixWriteBytes = "write(toByteArray(StandardCharsets.UTF_8))"
    private val encryptedFile = "val encryptedFile = EncryptedFile.Builder(file, applicationContext, keyAlias,\n EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB).build()\n"

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {
            val pattern = "Environment.getExternalStoragePublicDirectory"
            val confidentTypeList = listOf<String>("password", "token", "creditcard", "phoneNumber", "email")
            val pattern2 = "writeBytes"

            var index = 0
            var currentPsiFileTextLength = psiFile.viewProvider.document.textLength
            var lastStartOffset = 0

            val currentRuleModelList = mutableListOf<RuleModel>()

            val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern)

            foundIndexes.forEach {patternIndex ->
                confidentTypeList.forEach { type ->
                    val confidentTypeFoundIndexes = MainKt().performKMPSearch(psiFile.text, type)
                    val pattern2FoundIndexes = MainKt().performKMPSearch(psiFile.text, pattern2)
                    confidentTypeFoundIndexes.forEach {
                        val consoleMessage = "\n" +
                                psiFile.originalFile.virtualFile.path + ":" + (psiFile.viewProvider.document.getLineNumber(
                            patternIndex
                        ) + 1) + " $ruleMessage"
                        //annotator

                        if (isNeedFix) {
                            if (patternIndex > lastStartOffset) {
                                psiFile.viewProvider.document?.replaceString(
                                    patternIndex + index,
                                    patternIndex + keyAlias.length + index,
                                    keyAlias
                                )
                                psiFile.viewProvider.document?.replaceString(
                                    patternIndex + index,
                                    patternIndex + fixWriteBytes.length + index,
                                    fixWriteBytes
                                )
                                psiFile.viewProvider.document?.replaceString(
                                    patternIndex + index,
                                    patternIndex + encryptedFile.length + index,
                                    encryptedFile
                                )
                                index += (psiFile.viewProvider.document.textLength - currentPsiFileTextLength)
                            } else {
                                psiFile.viewProvider.document?.replaceString(
                                    patternIndex,
                                    patternIndex + keyAlias.length,
                                    keyAlias
                                )
                                psiFile.viewProvider.document?.replaceString(
                                    patternIndex,
                                    patternIndex + fixWriteBytes.length,
                                    fixWriteBytes
                                )
                                psiFile.viewProvider.document?.replaceString(
                                    patternIndex,
                                    patternIndex + encryptedFile.length,
                                    encryptedFile
                                )
                            }
                            lastStartOffset = patternIndex
                            currentPsiFileTextLength = psiFile.viewProvider.document.textLength
                        } else {
                            currentRuleModelList.add(
                                RuleModel(
                                    it,
                                    it + pattern.length,
                                    ruleMessage,
                                    consoleMessage,
                                    keyAlias
                                )
                            )
                            currentRuleModelList.add(
                                RuleModel(
                                    it,
                                    it + pattern.length,
                                    ruleMessage,
                                    consoleMessage,
                                    fixWriteBytes
                                )
                            )
                            currentRuleModelList.add(
                                RuleModel(
                                    it,
                                    it + pattern.length,
                                    ruleMessage,
                                    consoleMessage,
                                    encryptedFile
                                )
                            )
                        }

                        printConsoleView(consoleMessage)
                    }
                }
            }

            if (!isNeedFix) {
                if (currentRuleModelList.isNotEmpty()) {
                    val findRuleModelList = AnnotatorRepository.getAnnotatorRuleModelsByFileName(psiFile.name)
                    if (findRuleModelList.isEmpty()) {
                        annotatorRuleModel.ruleList.addAll(currentRuleModelList)
                        AnnotatorRepository.annotatorRuleModelList.add(annotatorRuleModel)
                    } else {
                        AnnotatorRepository.updateAnnotatorRuleModelsByFileName(psiFile.name, currentRuleModelList)
                    }
                }
            }
        }
    }

    private fun printConsoleView(consoleMessage: String) {
        consoleView.print(consoleMessage, ConsoleViewContentType.NORMAL_OUTPUT)
    }
}