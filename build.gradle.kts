import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.20"
}

repositories {
    mavenLocal()
    maven {
        setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
    }
    mavenCentral()
}

//注意以下内容
subprojects {
    repositories {
        mavenLocal()
        maven {
            setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
        }
        mavenCentral()
    }

}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.register<Task>("buildApp") {
    description = "前后端一起打包的Task"
    group = JavaBasePlugin.BUILD_TASK_NAME
}

tasks.register<Task>("copy2") {
    description = "转移前端打包好的资源到fatjar对应的目录中"
    group = JavaBasePlugin.BUILD_NEEDED_TASK_NAME

    val destinationDir = rootProject.buildDir
    val from = rootProject.file("platform/build/libs/")
    moveFolderContents(from, destinationDir)
}

tasks.register<Task>("copy") {
    description = "转移前端打包好的资源到fatjar对应的目录中"
    group = JavaBasePlugin.BUILD_NEEDED_TASK_NAME
    dependsOn(":web:buildWebApp")
    val destinationDir = rootProject.file("platform/build/resources/main/static")
    val from = rootProject.file("web/dist")
    moveFolderContents(from, destinationDir)
}

fun moveFolderContents(source: File, destination: File) {
    source.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            println("copying dir $file to $destination" )
            val newDestination = File(destination, file.name)
            newDestination.mkdirs()
            moveFolderContents(file, newDestination)
        } else {
            val destinationFile = File(destination, file.name)
            println("copying file $file to $destinationFile" )
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }
}