package convention.ktlint

plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    android.set(true)
    filter {
        exclude { "generated" in it.file.path }
        include { "kotlin" in it.file.path }
    }
}