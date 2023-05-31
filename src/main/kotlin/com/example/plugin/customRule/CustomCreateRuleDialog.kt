package com.example.plugin.customRule

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

class CustomCreateRuleDialog(project: Project) : DialogWrapper(true) {

    companion object {
        private const val CUSTOM_CREATE_DIALOG_TITLE = "Create Custom Rule"
    }

    private val panel = CustomCreateRulePanel()

    init {
        title = CUSTOM_CREATE_DIALOG_TITLE
        panel.onCategoryIndexChanged = { }
        init()
    }

    override fun doOKAction() {
//            panel.packageTextField.text,
//            panel.nameTextField.text,
//            panel.categoryComboBox.selectedItem as TypeFile
    }

    override fun createCenterPanel() = panel
}
