package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class GetCanonicalPathType(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private val ruleMessage =
        "When working with files, before giving users access to files, it is necessary to check " +
                "whether this file is really related to our application. In this case, the use of absolutePath " +
                "will be erroneous, since one file in the file system can have an infinite number of absolute paths. " +
                "However, the canonical path will always be unique. You should use canonicalPath."

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {
            val pattern = "absolutePath"

            val currentRuleModelList = mutableListOf<RuleModel>()

            val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern)

            foundIndexes.forEach {

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

                //if resolved
                if (isNeedFix) {
                    psiFile.viewProvider.document?.replaceString(it, it + pattern.length, "canonicalPath")
                }
            }

            if (currentRuleModelList.isNotEmpty()) {
                if (!AnnotatorRepository.annotatorFileNameList.contains(psiFile.name)) {
                    AnnotatorRepository.annotatorFileNameList.add(psiFile.name)
                }
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

    private fun printConsoleView(consoleMessage: String) {
        consoleView.print(consoleMessage, ConsoleViewContentType.NORMAL_OUTPUT)
    }
}