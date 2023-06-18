package com.example.plugin

import com.example.plugin.annotator.AnnotatorRepository
import com.example.plugin.annotator.AnnotatorRuleModel
import com.example.plugin.data.ScreenGeneratorComponent
import com.example.plugin.models.Directory
import com.example.plugin.models.Module
import com.example.plugin.repository.SourceRootRepository
import com.example.plugin.rules.*
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

class AndroidSafetyService(
    val project: Project,
    private val isNeedFix: Boolean
) {

    private lateinit var consoleView: ConsoleView
    private val screenGeneratorComponent = ScreenGeneratorComponent.getInstance(project)
    private var customRuleModels: List<RuleRealization>

    companion object {
        private const val BEGIN_CONSOLE_MESSAGE = "Start of Android Safety Analysis...\n"
        private const val END_CONSOLE_MESSAGE = "\n\nEnd of Android Safety Analysis"
    }

    private var webViewAllowFileAccess: WebViewAllowFileAccess
    private var webViewAssetLoader: WebViewAssetLoader
    private var webViewNeedListing: WebViewNeedListing
    private var editTextAutoFill: EditTextAutoFill
    private var editTextConfidentType: EditTextConfidentType
    private var getCanonicalPathType: GetCanonicalPathType
    private var broadCastReceiverPermission: BroadCastReceiverPermission
    private var storageInternalMemory: StorageInternalMemory
    private var checkHostName: CheckHostName
    private var contentFile: ContentFile
    private var httpProtocol: HttpProtocol
    private var pendingIntent: PendingIntent
    private var permissions: Permissions
    private var securityConfig: SecurityConfig
    private var validatedSertificates: ValidatedSertificates
    private var webViewAllowUniversal: WebViewAllowUniversal
    private var webViewExpectedUrls: WebViewExpectedUrls
    private var webViewSafety: WebViewSafety
    private var testRule: TestRule

    private var kotlinRules: MutableList<RuleRealization> = mutableListOf()

    init {
        AnnotatorRepository.annotatorRuleModelList.clear()
        createConsole()
        printConsoleView(BEGIN_CONSOLE_MESSAGE)
        val ruleListForCheck = screenGeneratorComponent.settings.ruleList

        customRuleModels = screenGeneratorComponent.customRuleModels.map { customRuleModel ->
            val isSelected = ruleListForCheck.find { it.id == customRuleModel.id }?.isSelected
            CustomRuleRealization(customRuleModel, consoleView, isSelected == true, isNeedFix)
        }

        webViewAllowFileAccess = WebViewAllowFileAccess(project, consoleView, ruleListForCheck[0].isSelected, isNeedFix)
        getCanonicalPathType = GetCanonicalPathType(project, consoleView, ruleListForCheck[1].isSelected, isNeedFix)
        webViewAssetLoader = WebViewAssetLoader(project, consoleView, ruleListForCheck[2].isSelected, isNeedFix)
        webViewNeedListing = WebViewNeedListing(project, consoleView, ruleListForCheck[3].isSelected, isNeedFix)
        editTextAutoFill = EditTextAutoFill(project, consoleView, ruleListForCheck[4].isSelected, isNeedFix)
        editTextConfidentType = EditTextConfidentType(project, consoleView, ruleListForCheck[5].isSelected, isNeedFix)
        checkHostName = CheckHostName(project, consoleView, ruleListForCheck[6].isSelected, isNeedFix)
        contentFile = ContentFile(project, consoleView, ruleListForCheck[7].isSelected, isNeedFix)
        httpProtocol = HttpProtocol(project, consoleView, ruleListForCheck[8].isSelected, isNeedFix)
        pendingIntent = PendingIntent(project, consoleView, ruleListForCheck[9].isSelected, isNeedFix)
        permissions = Permissions(project, consoleView, ruleListForCheck[10].isSelected, isNeedFix)
        securityConfig = SecurityConfig(project, consoleView, ruleListForCheck[11].isSelected, isNeedFix)
        var secConfig = SecurityConfig(project, consoleView, ruleListForCheck[18].isSelected, isNeedFix)
        validatedSertificates = ValidatedSertificates(project, consoleView, ruleListForCheck[12].isSelected, isNeedFix)
        webViewAllowUniversal = WebViewAllowUniversal(project, consoleView, ruleListForCheck[13].isSelected, isNeedFix)
        webViewExpectedUrls = WebViewExpectedUrls(project, consoleView, ruleListForCheck[16].isSelected, isNeedFix)
        webViewSafety = WebViewSafety(project, consoleView, ruleListForCheck[15].isSelected, isNeedFix)
        var safety = WebViewSafety(project, consoleView, ruleListForCheck[19].isSelected, isNeedFix)
        broadCastReceiverPermission =
            BroadCastReceiverPermission(project, consoleView, ruleListForCheck[20].isSelected, isNeedFix)
        storageInternalMemory = StorageInternalMemory(project, consoleView, ruleListForCheck[14].isSelected, isNeedFix)
        testRule = TestRule(project, consoleView, ruleListForCheck[17].isSelected, isNeedFix)
        kotlinRules.apply {
            add(webViewAllowFileAccess)
            add(broadCastReceiverPermission)
            add(getCanonicalPathType)
            add(storageInternalMemory)
            add(webViewAssetLoader)
            add(webViewNeedListing)
            add(editTextAutoFill)
            add(editTextConfidentType)
            add(checkHostName)
            add(contentFile)
            add(httpProtocol)
            add(pendingIntent)
            add(httpProtocol)
            add(securityConfig)
            add(validatedSertificates)
            add(webViewAllowUniversal)
            add(webViewExpectedUrls)
            add(webViewSafety)
            add(testRule)
        }

        getAllFiles()

        //
        screenGeneratorComponent.settingsRules = AnnotatorRepository.annotatorRuleModelList
        printConsoleView(END_CONSOLE_MESSAGE)

    }

    private fun getAllFiles() {
        val projectName: String = project.name
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
                webViewAllowFileAccess.firstTestXml(it)
            }

            findCodeSubdirectory(
                Module(
                    module.name,
                    nameWithoutPrefix = module.name.replace("${projectName}.", "")
                )
            )
        }
        AnnotatorRepository.annotatorRuleModelList
    }

    private fun createConsole() {
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
                println(toolWindow.contentManager.removeContent(currentContent, true))
                toolWindow.contentManager.addContent(content)
                toolWindow.contentManager.setSelectedContent(content)
            } else {
                toolWindow.contentManager.addContent(content)
                toolWindow.contentManager.setSelectedContent(content)
            }
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

    private fun getSubDirectories(psiDirectory: PsiDirectory) {
        psiDirectory.files.forEach { psiFile ->
            var index = 0
            var currentPsiFileTextLength = psiFile.textLength
            var lastStartOffset = 0
            val hashCode = screenGeneratorComponent.getHashCodeFileByFileName(psiFile.name)

            if (hashCode != psiFile.text.hashCode()) {

                val annotatorRuleModel =
                    AnnotatorRuleModel(psiFile.name, mutableListOf(), psiFile.text.hashCode())
                kotlinRules.forEach { kotlinRule ->
                    kotlinRule.analyze(psiFile, annotatorRuleModel)
                }
                customRuleModels.forEach { customRule ->
                    customRule.analyze(psiFile, annotatorRuleModel)
                }

            } else {

                screenGeneratorComponent.getAnnotatorRuleModelsByFileName(psiFile.name)?.let {
                    AnnotatorRepository.annotatorRuleModelList.add(it)
                    val ruleSet = it.ruleList.toSet()
                    ruleSet.forEach { ruleModel ->
                        if (ruleModel.startOffset > lastStartOffset) {
                            fix(
                                psiFile,
                                ruleModel.startOffset + index,
                                ruleModel.endOffset + index,
                                ruleModel.fixMessage
                            )
                        } else {
                            fix(psiFile, ruleModel.startOffset, ruleModel.endOffset, ruleModel.fixMessage)
                        }
                        lastStartOffset = ruleModel.startOffset
                        index += (psiFile.textLength - currentPsiFileTextLength)
                        currentPsiFileTextLength = psiFile.textLength
                        printConsoleView(ruleModel.consoleMessage)
                    }
                }

            }
        }
        psiDirectory.subdirectories.forEach {
            getSubDirectories(it)
        }
    }

    private fun printConsoleView(message: String) {
        consoleView.print(message, ConsoleViewContentType.NORMAL_OUTPUT)
    }

    private fun fix(psiFile: PsiFile, startOffset: Int, endOffset: Int, fixMessage: String) {
        if (isNeedFix) {
            if (fixMessage.isNotBlank()) {
                psiFile.viewProvider.document?.replaceString(startOffset, endOffset, fixMessage)
            }
        }
    }
}