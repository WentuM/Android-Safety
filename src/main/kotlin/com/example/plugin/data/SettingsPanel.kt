package com.example.plugin.data

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.labels.LinkLabel
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JPanel

class SettingsPanel(project: Project) : JPanel() {

    val ruleElementsPanel = RuleElementsPanel()
//    val categoriesPanel = CategoriesPanel()
//    val screenElementDetailsPanel = ScreenElementDetailsPanel()
//    val customVariablesPanel = CustomVariablesPanel()
//    val customVariableDetailsPanel = CustomVariableDetailsPanel()
//    val categoryDetailsPanel = CategoryDetailsPanel()
//    val codePanels: Map<FileType, CodePanel>

//    var onTemplateTextChanged: ((String) -> Unit)? = null
//        set(value) {
//            field = value
//            codePanels.forEach { _, panel -> panel.onTemplateTextChanged = value }
//        }

    var onHelpClicked: (() -> Unit)? = null

    init {
        layout = BorderLayout()

        val helpPanel = JPanel(FlowLayout(FlowLayout.TRAILING)).apply {
            add(LinkLabel.create("Help") { onHelpClicked?.invoke() })
        }
        val contentPanel = JPanel().apply {
            layout = BorderLayout()

            add(ruleElementsPanel)

//            val topPanel = JPanel().apply {
//                layout = BoxLayout(this, BoxLayout.Y_AXIS)
//
//                add(ruleElementsPanel)
//            }
//
//                add(
//                    JBSplitter(0.3f).apply {
//                        firstComponent = categoriesPanel
//                        secondComponent = JBSplitter(0.4f).apply {
//                            firstComponent = customVariablesPanel
//                            secondComponent = JPanel().apply {
//                                layout = BoxLayout(this, BoxLayout.Y_AXIS)
//                                add(categoryDetailsPanel)
//                                add(customVariableDetailsPanel)
//                            }
//                        }
//                    }
//                )
//                add(
//                    JBSplitter(0.3f).apply {
//                        firstComponent = screenElementsPanel
//                        secondComponent = screenElementDetailsPanel
//                    }
//                )
//            }

//            add(topPanel, BorderLayout.PAGE_START)

//            add(
//                JPanel().apply {
//                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
//                    codePanels.forEach { (_, panel) -> add(panel) }
//                },
//                BorderLayout.CENTER
//            )
//            add(topPanel)
        }

        add(helpPanel, BorderLayout.PAGE_START)
        add(contentPanel, BorderLayout.PAGE_START)
    }

//    fun render(state: SettingsState) {
//        categoriesPanel.render(state)
//        categoryDetailsPanel.render(state)
//        customVariablesPanel.render(state)
//        customVariableDetailsPanel.render(state)
//        screenElementsPanel.render(state)
//        screenElementDetailsPanel.render(state)
//        codePanels.values.forEach { it.render(state) }
//        if (state.selectedElementIndex == null) {
//            codePanels[FileType.KOTLIN]?.isVisible = true
//        }
//    }

    fun resetRules() {

    }
}
