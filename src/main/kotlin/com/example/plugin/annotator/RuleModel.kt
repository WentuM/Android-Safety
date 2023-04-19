package com.example.plugin.annotator

data class RuleModel(
    val startOffset: Int,
    val endOffset: Int,
    val ruleMessage: String
)