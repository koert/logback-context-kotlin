buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath(kotlin("gradle-plugin", version = "1.3.61"))
  }
}

plugins {
  id ("org.asciidoctor.jvm.convert") version "2.4.0"
  kotlin("jvm") version "1.3.61"
//  id 'org.jetbrains.kotlin.jvm' version '1.3.61'
}

group = "nl.zencode.logback"

repositories {
  mavenCentral()
}

dependencies {
  api("org.slf4j:slf4j-api:1.7.21")
  implementation ("ch.qos.logback:logback-core:1.2.3")
  implementation ("ch.qos.logback:logback-classic:1.2.3")
  implementation(kotlin("stdlib-jdk8"))
}

//application {
//  mainClassName = 'example.Main'
//}

//tasks {
//  "asciidoctor"(org.asciidoctor.gradle.AsciidoctorTask::class) {
//    sourceDir = file("docs")
//    sources(delegateClosureOf<PatternSet> {
//      include("readme.adoc")
//    })
//    outputDir = file("build/docs")
//  }
//}

//compileKotlin {
//  kotlinOptions {
//    jvmTarget = "1.8"
//  }
//}
//compileTestKotlin {
//  kotlinOptions {
//    jvmTarget = "1.8"
//  }
//}