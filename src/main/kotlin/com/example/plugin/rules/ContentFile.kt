package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class ContentFile(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private val ruleMessage =
        "Sharing data between applications - When sharing data, you must use content:// instead of file:// in the URI. \" +\n" +
                "\"As an example, to provide the display of a PDF file in a standalone application - Uri.parse(\\\"content://com.example/example.pdf\\\")"

    private val fixMessage = "content"

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {
            val pattern = "parse.(file://"

            var index = 0
            var currentPsiFileTextLength = psiFile.viewProvider.document.textLength
            var lastStartOffset = 0

            val currentRuleModelList = mutableListOf<RuleModel>()

            val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern)

            foundIndexes.forEach {

                val consoleMessage = "\n" +
                        psiFile.originalFile.virtualFile.path + ":" + (psiFile.viewProvider.document.getLineNumber(
                    it
                ) + 1) + " $ruleMessage"
                //annotator

                if (isNeedFix) {
                    if (it > lastStartOffset) {
                        psiFile.viewProvider.document?.replaceString(
                            it + index,
                            it + pattern.length + index,
                            fixMessage
                        )
                        index += (psiFile.viewProvider.document.textLength - currentPsiFileTextLength)
                    } else {
                        psiFile.viewProvider.document?.replaceString(it, it + pattern.length, fixMessage)
                    }
                    lastStartOffset = it
                    currentPsiFileTextLength = psiFile.viewProvider.document.textLength
                } else {
                    currentRuleModelList.add(
                        RuleModel(
                            it,
                            it + pattern.length,
                            ruleMessage,
                            consoleMessage,
                            fixMessage
                        )
                    )
                }

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