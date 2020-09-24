import com.lightningkite.khrysalis.gradle.KhrysalisPluginExtension
import com.lightningkite.khrysalis.ios.layout.mapViews

fun DependencyHandler.elastic(notation: String): Dependency {
    val projectName = notation.substringAfter(':').substringBefore(':')
    return rootProject.allprojects.find { it.name == projectName && it.subprojects.isEmpty() }?.let {
        val path = ":" + generateSequence(it) { it.parent }.map { it.name }.toList().reversed().drop(1).joinToString(":")
        println("Using local dependency on ${path}")
        project(path, "default")
    } ?: run {
        dependencies.create(notation)
    }
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath("com.lightningkite.khrysalis:plugin:0.1.0")
    }
}

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("digital.wup.android-maven-publish") version "3.6.2"
}
apply(plugin = "com.lightningkite.khrysalis")

configure<KhrysalisPluginExtension> {
    projectName = "ButterflyFCM"
    organizationName = "Lightning Kite"
    overrideIosFolder = project.projectDir.resolve("../ios-fcm").toString()
    overrideWebFolder = project.projectDir.resolve("../web-fcm").toString()
}

group = "com.lightningkite.khrysalis"
version = "0.1.1"

repositories {
    jcenter()
    mavenCentral()
    maven("https://jitpack.io")
    google()
    mavenLocal()
    maven("https://maven.google.com")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(30)
        versionCode = 5
        versionName = "1.0.5"
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api(elastic("com.lightningkite.butterfly:butterfly-android:0.1.1"))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
    api("com.google.firebase:firebase-messaging:20.2.4")
}

tasks.create("sourceJar", Jar::class) {
    classifier = "sources"
    from(android.sourceSets["main"].java.srcDirs)
    from(project.projectDir.resolve("src/include"))
}

publishing {
    publications {
        val mavenAar by creating(MavenPublication::class) {
            from(components["android"])
            artifact(tasks.getByName("sourceJar"))
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}