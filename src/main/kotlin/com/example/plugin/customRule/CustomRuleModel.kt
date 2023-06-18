package com.example.plugin.customRule

data class CustomRuleModel(
    var id: Int = -1,
    var name: String = "",
    var templateFindError: String = "",
    var templateCorrectError: String = "",
    var messageRecommendation: String = "",
    var categoryComboBox: String = ""
)
