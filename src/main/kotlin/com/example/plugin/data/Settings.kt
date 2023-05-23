package com.example.plugin.data

import java.io.Serializable

data class Settings(
    var s: String = "",
    var ruleList: MutableList<RuleElement> = listModelByDefault()
): Serializable

private fun listModelByDefault(): MutableList<RuleElement> {
    val listModel = mutableListOf<RuleElement>().apply {
        add(RuleElement(0, "Rule 1 -", true))
        add(RuleElement(1, "Rule 2 -", true))
        add(RuleElement(2, "Rule 3 -", true))
        add(RuleElement(3, "Rule 4 -", true))
        add(RuleElement(4, "Rule 5 -", true))
    }
    return listModel
}