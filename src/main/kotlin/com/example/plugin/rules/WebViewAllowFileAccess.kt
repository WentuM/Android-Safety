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

class WebViewAllowFileAccess(
    val project: Project,
    val consoleView: ConsoleView,
    val isNeedCheck: Boolean,
    val isNeedFix: Boolean
) : RuleRealization {

    private val webViewTemplate = "WebView"
    private val attributeNameTemplate = "android:id"
    private val allowFileAccessTemplate = "allowFileAccess = false"
    private val allowContentAccessTemplate = "allowContentAccess = false"

    private val foundIds = mutableListOf<String>()

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {
//        firstTestXml()
            //вынести, чтобы один файл добавлялся, а к нему уже список правил
            val annotatorRuleModel = AnnotatorRuleModel(psiFile.name, mutableListOf(), psiFile.text.hashCode())

            foundIds.removeIf { pattern ->

                var removeFlag = false

                val foundIndexesPattern = MainKt().performKMPSearch(psiFile.text, pattern)

                if (foundIndexesPattern.isNotEmpty()) {
                    val foundFullPattern = MainKt().performKMPSearch(psiFile.text, "allowFileAccess = false")
                    if (foundFullPattern.isNotEmpty()) {
                        removeFlag = true
                        println(foundFullPattern.toString())
                    } else {

                        val firstFoundIndex = foundIndexesPattern.first()
                        //annotator
                        annotatorRuleModel.ruleList.add(
                            RuleModel(
                                firstFoundIndex,
                                firstFoundIndex + pattern.length,
                                "It is necessary to disable the ability to access external files using WebView, or rather, you must explicitly set allowFileAccess = false"
                            )
                        )

                        removeFlag = true

                        printConsoleView(psiFile, firstFoundIndex)
                    }
                }
                removeFlag

//        if resolved
//            psiFile.viewProvider.document?.replaceString(it, it + pattern.length, "canonicalPath")
            }

            if (annotatorRuleModel.ruleList.isNotEmpty()) {
                AnnotatorRepository.annotatorFileNameList.add(annotatorRuleModel.fileName)
                AnnotatorRepository.annotatorRuleModelList.add(annotatorRuleModel)
            }
        }
    }

    fun firstTestXml(psiFile: PsiFile) {
        if (isNeedCheck) {
            val webView = "WebView"
            val attributeName = "android:id"
            //правильное нахождение айди у <WebView>
            val document = psiFile.viewProvider.getPsi(XMLLanguage.INSTANCE) as XmlFile
            document.rootTag?.findSubTags(webView)?.forEach {
                //видимо можно так
//            it.getAttribute(attributeName)?.textRange
                it.getAttribute(attributeName)?.value?.substring(5)?.let { it1 -> foundIds.add(it1) }
            }
        }
    }

    private fun printConsoleView(layoutFile: PsiFile, passwordIndex: Int) {
        consoleView.print(
            "\n" +
                    layoutFile.originalFile.virtualFile.path + ":" + (layoutFile.viewProvider.document.getLineNumber(
                passwordIndex
            ) + 1) + " It is necessary to disable the ability to access external files using WebView, or rather, you must explicitly set allowFileAccess = false",
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