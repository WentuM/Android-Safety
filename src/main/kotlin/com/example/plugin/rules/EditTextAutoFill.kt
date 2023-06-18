package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile

class EditTextAutoFill(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
): RuleRealization {

    private val foundIds = mutableListOf<String>()

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {

        foundIds.forEach { pattern ->

            val foundIndexesPattern = MainKt().performKMPSearch(psiFile.text, pattern)

            if (foundIndexesPattern.isNotEmpty()) {
                val foundFullPattern = MainKt().performKMPSearch(psiFile.text, "allowFileAccess = false")
                if (foundFullPattern.isNotEmpty()) {
                    foundIds.remove(pattern)
                    println(foundFullPattern.toString())
                } else {
                    foundIndexesPattern.forEach {
                        //annotator
                        annotatorRuleModel.ruleList.add(
                            RuleModel(
                                it,
                                it + pattern.length,
                                "It is necessary to disable the ability to access external files using WebView, or rather, you must explicitly set allowFileAccess = false"
                            )
                        )

                        printConsoleView(psiFile, it)
                    }
                }
            }

//        if resolved
//            psiFile.viewProvider.document?.replaceString(it, it + pattern.length, "canonicalPath")
        }

        if (annotatorRuleModel.ruleList.isNotEmpty()) {
            AnnotatorRepository.annotatorRuleModelList.add(annotatorRuleModel)
        }
    }

    fun firstTestXml(psiFile: PsiFile) {
        val editText = "WebView"
        //правильное нахождение айди у <WebView>
        val document = psiFile.viewProvider.getPsi(XMLLanguage.INSTANCE) as XmlFile
        document.rootTag?.findSubTags(editText)?.forEach {
            it.getAttribute("android:id")?.value?.substring(5)?.let { it1 -> foundIds.add(it1) }
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