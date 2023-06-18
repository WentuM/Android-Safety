package com.example.plugin.annotator

object AnnotatorRepository {
    val annotatorRuleModelList = mutableListOf<AnnotatorRuleModel>()

    fun getAnnotatorRuleModelsByFileName(fileName: String): List<RuleModel> {
        return annotatorRuleModelList.find { it.fileName == fileName }?.ruleList ?: emptyList()
    }

    fun updateAnnotatorRuleModelsByFileName(fileName: String, newRuleModelList: List<RuleModel>) {
        annotatorRuleModelList.find { it.fileName == fileName }?.ruleList?.addAll(newRuleModelList)
    }

    fun setAnnotatorRuleModelsByFileName(fileName: String, newRuleModelList: MutableList<RuleModel>) {
        annotatorRuleModelList.find { it.fileName == fileName }?.ruleList = newRuleModelList
    }
}