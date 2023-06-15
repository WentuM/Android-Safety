package com.example.plugin.annotator

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression

const val SIMPLE_PREFIX_STR = "simple"
const val SIMPLE_SEPARATOR_STR = ":"

class KotlinRuleAnnotator: Annotator {

    private val annotatorRepository = AnnotatorRepository

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val fileName = element.containingFile.name

        if (element.toString() != "KtFile: $fileName") {
            return
        }

        if (!element.isValid) {
            return
        }

        if (!AnnotatorRepository.annotatorFileNameList.contains(fileName)) {
            return
        }

        val ruleModelList = AnnotatorRepository.getAnnotatorRuleModelsByFileName(fileName)

        for (ruleModel in ruleModelList) {

//            val keyRange = TextRange(annotatorRepository.startOffset, annotatorRepository.endOffset)
            val keyRange = TextRange(ruleModel.startOffset, ruleModel.endOffset)
            holder.newAnnotation(HighlightSeverity.WARNING, ruleModel.ruleMessage)
                .range(keyRange)
                .highlightType(ProblemHighlightType.WARNING) // ** Tutorial step 18.3 - Add a quick fix for the string containing possible properties
//                .withFix(SimpleCreatePropertyQuickFix(key))
                .create()
        }
    }
}