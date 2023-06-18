package com.example.plugin.data

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.labels.LinkLabel
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

class SettingsPanel(val project: Project) : JPanel() {

    private val ruleListComponent = ScreenGeneratorComponent.getInstance(project).settings.ruleList

    private val ruleElementsPanel = RuleElementsPanel(ruleListComponent, ::onCheckBoxClicked)

    var onHelpClicked: (() -> Unit)? = null

    var isModify = false

    init {
        layout = BorderLayout()

        val helpPanel = JPanel(FlowLayout(FlowLayout.TRAILING)).apply {
            add(LinkLabel.create("Help") { onHelpClicked?.invoke() })
        }
        val contentPanel = JPanel().apply {
            layout = BorderLayout()

            println(ruleListComponent)
            add(ruleElementsPanel)
        }

        add(helpPanel, BorderLayout.PAGE_START)
        add(contentPanel, BorderLayout.PAGE_START)
    }

    private fun onCheckBoxClicked() {
        isModify = true
    }

    fun applyRuleSettings() {
        ScreenGeneratorComponent.getInstance(project).run {
            this.settings = Settings(ruleList = ruleElementsPanel.listModel)
            this.removeCustomRuleByName(ruleElementsPanel.removeCustomList)
        }
        isModify = false
    }

    fun resetRules() {

    }
}
