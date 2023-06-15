package com.example.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction


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
            AndroidSafetyService(e.project!!, false)
        })
    }
}