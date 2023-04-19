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
        // Ensure the Psi Element is an expression

        if (element.toString() != "KtFile: $fileName") {
            return
        }

        if (!element.isValid) {
            return
        }


//        if (element !is PsiLiteralExpression) {
//            return
//        }

        if (!AnnotatorRepository.annotatorFileNameList.contains(fileName)) {
            return
        }

//        println(element.toString())
//        println(element.containingFile.toString())
        //MainActivity.kt
//        println(element.containingFile.name)

        // Ensure the Psi element contains a string that starts with the prefix and separator

//        println(element.toString())


        // Ensure the Psi element contains a string that starts with the prefix and separator
//        val literalExpression = element as PsiLiteralExpression
//        val value: String? = if (literalExpression.value is String) literalExpression.value as String else null
//        if (value == null || !value.startsWith(SIMPLE_PREFIX_STR + SIMPLE_SEPARATOR_STR)) {
//            return
//        }
//        println("something3")


        // Define the text ranges (start is inclusive, end is exclusive)
        // "simple:key"
        //  01234567890

        // Define the text ranges (start is inclusive, end is exclusive)
        // "simple:key"
        //  01234567890
//        val prefixRange: TextRange = TextRange.from(element.textRange.startOffset, SIMPLE_PREFIX_STR.length + 1)
//        val separatorRange: TextRange = TextRange.from(prefixRange.endOffset, SIMPLE_SEPARATOR_STR.length)

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
        // highlight "simple" prefix and ":" separator

        // highlight "simple" prefix and ":" separator
//        holder.newSilentAnnotation(HighlightSeverity.WARNING)
//            .range(keyRange).textAttributes(DefaultLanguageHighlighterColors.KEYWORD).create()
//        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
//            .range(separatorRange).textAttributes(SimpleSyntaxHighlighter.SEPARATOR).create()


        // Get the list of properties for given key


        // Get the list of properties for given key
//        val key: String = value.substring(SIMPLE_PREFIX_STR.length() + SIMPLE_SEPARATOR_STR.length())
//        val properties: List<SimpleProperty> = SimpleUtil.findProperties(element.project, key)
//        if (properties.isEmpty()) {
//        println(keyRange)

//        } else {
//            // Found at least one property, force the text attributes to Simple syntax value character
//            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
//                .range(keyRange).textAttributes(DefaultLanguageHighlighterColors.KEYWORD).create()
//        }
    }
}