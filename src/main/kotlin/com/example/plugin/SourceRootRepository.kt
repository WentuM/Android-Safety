package com.example.plugin

import com.example.plugin.models.Module
import com.example.plugin.models.SourceRoot
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.util.sourceRoots

const val DEFAULT_SOURCE_SET = "main"

class SourceRootRepository(
    private val project: Project
) {

    private fun getProjectPath() = project.basePath ?: ""

    private fun findSourceRoots(module: Module): List<SourceRoot> =
        ModuleManager.getInstance(project)
            .findModuleByName(module.name)
            ?.sourceRoots
            ?.map { SourceRoot(project, it) }
            ?: throw IllegalStateException("${module.name} module doesn't exist!")

    fun findCodeSourceRoot(module: Module, sourceSet: String = DEFAULT_SOURCE_SET) =
        findSourceRoots(module).firstOrNull {
            val pathTrimmed = it.virtualFile.path.removeModulePathPrefix(module)
            pathTrimmed.contains("src", true) &&
                    pathTrimmed.contains(sourceSet) &&
                    !pathTrimmed.contains("assets", true) &&
                    !pathTrimmed.contains("res", true)
        }

    fun findResourcesSourceRoot(module: Module) =
        findSourceRoots(module).firstOrNull {
            val pathTrimmed = it.virtualFile.path.removeModulePathPrefix(module)
            pathTrimmed.contains("src", true) &&
                    pathTrimmed.contains("main", true) &&
                    pathTrimmed.contains("res", true)
        }

    private fun String.removeModulePathPrefix(module: Module) =
        removePrefix(getProjectPath() + "/" + module.nameWithoutPrefix.replace(".", "/"))
}
