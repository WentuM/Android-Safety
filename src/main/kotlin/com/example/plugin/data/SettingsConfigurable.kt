package com.example.plugin.data

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.swing.JComponent
import kotlin.coroutines.CoroutineContext

class SettingsConfigurable(private val project: Project) : Configurable {

    private lateinit var panel: SettingsPanel

    override fun getDisplayName() = "Android Safety Plugin"

    override fun apply() {
        panel.applyRuleSettings()
    }

    override fun reset() {
        panel.resetRules()
    }

    override fun isModified(): Boolean {
        return panel.isModify
    }

    override fun createComponent(): JComponent {
        panel = SettingsPanel(project)

//        panel.categoriesPanel.onAddClicked = { launch { viewModel.actionFlow.emit(SettingsAction.AddCategory) } }
//        panel.categoriesPanel.onRemoveClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.RemoveCategory(it)) } }
//        panel.categoriesPanel.onMoveUpClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.MoveUpCategory(it)) } }
//        panel.categoriesPanel.onMoveDownClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.MoveDownCategory(it)) } }
//        panel.categoriesPanel.onItemSelected =
//            { launch { viewModel.actionFlow.emit(SettingsAction.SelectCategory(it)) } }
//
//        panel.categoryDetailsPanel.onNameTextChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeCategoryName(it)) } }
//
//        panel.customVariableDetailsPanel.onNameTextChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeCustomVariableName(it)) } }
//
//        panel.customVariablesPanel.onAddClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.AddCustomVariable) } }
//        panel.customVariablesPanel.onRemoveClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.RemoveCustomVariable(it)) } }
//        panel.customVariablesPanel.onMoveUpClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.MoveUpCustomVariable(it)) } }
//        panel.customVariablesPanel.onMoveDownClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.MoveDownCustomVariable(it)) } }
//        panel.customVariablesPanel.onItemSelected =
//            { launch { viewModel.actionFlow.emit(SettingsAction.SelectCustomVariable(it)) } }
//
//        panel.screenElementsPanel.onAddClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.AddScreenElement) } }
//        panel.screenElementsPanel.onRemoveClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.RemoveScreenElement(it)) } }
//        panel.screenElementsPanel.onMoveDownClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.MoveDownScreenElement(it)) } }
//        panel.screenElementsPanel.onMoveUpClicked =
//            { launch { viewModel.actionFlow.emit(SettingsAction.MoveUpScreenElement(it)) } }
//        panel.screenElementsPanel.onItemSelected =
//            { launch { viewModel.actionFlow.emit(SettingsAction.SelectScreenElement(it)) } }
//
//        panel.screenElementDetailsPanel.onNameTextChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeName(it)) } }
//        panel.screenElementDetailsPanel.onFileNameTextChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeFileName(it)) } }
//        panel.screenElementDetailsPanel.onFileTypeIndexChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeFileType(it)) } }
//        panel.screenElementDetailsPanel.onSubdirectoryTextChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeSubdirectory(it)) } }
//        panel.screenElementDetailsPanel.onSourceSetTextChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeSourceSet(it)) } }
//        panel.screenElementDetailsPanel.onAndroidComponentIndexChanged =
//            { launch { viewModel.actionFlow.emit(SettingsAction.ChangeAndroidComponent(it)) } }
//
//        panel.onTemplateTextChanged = { launch { viewModel.actionFlow.emit(SettingsAction.ChangeTemplate(it)) } }
//        panel.onHelpClicked = { launch { viewModel.actionFlow.emit(SettingsAction.ClickHelp) } }
//
//        launch { viewModel.state.collect { panel.render(it) } }
//        launch { viewModel.effect.collect { it.handle() } }

        return panel
    }
}
