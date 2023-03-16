package com.example.plugin.toolwindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class MyToolWindowFactory : ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
//        val myToolWindow = MyToolWindow(toolWindow)
        val contentFactory = ContentFactory.SERVICE.getInstance()
//        val content = contentFactory.createContent(myToolWindow.content, "", false)
//        toolWindow.contentManager.addContent(content)
    }
}