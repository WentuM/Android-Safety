package com.example.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.intellij.openapi.command.WriteCommandAction
import org.gradle.api.DomainObjectSet
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Project.android(): BaseExtension {
    val android = project.extensions.findByType(BaseExtension::class.java)
    if (android != null) {
        return android
    } else {
        throw GradleException("Project $name is not an Android project")
    }
}

fun BaseExtension.variants(): DomainObjectSet<out BaseVariant> {
    return when (this) {
        is AppExtension -> {
            applicationVariants
        }

        is LibraryExtension -> {
            libraryVariants
        }

        else -> throw GradleException("Unsupported BaseExtension type!")
    }
}

class MyPlugin : Plugin<Project> {

    override fun apply(project: Project) {
//        WriteCommandAction.runWriteCommandAction(project, COMMAND_NAME, GROUP_ID, {
//            EditTextConfidentType(project).show()
//        })
        project.tasks.create("printSomething") {
            println("Hello world")
        }
//        project.android().variants().all { variant ->
//
//            // Make a task for each combination of build type and product flavor
//            val myTask = "myFirstTask${variant.name.capitalize()}"
//
//
//            // Register a simple task as a lambda. We'll later move it its own class and add some niceties
//            project.tasks.create(myTask) {
//
//                // Group all our plugin's tasks together
//                group = "MyPluginTasks"
//                doLast {
//                    File("${project.projectDir.path}/myFirstGeneratedFile.txt").apply {
//                        writeText("Hello Gradle!\nPrinted at: ${SimpleDateFormat("HH:mm:ss").format(Date())}")
//                    }
//                }
//            }
//            true
//        }
    }
}