package com.example.plugin

import com.example.plugin.annotator.AnnotatorRepository
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class HideRecommendationsAnAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        AnnotatorRepository.annotatorRuleModelList.clear()
    }
}