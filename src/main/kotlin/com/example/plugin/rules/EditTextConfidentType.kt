package com.example.plugin.rules

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.annotator.RuleModel
import com.example.plugin.models.Directory
import com.example.plugin.models.Module
import com.example.plugin.repository.SourceRootRepository
import com.intellij.codeInsight.highlighting.HighlightManager
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.*
import java.util.*
import java.util.stream.Collectors


const val LAYOUT_DIRECTORY = "layout"

class EditTextConfidentType(
    private val project: Project,
    private val consoleView: ConsoleView,
    private val isNeedCheck: Boolean,
    private val isNeedFix: Boolean
) : RuleRealization {

    override fun analyze(psiFile: PsiFile, annotatorRuleModel: AnnotatorRuleModel) {
        val projectName: String = project.name
        val vFiles: Array<VirtualFile> = ProjectRootManager.getInstance(project).contentRootsFromAllModules
        val sourceRootsList: String = Arrays.stream(vFiles).map(VirtualFile::getUrl).collect(Collectors.joining("\n"))
//        println("----------->$sourceRootsList")
        //
//        println(projectStructure.getAllModules())
//        val project = projectStructure.getProject()
        val allModules = ModuleManager.getInstance(project).modules
        for (module in allModules) {

            val resourcesSubdirectory = findResourcesSubdirectory(
                project,
                Module(
                    module.name,
                    nameWithoutPrefix = module.name.replace("${projectName}.", "")
                )
            )
            val listFileTextLayout = resourcesSubdirectory?.getFilesText()
            listFileTextLayout?.forEach {
                firstTestXml(it)
            }

            findCodeSubdirectory(
                Module(
                    module.name,
                    nameWithoutPrefix = module.name.replace("${projectName}.", "")
                )
            )
        }
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

            getSubDirectories(psiDirectory)

        }
    }

    private fun firstTestXml(layoutFile: PsiFile) {
        val textLayoutFile = layoutFile.text
        val editText = "EditText"
        val passwordText = "passwordEditText"
        var index = 0
        val annotatorRuleModel = AnnotatorRuleModel(layoutFile.name, mutableListOf(), layoutFile.text.hashCode())


        while (index < textLayoutFile.length) {
            index = textLayoutFile.indexOf(editText, index)
            if (index != -1) {
                index += editText.length
                val currentLastIndex = textLayoutFile.indexOf(">", index)
                val currentString = textLayoutFile.substring(index, currentLastIndex)

                val passwordIndex = currentString.indexOf(passwordText)
                if (passwordIndex != -1) {
                    val inputTypeConfident = "android:inputType=\"textNoSuggestions\""
                    val inputTypeConfidentIndex = currentString.indexOf(inputTypeConfident)

                    if (inputTypeConfidentIndex != -1) {
                        println("Индекс вхождения инпут тайпа " + inputTypeConfidentIndex)
                    } else {
                        annotatorRuleModel.ruleList.add(
                            RuleModel(
                                index + passwordIndex,
                                index + passwordIndex + passwordText.length,
                                "Where textNoSuggestions?"
                            )
                        )

                        printConsoleView(layoutFile, index + passwordIndex)

                        println("Индекс вхождения инпут тайпа отсутствует. Необходимо добавить инпут тайп.")
                    }
                }
                index = currentLastIndex
            } else {
                break
            }
        }
        if (annotatorRuleModel.ruleList.isNotEmpty()) {
            AnnotatorRepository.annotatorRuleModelList.add(annotatorRuleModel)

            layoutFile.viewProvider.document.addDocumentListener(object : DocumentListener {
                override fun documentChanged(event: DocumentEvent) {
                    super.documentChanged(event)
                    println("moveOffset3 = ${event.moveOffset}")
                    println("oldLength = ${event.oldLength}")
                    println("newLength = ${event.newLength}")
                }
            })
        }
    }

    private fun resolveFirstTextXml(layoutFile: PsiFile, index: Int, currentLastIndex: Int) {
        layoutFile.viewProvider.document?.insertString(index, "\nandroid:inputType=\"textNoSuggestions\"")
    }

    private fun printConsoleView(layoutFile: PsiFile, passwordIndex: Int) {
        consoleView.print(
            layoutFile.originalFile.virtualFile.path + ":" + layoutFile.viewProvider.document.getLineNumber(
                passwordIndex
            ) + ": Something", ConsoleViewContentType.NORMAL_OUTPUT
        )
    }

    private fun getSubDirectories(psiDirectory: PsiDirectory) {
        println("directory: " + psiDirectory.name)
        psiDirectory.files.forEach {
            println("file: " + it.name)
        }
        psiDirectory.subdirectories.forEach {
            getSubDirectories(it)
        }
    }
}