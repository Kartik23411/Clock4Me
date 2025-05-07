buildscript {
    repositories { google(); mavenCentral() }
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        // you can drop the extra kotlin-gradle-plugin if you're using the above alias
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)      apply false
    alias(libs.plugins.kotlin.android)      version libs.versions.kotlin apply false
    alias(libs.plugins.compose.compiler)    version libs.versions.kotlin apply false
    alias(libs.plugins.ksp)                 version libs.versions.ksp    apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}