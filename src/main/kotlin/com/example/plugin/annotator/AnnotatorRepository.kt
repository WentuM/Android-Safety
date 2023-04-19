package com.example.plugin.annotator

object AnnotatorRepository {

    const val startOffset: Int = 0
    const val endOffset: Int = 10

    const val startOffset2: Int = 20
    const val endOffset2: Int = 30

    val annotatorFileNameList = mutableListOf<String>()
    val annotatorRuleModelList = mutableListOf<AnnotatorRuleModel>()

    fun getAnnotatorRuleModelsByFileName(fileName: String): List<RuleModel> {
        return annotatorRuleModelList.find { it.fileName == fileName }?.ruleList ?: emptyList()
    }
}