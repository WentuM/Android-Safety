package com.example.plugin.annotator

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import org.gradle.kotlin.dsl.execution.text
import org.jetbrains.kotlin.idea.inspections.findExistingEditor
import org.mozilla.javascript.ast.XmlExpression

class XmlRuleAnnotator: Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val fileName = element.containingFile.name

        if (!element.isValid) {
            return
        }

        if (!element.containingFile.name.endsWith(".xml")) {
            return
        }

        if (element.toString() != "PsiElement(XML_DOCUMENT)") {
            return
        }

//        element.containingFile.viewProvider.document.addDocumentListener(object: DocumentListener {
//            override fun documentChanged(event: DocumentEvent) {
//                super.documentChanged(event)
//                println("moveOffset3 = ${event.moveOffset}")
//                println("oldLength = ${event.oldLength}")
//                println("newLength = ${event.newLength}")
//            }
//        })

        val ruleModelList = AnnotatorRepository.getAnnotatorRuleModelsByFileName(fileName)
        //MainActivity.kt
        println(ruleModelList.toString())

//        println(element.toString())
//        val annotatorRepository = AnnotatorRepository
//        val keyRange = TextRange(annotatorRepository.startOffset, annotatorRepository.endOffset)
//
//        val keyRange2 = TextRange(annotatorRepository.startOffset2, annotatorRepository.endOffset2)

//        println(keyRange)

        for (ruleModel in ruleModelList) {
            val textRange = TextRange(ruleModel.startOffset, ruleModel.endOffset)
            holder.newAnnotation(HighlightSeverity.WARNING, ruleModel.ruleMessage)
                .range(textRange)
                .highlightType(ProblemHighlightType.WARNING)
//                .withFix(SimpleCreatePropertyQuickFix(key))
                .create()
        }
//        holder.newAnnotation(HighlightSeverity.WARNING, "Unresolved property")
//            .range(keyRange)
//            .highlightType(ProblemHighlightType.WARNING)
////                .withFix(SimpleCreatePropertyQuickFix(key))
//            .create()
//
//        holder.newAnnotation(HighlightSeverity.WARNING, "Unresolved property")
//            .range(keyRange2)
//            .highlightType(ProblemHighlightType.WARNING)
////                .withFix(SimpleCreatePropertyQuickFix(key))
//            .create()
    }
}