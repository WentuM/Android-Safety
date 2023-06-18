package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class TestRule(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private var ruleMessage =
        "When configuring pendingIntent, you must specify PendingIntent.FLAG_IMMUTABLE in the object creation parameters."

    private var i = 0

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        val pattern = "pendingIntent"
        val pattern2 = "PendingIntent.FLAG_IMMUTABLE"

        val currentRuleModelList = mutableListOf<RuleModel>()

        val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern2)

        foundIndexes.forEach {

            val consoleMessage = "\n" +
                    psiFile.originalFile.virtualFile.path + ":" + (psiFile.viewProvider.document.getLineNumber(
                it
            ) + 1) + " $ruleMessage"
            //annotator

            i++

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

    private fun printConsoleView(consoleMessage: String) {
        consoleView.print(consoleMessage, ConsoleViewContentType.NORMAL_OUTPUT)
    }
}