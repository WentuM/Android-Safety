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

    private val ruleMessage =
        "You need to disable the ability to access external files using the WebView. Here it is possible by setting the WebView settings attributes “allowFileAccess = false”, “allowContentAccess = false”."

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        if (isNeedCheck) {

            val currentRuleModelList = mutableListOf<RuleModel>()

            foundIds.removeIf { pattern ->

                var removeFlag = false

                val foundIndexesPattern = MainKt().performKMPSearch(psiFile.text, pattern)

                if (foundIndexesPattern.isNotEmpty()) {
                    val foundFullPattern = MainKt().performKMPSearch(psiFile.text, allowFileAccessTemplate)
                    if (foundFullPattern.isNotEmpty()) {
                        removeFlag = true
                    } else {

                        val firstFoundIndex = foundIndexesPattern.first()

                        val consoleMessage = "\n" +
                                psiFile.originalFile.virtualFile.path + ":" + (psiFile.viewProvider.document.getLineNumber(
                            firstFoundIndex
                        ) + 1) + " $ruleMessage"

                        val fixMessage =
                            "\n$pattern.settings.allowFileAccess = false\n$pattern.settings.allowContentAccess=false"

                        if (isNeedFix) {
                            psiFile.viewProvider.document?.replaceString(firstFoundIndex, firstFoundIndex + pattern.length, fixMessage)
                        } else {
                            currentRuleModelList.add(
                                RuleModel(
                                    firstFoundIndex,
                                    firstFoundIndex + pattern.length,
                                    ruleMessage,
                                    consoleMessage,
//                                    fixMessage
                                )
                            )
                        }
                        printConsoleView(consoleMessage)

                        removeFlag = true
                    }
                }
                removeFlag
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

    private fun printConsoleView(consoleMessage: String) {
        consoleView.print(consoleMessage, ConsoleViewContentType.NORMAL_OUTPUT)
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