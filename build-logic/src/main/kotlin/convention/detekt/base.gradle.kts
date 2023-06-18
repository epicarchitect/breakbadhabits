package convention.detekt

import libs

plugins {
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    detektPlugins(libs.arturbosch.detektFormatting)
}

detekt {
    config.setFrom(rootProject.rootDir.path + "/detekt.yml")
    source.setFrom(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/iosMain/kotlin",
        "src/main/kotlin"
    )
    parallel = true
    buildUponDefaultConfig = true
}