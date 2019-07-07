import com.jfrog.bintray.gradle.BintrayExtension
import groovy.lang.GroovyObject
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

plugins {
    id("org.jetbrains.dokka") version "0.9.18"
    id("com.jfrog.bintray") version "1.8.4"
    id("com.jfrog.artifactory") version "4.9.6"
    kotlin("jvm") version "1.3.11"
    `maven-publish`
    java
}

group = "me.schlaubi.regnumutils"
version = "1.2.1-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("net.dv8tion:JDA:4.ALPHA.0_103") {
        exclude(module = "opus-java")
    }

    compile("cc.hawkbot.regnum", "client", "1.0.0")

    compile(kotlin("stdlib-jdk8"))

    //Tests
    testCompile("org.slf4j", "slf4j-simple", "1.7.26")
    testCompile("junit", "junit", "4.12")
    testCompile("org.mockito:mockito-core:2.28.2")
}

val sourcesJar by tasks.creating(Jar::class)
val dokkaJar by tasks.creating(Jar::class)

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = "regnum-util-common"
        userOrg = "hawk"
        setLicenses("GPL-3.0")
        vcsUrl = "https://github.com/HawkDiscord/regnum-util.git"
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version as String
        })
    })
}

artifactory {
    setContextUrl("https://oss.jfrog.org/artifactory")
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<GroovyObject> {
            setProperty("repoKey", "oss-snapshot-local")
            setProperty("username", System.getenv("BINTRAY_USER"))
            setProperty("password", System.getenv("BINTRAY_KEY"))
            setProperty("maven", true)
        })
        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", "mavenJava")
        })
    })
    resolve(delegateClosureOf<ResolverConfig> {
        setProperty("repoKey", "repo")
    })
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)
            artifact(dokkaJar)
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String
        }
    }
}

tasks {
    dokka {
        moduleName = "command"
        outputDirectory = "${project.parent!!.projectDir}/docs"
        // Oracle broke it
        noJdkLink = true
        reportUndocumented = true
        impliedPlatforms = mutableListOf("JVM")
        linkMapping {
            dir = "./"
            url = "https://github.com/HawkDiscord/regnum-util/tree/master"
            suffix = "#L"
        }
        externalDocumentationLink {
            url = uri("https://www.slf4j.org/api/").toURL()
        }
        externalDocumentationLink {
            url = uri("http://fasterxml.github.io/jackson-databind/javadoc/2.9/").toURL()
        }
        externalDocumentationLink {
            url = uri("https://pages.hawkbot.cc/shared/").toURL()
        }
        externalDocumentationLink {
            url = uri("https://pages.hawkbot.cc/client/").toURL()
        }
        externalDocumentationLink {
            url = uri("https://ci.dv8tion.net/job/JDA4-Alpha/javadoc/").toURL()
            packageListUrl =
                uri("https://gist.githubusercontent.com/DRSchlaubi/3d1d0aaa5c01963dcd4d0149c841c896/raw/22141759fbab1e38fd2381c3e4f97616ecb43fc8/package-list").toURL()
        }
    }

    "sourcesJar"(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    "dokkaJar"(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
        from(dokka)
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_12
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}