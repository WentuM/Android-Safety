package com.example.plugin

import com.example.plugin.models.Directory
import com.example.plugin.models.Module
import com.example.plugin.toolwindow.MyToolWindowFactory
import com.intellij.codeInsight.highlighting.HighlightManager
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.inspections.findExistingEditor
import java.awt.Color
import java.awt.Font
import java.util.*
import java.util.stream.Collectors


const val LAYOUT_DIRECTORY = "layout"

class EditTextConfidentType(
    private val project: Project
) {

    fun show() {
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
                firstTestXml(it)
            }
        }
    }

    private fun findResourcesSubdirectory(project: Project, module: Module): Directory? {
        val sourceRootRepository = SourceRootRepository(project)
        return sourceRootRepository.findResourcesSourceRoot(module)?.directory?.run {
            findSubdirectory(LAYOUT_DIRECTORY)
        }
    }

    private fun firstTestXml(layoutFile: PsiFile) {
        val textLayoutFile = layoutFile.text
        val editText = "EditText"
        val passwordText = "passwordEditText"
        var index = 0

        //получение ссылки на файл с первой строкой
//        println(layoutFile.originalFile.virtualFile.path + ":1")

        while (index < textLayoutFile.length) {
            index = textLayoutFile.indexOf(editText, index)
            if (index != -1) {
                index += editText.length
//                val currentBeginString = textLayoutFile.indexOf(index)
                val currentLastIndex = textLayoutFile.indexOf(">", index)
                val currentString = textLayoutFile.substring(index, currentLastIndex)

                val passwordIndex = currentString.indexOf(passwordText)
                if (passwordIndex != -1) {
                    val editor = layoutFile.findExistingEditor()
                    editor?.markupModel?.addRangeHighlighter(
                        index + passwordIndex,
                        index + passwordIndex + passwordText.length,
                        9999,
                        TextAttributes(null, Color.red, null, null, Font.PLAIN),
                        HighlighterTargetArea.EXACT_RANGE
                    )

                    println(index)
                    println(passwordIndex)
                    createConsole(layoutFile, index + passwordIndex)
//                    editor?.markupModel?.document?.createRangeMarker(
//                        index + passwordIndex,
//                        index + passwordIndex + passwordText.length,
//                    )
//                    editor?.markupModel?.addLineHighlighter(8, 9999, TextAttributes(null, Color.red, null, null, Font.PLAIN),)
                    val inputTypeConfident = "android:inputType=\"textNoSuggestions\""
                    val inputTypeConfidentIndex = currentString.indexOf(inputTypeConfident)

                    if (inputTypeConfidentIndex != -1) {
                        println("Индекс вхождения инпут тайпа " + inputTypeConfidentIndex)
                    } else {
                        resolveFirstTextXml(layoutFile, index, currentLastIndex)
                        println("Индекс вхождения инпут тайпа отсутствует. Необходимо добавить инпут тайп.")
                    }
                }
                index = currentLastIndex
            } else {
                break
            }
//            println("Индекс вхождения по айди эдит текста" + firstIndex)
        }
    }

    private fun resolveFirstTextXml(layoutFile: PsiFile, index: Int, currentLastIndex: Int) {
//        layoutFile.viewProvider.document?.setText("lolololo")
        val rangeMarker: RangeMarker? = layoutFile.viewProvider.document?.createRangeMarker(0, 10)
        val lighter = HighlightManager.getInstance(project)


//        val factory = TextConsoleBuilderFactory.getInstance()
//        val builder = factory.createBuilder(project)
//        val view: ConsoleView = builder.console
//        view.print("дщдщдщдщ", ConsoleViewContentType.NORMAL_OUTPUT)
//        console = view.getComponent()
//        lighter.addRangeHighlight(layoutFile.findExistingEditor(), 0, 10)
//        val editor = e.getData(CommonDataKeys.EDITOR)!!
//        val document = editor.document
//        val editor = layoutFile.findExistingEditor()!!
//        val document = layoutFile.viewProvider.document
//        val startOffset: Int = document.getLineStartOffset(4)
//        val endOffset: Int = document.getLineEndOffset(7) // assuming open line range [4-8)
//        val highlighter: RangeHighlighter = editor.markupModel
//            .addRangeHighlighter(startOffset, endOffset, 0, null, HighlighterTargetArea.EXACT_RANGE)
//            .addLineHighlighter(startOffset, endOffset, null)
//        highlighter.errorStripeMarkColor = JBColor.MAGENTA


        //добавлять после анализа всего файла или предусмотреть, чтобы добавляется надпись и надо увеличить индекс
        layoutFile.viewProvider.document?.insertString(index, "\nandroid:inputType=\"textNoSuggestions\"")
    }

    private fun createConsole(layoutFile: PsiFile, passwordIndex: Int) {
        // 1) сделать один вызов метода, который заполняет контент, при перезапуске
        // уже предусмотрено, что открывается новый диалог
        // 2) фокус и сам билд открываются, если билд активен - сделать активным билд в первый запуск)
        // 3) как в документации сделать фильтр на редактируемый файл, только подсвечивать буду с помощью плагина,
        // а точнее по названию файла и индексам, где нужно подсветить?
        //

        val mainToolWindow = ToolWindowManager.getInstance(project)
            .registerToolWindow("Android Safety", true, ToolWindowAnchor.BOTTOM, project, true)

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Android Safety")

        toolWindow?.show()
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content =
            toolWindow?.contentManager?.factory?.createContent(consoleView.component, "Android Safety Output", false)
        if (content != null) {

            content.setDisposer(Disposable { mainToolWindow.remove() })

            val currentContent = toolWindow.contentManager.findContent("MyPlugin Output")
            println(toolWindow.contentManager.findContent("MyPlugin Output"))
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
        consoleView.print(
            layoutFile.originalFile.virtualFile.path + ":" + layoutFile.viewProvider.document.getLineNumber(
                passwordIndex
            ) + ": Something", ConsoleViewContentType.NORMAL_OUTPUT
        )



//        mainToolWindow.remove()
    }

    private fun testConsole(layoutFile: PsiFile, passwordIndex: Int) {
//        val console = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
//        console.attachToProcess()
    }
}