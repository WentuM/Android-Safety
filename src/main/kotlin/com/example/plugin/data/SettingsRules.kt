package com.example.plugin.data

data class SettingsRules(
    var nameFile: String = "",
    var ruleList: MutableList<SettingsRuleModel> = mutableListOf(),
    var hashCode: Int = 0,
)

data class SettingsRuleModel(
    var startOffset: Int = 0,
    var endOffset: Int = 0,
    var textMessage: String = "",
)
