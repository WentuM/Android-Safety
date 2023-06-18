package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class BroadCastReceiverPermission(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private val ruleMessage =
        "This requires the use of permission flags when registering a broadcast message receiver. That is, the following registration option is required: “context.registerReceiver(broadcastReceiver, intentFilter, “permission.ALLOW_BROADCAST”, handler)”. That is, you must explicitly specify the permissions that the sender must have."

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {
            val pattern = "registerReceiver"
            val pattern2 = "permission."

            val currentRuleModelList = mutableListOf<RuleModel>()

            val foundIndexPattern = MainKt().performKMPSearch(psiFile.text, pattern)


            if (foundIndexPattern.isNotEmpty()) {
                val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern2)

                println(foundIndexes.toString())

                foundIndexes.forEach {

                    val consoleMessage = "\n" +
                            psiFile.originalFile.virtualFile.path + ":" + (psiFile.viewProvider.document.getLineNumber(
                        foundIndexPattern.first()
                    ) + 1) + " $ruleMessage"
                    //annotator

                    currentRuleModelList.add(
                        RuleModel(
                            foundIndexPattern.first(),
                            foundIndexPattern.first() + pattern.length,
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
        }
    }

    private fun printConsoleView(consoleMessage: String) {
        consoleView.print(consoleMessage, ConsoleViewContentType.NORMAL_OUTPUT)
    }
}