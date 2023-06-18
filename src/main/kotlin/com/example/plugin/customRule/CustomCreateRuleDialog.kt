package com.example.plugin.customRule

import com.example.plugin.data.RuleElement
import com.example.plugin.data.ScreenGeneratorComponent
import com.example.plugin.data.TypeFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

class CustomCreateRuleDialog(project: Project) : DialogWrapper(true) {

    private val screenGeneratorComponent = ScreenGeneratorComponent.getInstance(project)

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
        val id = screenGeneratorComponent.settings.ruleList.last().id + 1
        screenGeneratorComponent.settings.ruleList.add(
            RuleElement(
                id,
                panel.nameTextField.text + ": ${panel.messageRecommendationField.text}",
                isSelected = true,
                isCustomRule = true
            )
        )
        panel.apply {
            val customRuleModel = CustomRuleModel(
                id,
                nameTextField.text,
                templateFindErrorField.text,
                templateCorrectErrorField.text,
                messageRecommendationField.text,
                (categoryComboBox.selectedItem as TypeFile).name
            )
            screenGeneratorComponent.customRuleModels.add(customRuleModel)
        }
        close(OK_EXIT_CODE)
    }

    override fun createCenterPanel() = panel

    fun saveNewRule() {

    }
}
