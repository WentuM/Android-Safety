package com.example.plugin

import com.example.plugin.models.Directory
import com.example.plugin.models.Module
import com.example.plugin.repository.SourceRootRepository
import com.example.plugin.rules.GetCanonicalPathType
import com.example.plugin.rules.LAYOUT_DIRECTORY
import com.example.plugin.rules.WebViewAllowFileAccess
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import java.util.*
import java.util.stream.Collectors

class AndroidSafetyCheck(
    val project: Project
) {

    private lateinit var consoleView: ConsoleView

    companion object {
        private const val BEGIN_CONSOLE_MESSAGE = "Start of Android Safety Analysis...\n"
        private const val END_CONSOLE_MESSAGE = "\n\nEnd of Android Safety Analysis"
    }

    init {
        createConsole()
        printConsoleView(BEGIN_CONSOLE_MESSAGE)
        getAllFiles()
        printConsoleView(END_CONSOLE_MESSAGE)

//        EditTextConfidentType(project, consoleView).show()
    }

    private fun getAllFiles() {
        val projectName: String = project.name
        val vFiles: Array<VirtualFile> = ProjectRootManager.getInstance(project).contentRootsFromAllModules
        val sourceRootsList: String = Arrays.stream(vFiles).map(VirtualFile::getUrl).collect(Collectors.joining("\n"))
//        println("----------->$sourceRootsList")
        //
//        println(projectStructure.getAllModules())
//        val project = projectStructure.getProject()
        val allModules = ModuleManager.getInstance(project).modules
        for (module in allModules) {
//            val path = module
//            val sourceRootList = ModuleManager.getInstance(project)
//                .findModuleByName(module.name)
//                ?.sourceRoots
//                ?.map { SourceRoot(project, it) }
//                ?: throw IllegalStateException("${module.name} module doesn't exist!")

            val resourcesSubdirectory = findResourcesSubdirectory(
                project,
                Module(
                    module.name,
                    nameWithoutPrefix = module.name.replace("${projectName}.", "")
                )
            )
//            println(resourcesSubdirectory.toString())
            val listFileTextLayout = resourcesSubdirectory?.getFilesText()
            listFileTextLayout?.forEach {
//                firstTestXml(it)
            }

            findCodeSubdirectory(
                Module(
                    module.name,
                    nameWithoutPrefix = module.name.replace("${projectName}.", "")
                )
            )
//            println("spisok - " + listFileCodeTextLayout.toString())
//            listFileCodeTextLayout?.forEach {
//                val a = it.toUElement(UMethod::class.java) as UMethod
//                println("ватофак - " + a.uastBody.toString())
//            }
        }
    }

    private fun createConsole() {
        // 1) сделать один вызов метода, который заполняет контент, при перезапуске
        // уже предусмотрено, что открывается новый диалог
        // 2) фокус и сам билд открываются, если билд активен - сделать активным билд в первый запуск)
        // 3) как в документации сделать фильтр на редактируемый файл, только подсвечивать буду с помощью плагина,
        // а точнее по названию файла и индексам, где нужно подсветить?
        //

        //перезапуск
        val currentToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Android Safety")
        currentToolWindow?.remove()

        val mainToolWindow = ToolWindowManager.getInstance(project)
            .registerToolWindow("Android Safety", true, ToolWindowAnchor.BOTTOM, project, true)

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Android Safety")

        toolWindow?.show()
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content =
            toolWindow?.contentManager?.factory?.createContent(consoleView.component, "Android Safety Output", false)
        if (content != null) {

            content.setDisposer(Disposable { mainToolWindow.remove() })

            val currentContent = toolWindow.contentManager.findContent("Android Safety Output")
            println(toolWindow.contentManager.findContent("Android Safety Output"))
            if (currentContent != null) {
                println("yes")
                println(toolWindow.contentManager.removeContent(currentContent, true))
                toolWindow.contentManager.addContent(content)
                toolWindow.contentManager.setSelectedContent(content)
            } else {
                println("no")
                toolWindow.contentManager.addContent(content)
                toolWindow.contentManager.setSelectedContent(content)
            }
        }
//        mainToolWindow.remove()
    }

    private fun findResourcesSubdirectory(project: Project, module: Module): Directory? {
        val sourceRootRepository = SourceRootRepository(project)
        return sourceRootRepository.findResourcesSourceRoot(module)?.directory?.run {
            findSubdirectory(LAYOUT_DIRECTORY)
        }
    }

    private fun findCodeSubdirectory(module: Module) {
        val sourceRootRepository = SourceRootRepository(project)
        sourceRootRepository.findCodeSourceRoot(module)?.directory?.run {
            //рекурсивно пройти по всем файлам всех директорий
//            println("subdirectories:  " + psiDirectory.subdirectories.get(0).name.toString())


            getSubDirectories(psiDirectory)


//            psiDirectory.subdirectories.forEach {
//                println("begin: ")
//                println(it.name)
//                println("end: ")
//            }
            getFilesText().forEach {
//                println("text - " + it.text)
                it.text
//                println("methoooods   "+ it.getContainingClass()?.findMethodsByName("onCreate", false)?.size)

                //правильный хэшкод при изменении файлов
//                println("hashcode " + it.text.hashCode())
                it.text.hashCode()
//                val a = it.toUElement(UCallExpression::class.java) as UMethod
//                println("ватофак - " + a.uastBody.toString())
            }
//            psiDirectory.subdirectories.forEach {
//                println("lolo"+ Directory(project, it).getFilesText())
//            }
        }
    }

    private fun getSubDirectories(psiDirectory: PsiDirectory) {
        psiDirectory.files.forEach {
            println("file: " + it.name)
            GetCanonicalPathType(project, it, consoleView).show()
            WebViewAllowFileAccess(project, it, consoleView).show()
        }
        psiDirectory.subdirectories.forEach {
            getSubDirectories(it)
        }
    }

    private fun printConsoleView(message: String) {
        consoleView.print(message, ConsoleViewContentType.NORMAL_OUTPUT)
    }
}