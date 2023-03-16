package com.example.plugin.models

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

data class SourceRoot(
    val project: Project,
    val virtualFile: VirtualFile
) {
    val directory: Directory = Directory(project, PsiManager.getInstance(project).findDirectory(virtualFile)!!)
}