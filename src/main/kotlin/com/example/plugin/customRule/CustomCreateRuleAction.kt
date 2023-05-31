package com.example.plugin.customRule

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class CustomCreateRuleAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        CustomCreateRuleDialog(e.project!!).show()
    }
}