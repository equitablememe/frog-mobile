plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

tasks.register<JavaExec>("coreDemo") {
    dependsOn(tasks.named("testClasses"))
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("frog.core.CoreDemoKt")
}
