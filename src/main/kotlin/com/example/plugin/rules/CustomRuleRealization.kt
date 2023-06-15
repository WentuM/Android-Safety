package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.customRule.CustomRuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.psi.PsiFile

class CustomRuleRealization(
    private val customRuleModel: CustomRuleModel,
    private val consoleView: ConsoleView,
    private val isNeedCheck: Boolean
): RuleRealization {

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        val pattern = customRuleModel.templateFindError
        val annotatorRuleModel = AnnotatorRuleModel(psiFile.name, mutableListOf(), psiFile.text.hashCode())

        val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern)

        foundIndexes.forEach {
            //annotator
            annotatorRuleModel.ruleList.add(
                RuleModel(
                    it,
                    it + pattern.length,
                    customRuleModel.messageRecommendation
                )
            )

            printConsoleView(psiFile, it)

            //if resolved

            if (isNeedCheck) {
                psiFile.viewProvider.document?.replaceString(
                    it,
                    it + pattern.length,
                    customRuleModel.templateCorrectError
                )
            }
        }

        if (annotatorRuleModel.ruleList.isNotEmpty()) {
            AnnotatorRepository.annotatorFileNameList.add(annotatorRuleModel.fileName)
            AnnotatorRepository.annotatorRuleModelList.add(annotatorRuleModel)
        }
    }

    private fun printConsoleView(layoutFile: PsiFile, passwordIndex: Int) {
        consoleView.print(
            "\n" +
                    layoutFile.originalFile.virtualFile.path + ":" + (layoutFile.viewProvider.document.getLineNumber(
                passwordIndex
            ) + 1) + " При работе с файлами прежде, чем предоставлять пользователям доступ к файлам, необходимо проверять относится ли этот файл по-настоящему к нашему приложению. В таком случае использование absolutePath будем ошибочным, поскольку один файл в файловой системе может иметь бесконечное количество абсолютных путей. Однако канонический путь всегда будет уникальным. Следует использовать canonicalPath.",
            ConsoleViewContentType.NORMAL_OUTPUT
        )
    }
}