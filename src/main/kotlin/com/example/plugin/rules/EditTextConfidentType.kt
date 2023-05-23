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
    private val consoleView: ConsoleView
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

            findCodeSubdirectory(Module(
                module.name,
                nameWithoutPrefix = module.name.replace("${projectName}.", "")
            ))
//            println("spisok - " + listFileCodeTextLayout.toString())
//            listFileCodeTextLayout?.forEach {
//                val a = it.toUElement(UMethod::class.java) as UMethod
//                println("ватофак - " + a.uastBody.toString())
//            }
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
            //рекурсивно пройти по всем файлам всех директорий
            println("subdirectories:  "+ psiDirectory.subdirectories.get(0).name.toString())


            getSubDirectories(psiDirectory)


//            psiDirectory.subdirectories.forEach {
//                println("begin: ")
//                println(it.name)
//                println("end: ")
//            }
            getFilesText().forEach {
                println("text - " + it.text)
//                println("methoooods   "+ it.getContainingClass()?.findMethodsByName("onCreate", false)?.size)

                //правильный хэшкод при изменении файлов
                println("hashcode " + it.text.hashCode())
//                val a = it.toUElement(UCallExpression::class.java) as UMethod
//                println("ватофак - " + a.uastBody.toString())
            }
//            psiDirectory.subdirectories.forEach {
//                println("lolo"+ Directory(project, it).getFilesText())
//            }
        }
    }

//    private fun getPublishMethods(): Array<PsiMethod> {
//        val javaPsiFacade = JavaPsiFacade.getInstance(project)
//        val publisherClass = javaPsiFacade.findClass(EVENT_PUBLISHER, scope) ?: return emptyArray()
//
//        return publisherClass.findMethodsByName(PUBLISH_EVENT_METHOD, false)
//    }

    private fun firstTestXml(layoutFile: PsiFile) {
        val textLayoutFile = layoutFile.text
        val editText = "EditText"
        val passwordText = "passwordEditText"
        var index = 0
        val annotatorRuleModel = AnnotatorRuleModel(layoutFile.name, mutableListOf(), layoutFile.text.hashCode())

        //получение ссылки на файл с первой строкой
//        println(layoutFile.originalFile.virtualFile.path + ":1")

        while (index < textLayoutFile.length) {
            index = textLayoutFile.indexOf(editText, index)
            if (index != -1) {
                index += editText.length
//                val currentBeginString = textLayoutFile.indexOf(index)
                val currentLastIndex = textLayoutFile.indexOf(">", index)
                val currentString = textLayoutFile.substring(index, currentLastIndex)

                //indexOf id="@
                val passwordIndex = currentString.indexOf(passwordText)
                if (passwordIndex != -1) {
//                    val editor = layoutFile.findExistingEditor()
//                    editor?.markupModel?.addRangeHighlighter(
//                        index + passwordIndex,
//                        index + passwordIndex + passwordText.length,
//                        9999,
//                        TextAttributes(null, Color.red, null, null, Font.PLAIN),
//                        HighlighterTargetArea.EXACT_RANGE
//                    )
//
//                    println(index)
//                    println(passwordIndex)
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
                        annotatorRuleModel.ruleList.add(
                            RuleModel(
                                index + passwordIndex,
                                index + passwordIndex + passwordText.length,
                                "Where textNoSuggestions?"
                            )
                        )

                        printConsoleView(layoutFile, index + passwordIndex)

//                        resolveFirstTextXml(layoutFile, index, currentLastIndex)
                        println("Индекс вхождения инпут тайпа отсутствует. Необходимо добавить инпут тайп.")
                    }
                }
                index = currentLastIndex
            } else {
                break
            }
//            println("Индекс вхождения по айди эдит текста" + firstIndex)
        }
        //вносить сразу, а не после окончания файла
        if (annotatorRuleModel.ruleList.isNotEmpty()) {
            AnnotatorRepository.annotatorFileNameList.add(annotatorRuleModel.fileName)
            AnnotatorRepository.annotatorRuleModelList.add(annotatorRuleModel)

            layoutFile.viewProvider.document.addDocumentListener(object: DocumentListener {
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
//        layoutFile.viewProvider.document?.setText("lolololo")
//        val rangeMarker: RangeMarker? = layoutFile.viewProvider.document?.createRangeMarker(0, 10)
//        val lighter = HighlightManager.getInstance(project)


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

    private fun printConsoleView(layoutFile: PsiFile, passwordIndex: Int) {
        consoleView.print(
            layoutFile.originalFile.virtualFile.path + ":" + layoutFile.viewProvider.document.getLineNumber(
                passwordIndex
            ) + ": Something", ConsoleViewContentType.NORMAL_OUTPUT
        )
    }

    private fun testConsole(layoutFile: PsiFile, passwordIndex: Int) {
//        val console = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
//        console.attachToProcess()
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