package com.example.plugin

import com.example.plugin.rules.EditTextConfidentType
import com.example.plugin.toolwindow.MyToolWindowFactory
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.JBColor
import com.jetbrains.rd.util.string.printToString
import org.gradle.api.tasks.GradleBuild
import org.gradle.plugins.ide.internal.tooling.GradleBuildBuilder


private const val COMMAND_NAME = "Screen Generator"
private const val GROUP_ID = "SCREEN_GENERATOR_ID"

class CheckAnAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
//        ExternalSystemUtil.runTask(settings, DefaultRunExecutor.EXECUTOR_ID, e.project!!, GradleConstants.SYSTEM_ID,
//            null, ProgressExecutionMode.IN_BACKGROUND_ASYNC, false);
//        val editor = e.getData(CommonDataKeys.EDITOR)!!
//        val document = editor.document
//        val startOffset: Int = document.getLineStartOffset(4)
//        val endOffset: Int = document.getLineEndOffset(7) // assuming open line range [4-8)
//
//        val highlighter: RangeHighlighter = editor.markupModel
//            .addRangeHighlighter(startOffset, endOffset, 0, null, HighlighterTargetArea.EXACT_RANGE)
//        highlighter.errorStripeMarkColor = JBColor.MAGENTA

//        toolWindow?.let { MyToolWindowFactory().createToolWindowContent(project, it) }

//        val a = TextConsoleBuilderFactory.getInstance().createBuilder(e.project!!).console.print(
//            "qwe",
//            ConsoleViewContentType.NORMAL_OUTPUT
//        )
//        val project = e.project!!


        WriteCommandAction.runWriteCommandAction(e.project!!, COMMAND_NAME, GROUP_ID, {
            AndroidSafetyCheck(e.project!!)
        })
    }
}