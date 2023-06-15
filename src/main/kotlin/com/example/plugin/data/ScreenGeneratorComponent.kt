package com.example.plugin.data

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.customRule.CustomRuleModel
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import java.io.Serializable

@State(
    name = "ScreenGeneratorComponent",
    storages = [Storage(value = "screenGeneratorComponent.xml")]
)
class ScreenGeneratorComponent : Serializable, PersistentStateComponent<ScreenGeneratorComponent> {

    companion object {
        fun getInstance(project: Project) = ServiceManager.getService(project, ScreenGeneratorComponent::class.java)
    }

    var settings: Settings = Settings()

    var settingsRules: MutableList<AnnotatorRuleModel> = mutableListOf()

    var customRuleModels: MutableList<CustomRuleModel> = mutableListOf()

    override fun getState(): ScreenGeneratorComponent = this

    override fun loadState(state: ScreenGeneratorComponent) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun getHashCodeFileByFileName(fileName: String): Int {
        return settingsRules.find { it.fileName == fileName }?.hashCodeFile ?: 0
    }

    fun getRuleModelsByFileName(fileName: String): List<RuleModel> {
        return settingsRules.find { it.fileName == fileName }?.ruleList ?: emptyList()
    }

    fun getAnnotatorRuleModelsByFileName(fileName: String): AnnotatorRuleModel? {
        return settingsRules.find { it.fileName == fileName }
    }

    fun updateAnnotatorRuleModelsByFileName(fileName: String, newRuleModelList: List<RuleModel>) {
        settingsRules.find { it.fileName == fileName }?.ruleList?.addAll(newRuleModelList)
    }
}
