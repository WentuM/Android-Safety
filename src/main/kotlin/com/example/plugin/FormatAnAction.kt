package com.example.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction

private const val COMMAND_NAME = "Android Safety"
private const val GROUP_ID = "ANDROID_SAFETY_ID"

class FormatAnAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        WriteCommandAction.runWriteCommandAction(e.project!!, COMMAND_NAME, GROUP_ID, {
            AndroidSafetyService(e.project!!, true)
        })
    }
}