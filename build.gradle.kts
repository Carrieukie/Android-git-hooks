// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt) apply false

}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(
                rootProject.file("${project.rootDir}/spotless/copyright.kt"),
                "^(package|object|import|interface)",
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("misc") {
            target("**/*.md", "**/.gitignore")
            trimTrailingWhitespace()
            indentWithTabs()
            endWithNewline()
        }
        java {
            target("src/*/java/**/*.java")
            indentWithSpaces()
            licenseHeaderFile(rootProject.file("spotless/copyright.java"))
            removeUnusedImports()
        }
        groovyGradle {
            target("**/*.gradle")
        }
    }

    afterEvaluate {
        // Apply detekt plugin for every module
        apply(plugin = "io.gitlab.arturbosch.detekt")
        val detektTaskProvider = tasks.named<io.gitlab.arturbosch.detekt.Detekt>("detekt")
        detektTaskProvider.configure {
            reports {
                html.required.set(true)
                sarif.required.set(false)
                xml.required.set(false)
                txt.required.set(false)
                md.required.set(false)
                html.outputLocation.set(file("${project.rootDir.absolutePath}/build/reports/detekt/report.html"))
            }
        }
        if (project.name == "rules") return@afterEvaluate
//        tasks.named("preBuild").configure {
//            finalizedBy(detektTaskProvider)
//        }
    }

}