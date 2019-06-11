import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.11"
}

group = "me.schlaubi.regnumutils"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile ("net.dv8tion:JDA:4.ALPHA.0_103") {
        exclude(module = "opus-java")
    }

    compile("cc.hawkbot.regnum", "client", "1.0.0")

    compile(kotlin("stdlib-jdk8"))
    
    //Tests
    testCompile("org.slf4j", "slf4j-simple", "1.7.26")
    testCompile("junit", "junit", "4.12")
    testCompile("org.mockito:mockito-core:2.28.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_12
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}