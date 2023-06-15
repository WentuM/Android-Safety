package com.example.plugin.annotator

data class RuleModel(
    var startOffset: Int = 0,
    var endOffset: Int = 0,
    var ruleMessage: String = "",
    var consoleMessage: String = ""
)