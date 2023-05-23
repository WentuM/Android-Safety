package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.testAlgorithm.MainKt
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.lang.StdLanguages
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.jetbrains.kotlin.j2k.getContainingClass
import org.jetbrains.uast.*

class WebViewAllowFileAccess(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean
) {

    private val foundIds = mutableListOf<String>()

    fun show(psiFile: PsiFile) {
//        firstTestXml()
        //вынести, чтобы один файл добавлялся, а к нему уже список правил
        val annotatorRuleModel = AnnotatorRuleModel(psiFile.name, mutableListOf(), psiFile.text.hashCode())

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
            AnnotatorRepository.annotatorFileNameList.add(annotatorRuleModel.fileName)
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

    fun getMethods() {
//        println(psiFile.toUElement()?)
//        println(psiFile.text.matches("webview[.]settings".toRegex()))

//        val psiMethod = PsiTreeUtil.getParentOfType(psiFile, PsiMethod::class.java)
//        println("method " + psiMethod)
//        println("methods " + (psiFile.getContainingClass()?.allMethods ?: " "))
//        val file = psiFile.toUElement() as UFile
//
////        println("asda: " + MethodReferencesSearch.search())
//
//        file.classes.forEach {
//            println(it.findFieldByName("webview", false)?.startOffset.toString())
//            it.findMethodsByName("show").forEach { jvmMethod ->
//                println("jvm: " + jvmMethod.name)
//                println(jvmMethod.sourceElement?.textOffset)
//                val jvmMethodStartOffset = jvmMethod.sourceElement?.startOffset
//                val jvmMethodEndOffset = jvmMethod.sourceElement?.endOffset
////                println(jvmMethod.sourceElement?.textRange)
//                if (jvmMethodStartOffset != null && jvmMethodEndOffset != null) {
////                    psiFile.viewProvider.document?.replaceString(
////                        jvmMethodStartOffset, jvmMethodEndOffset,
////                        "getCanonicalPath()"
////                    )
//                }
//            }
//            it.methods.forEach { uMethod ->
//                println("methooods: " + uMethod.name)
//            }
//        }
    }
}