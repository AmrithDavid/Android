pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/singularhealth/3cr-sdk-android")
            credentials {
                username = "sam.kellahan@ide.group"
                password = "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"
            }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "android3DicomViewer"
include(":app")
