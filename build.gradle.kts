plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.intellij") version "1.5.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("kapt") version "1.6.10"
    `java-gradle-plugin`
    `kotlin-dsl`
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { setUrl("https://plugins.gradle.org/m2/") }
    maven {
        url = uri("https://jitpack.io")
    }
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.google.dagger:dagger:2.41")
    kapt("com.google.dagger:dagger-compiler:2.41")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.amshove.kluent:kluent:1.68")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("app.cash.turbine:turbine:0.7.0")

    implementation("com.github.shibing624:similarity:1.1.6")

//    implementation("com.github.cretz.kastree:kastree-ast-jvm:0.4.0")
//    implementation("com.github.cretz.kastree:kastree-ast-psi:0.4.0")

//    implementation("com.github.kotlinx.ast:grammar-kotlin-parser-antlr-kotlin:0123456789")

    implementation("com.android.tools.build:gradle:3.5.3")
    implementation("org.apache.commons:commons-lang3:3.12.0")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.2")
    type.set("IC") // Target IDE Platform
    plugins.set(listOf("org.jetbrains.kotlin", "java"))
}

gradlePlugin {
    plugins {
        create("MyPlugin") {
            id = "MyPlugin"
            implementationClass = "com.example.plugin.MyPlugin"
        }
    }
}

//gradlePlugin {
//    plugins {
//        register("first-plugin") {
//            description = "My first plugin"
//            displayName = "Does nothing"
//            id = "com.example.plugin.my-plugin"
//            implementationClass = "com.example.plugin.MyPlugin"
//        }
//    }
//}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    runIde {
        ideDir.set(file("/Applications/Android Studio.app/Contents"))
    }

    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
