import com.github.gradle.node.npm.task.NpmTask

group = "com.github.blanexie.magic.web"
version = "0.0.1"

plugins {
    id("com.github.node-gradle.node") version "7.0.2"
}

val buildTask = tasks.register<NpmTask>("buildWebApp") {
    args.set(listOf("run", "build", "--prod"))
    dependsOn(tasks.npmInstall)
}

