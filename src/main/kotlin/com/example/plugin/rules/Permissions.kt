package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class Permissions(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private val ruleMessage =
        "Permissions for actions that can be performed in another application - Do not use permissions for actions that can be performed in another application that already has permissions to do so. So, you need to use redirect intents with the appropriate requests. \" +\n" +
                "\"For example, instead of using the READ_CONTACTS, WRITE_CONTACTS permissions in your application, you need to redirect the user in the Contacts application, which already has permissions to do so. Intent(Intent.ACTION_INSERT).apply { type = ContactsContract.Contacts.CONTENT_TYPE}.also {intent -> intent.resolveActivity(packageManager)?.startActivity(intent) }"

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {
            val pattern = "intent.resolveActivity(packageManager)?.startActivity(intent)"
            val secondPattern = "WRITE_CONTACTS"
            val thirdPattern = "READ_CONTACTS"

            var index = 0
            var currentPsiFileTextLength = psiFile.viewProvider.document.textLength
            var lastStartOffset = 0

            val currentRuleModelList = mutableListOf<RuleModel>()

            val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern)
            val foundIndexes2 = MainKt().performKMPSearch(psiFile.text, secondPattern)
            val foundIndexes3 = MainKt().performKMPSearch(psiFile.text, thirdPattern)

            foundIndexes.forEach {
                foundIndexes2.forEach {
                    val list = it
                }

                val consoleMessage = "\n" +
                        psiFile.originalFile.virtualFile.path + ":" + (psiFile.viewProvider.document.getLineNumber(
                    it
                ) + 1) + " $ruleMessage"
                //annotator

                currentRuleModelList.add(
                    RuleModel(
                        it,
                        it + pattern.length,
                        ruleMessage,
                        consoleMessage
                    )
                )
                printConsoleView(consoleMessage)
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