package com.example.plugin.annotator

data class AnnotatorRuleModel(
    var fileName: String = "",
    var ruleList: MutableList<RuleModel> = mutableListOf(),
    var hashCodeFile: Int = 0
)
