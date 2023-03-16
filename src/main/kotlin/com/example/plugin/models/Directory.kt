package com.example.plugin.models

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

data class Directory(
    private val project: Project,
    private val psiDirectory: PsiDirectory
) {
    fun findSubdirectory(name: String) = psiDirectory.findSubdirectory(name)?.let { Directory(project, it) }

    fun getFilesText(): List<PsiFile> {
        val arrayAllFileProject = psiDirectory.files

//        arrayAllFileProject.map { println(it.name) }
//        val arrayAllFileTestProject = arrayAllFileProject.map { it.text }
        return arrayAllFileProject.toList()
    }
}