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
    maven {
        setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
        setUrl("https://mirrors.163.com/maven/repository/maven-public/")
        setUrl("https://repo.huaweicloud.com/repository/maven/")
        setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
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

java.sourceCompatibility = JavaVersion.VERSION_21

tasks.register<Task>("buildApp") {
    description = "前后端一起打包的Task"
    group = JavaBasePlugin.BUILD_TASK_NAME

    dependsOn("buildFatjar")
    doLast {
        val destinationDir = rootProject.file("/build/")
        val from = rootProject.file("platform/build/libs/")
        moveFolderContents(from, destinationDir)

    }
}

tasks.register<Task>("buildFatjar") {
    description = "转移前端打包好的资源到fatjar对应的目录中"
    group = JavaBasePlugin.BUILD_NEEDED_TASK_NAME
    dependsOn("copyWebDist")

    dependsOn(":platform:assemble")

}

tasks.register<Task>("copyWebDist") {
    description = "转移前端打包好的资源到fatjar对应的目录中"
    group = JavaBasePlugin.BUILD_NEEDED_TASK_NAME

    dependsOn(":web:buildWebApp")
    doLast {
        val destinationDir = rootProject.file("platform/build/resources/main/static")
        val from = rootProject.file("web/dist")
        moveFolderContents(from, destinationDir)
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