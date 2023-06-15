package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRuleModel
import com.intellij.psi.PsiFile

interface RuleRealization {

    fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel)
}