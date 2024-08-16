import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask
import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList
import org.jetbrains.kotlin.gradle.targets.js.npm.buildNpmVersion

group = "com.github.blanexie.magic.web"
version = "0.0.1"

plugins {
    java
    id("com.github.node-gradle.node") version "7.0.2"
}




val buildTask = tasks.register<NpmTask>("buildWebApp") {
    args.set(listOf("run","build", "--prod"))
    dependsOn(tasks.npmInstall)
    inputs.dir(fileTree("src")
            .exclude("**/*.test.js")
            .exclude("**/*.spec.js")
            .exclude("**/__tests__/**/*.js"))

    inputs.dir("node_modules")
    inputs.dir("public")
    outputs.dir("$project/${buildDir}/webapp")

}


sourceSets {
    java {
        main {
            resources {
                srcDir(buildTask)
            }
        }
    }
}