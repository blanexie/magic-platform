import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("kotlin")
    id("idea")
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.jetbrains.kotlin.plugin.noarg")
}

group = "com.github.blanexie.magic.platform"
version = "0.0.1-SNAPSHOT"


java.sourceCompatibility = JavaVersion.VERSION_21

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("org.springframework.boot:spring-boot-starter-web")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("us.codecraft:webmagic-core")
    implementation("us.codecraft:webmagic-extension")
    implementation("us.codecraft:webmagic-saxon")

    implementation("cn.hutool:hutool-all")

    implementation("org.xerial:sqlite-jdbc")
    //implementation("com.baomidou:mybatis-plus-boot-starter")
    //implementation("org.mybatis:mybatis-typehandlers-jsr310")
    implementation("com.alibaba:fastjson")
}

dependencyManagement {
    dependencies {
        dependency("us.codecraft:webmagic-core:1.0.0")
        dependency("us.codecraft:webmagic-extension:1.0.0")
        dependency("us.codecraft:webmagic-saxon:1.0.0")
        dependency("cn.hutool:hutool-all:5.8.31")

        //模块需要其他第三方库, 在这里写
        dependency("org.xerial:sqlite-jdbc:3.21.0.1")
        dependency("com.baomidou:mybatis-plus-boot-starter:3.5.0")
        dependency("org.mybatis:mybatis-typehandlers-jsr310:1.0.2")
        dependency("com.alibaba:fastjson:2.0.23")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

