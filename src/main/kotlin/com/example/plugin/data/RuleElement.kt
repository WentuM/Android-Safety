package com.example.plugin.data

import java.io.Serializable

data class RuleElement(
    var id: Int = 0,
    var name: String = "",
    var isSelected: Boolean = true,
    var isCustomRule: Boolean = false
): Serializable
