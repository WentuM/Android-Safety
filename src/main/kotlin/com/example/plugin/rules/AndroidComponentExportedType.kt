package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class AndroidComponentExportedType(
    val project: Project,
    val psiFile: PsiFile,
    val consoleView: ConsoleView
) {

    fun show() {
        val pattern = "absolutePath"
        val annotatorRuleModel = AnnotatorRuleModel(psiFile.name, mutableListOf(), psiFile.text.hashCode())

        val foundIndexes = MainKt().performKMPSearch(psiFile.text, pattern)

        foundIndexes.forEach {
            //annotator
            annotatorRuleModel.ruleList.add(
                RuleModel(
                    it,
                    it + pattern.length,
                    "Where textNoSuggestions?"
                )
            )

            printConsoleView(psiFile, it)

            //if resolved
//            psiFile.viewProvider.document?.replaceString(it, it + pattern.length, "canonicalPath")
        }

        if (annotatorRuleModel.ruleList.isNotEmpty()) {
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