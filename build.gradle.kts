import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "2.0.10"
}

repositories {
    mavenLocal()
    mavenCentral()
}

//注意以下内容
subprojects {
    apply(plugin = "idea")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin ="org.jetbrains.kotlin.plugin.noarg")

    repositories {
        mavenLocal()
        mavenCentral()
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    dependencyManagement {
        dependencies {
            dependency("us.codecraft:webmagic-core:1.0.0")
            dependency("us.codecraft:webmagic-extension:1.0.0")
            dependency("us.codecraft:webmagic-saxon:1.0.0")
            dependency("cn.hutool:hutool-all:5.8.31")
            dependency("org.projectlombok:lombok:1.18.34")
            dependency("org.hibernate.orm:hibernate-community-dialects:6.6.0.Final")
            dependency("org.xerial:sqlite-jdbc:3.46.1.0")
            dependency("com.alibaba:fastjson:2.0.23")
            dependency("org.hibernate.orm:hibernate-core:6.6.0.Final")

        }
    }
}


fun moveFolderContents(source: File, destination: File) {
    if (destination.isDirectory && !destination.exists()) {
        destination.mkdirs()
    }
    destination.mkdirs()
    source.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            println("copying dir $file to $destination")
            val newDestination = File(destination, file.name)
            newDestination.mkdirs()
            moveFolderContents(file, newDestination)
        } else {
            val destinationFile = File(destination, file.name)
            println("copying file $file to $destinationFile")
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }
}