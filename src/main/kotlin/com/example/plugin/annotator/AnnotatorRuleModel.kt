package com.example.plugin.annotator

data class AnnotatorRuleModel(
    val fileName: String,
    val ruleList: MutableList<RuleModel>,
    val hashCodeFile: Int
)
