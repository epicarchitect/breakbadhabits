package convention.detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    config.setFrom("${rootProject.rootDir}/detekt.yml")
    source.setFrom(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/iosMain/kotlin",
        "src/main/kotlin"
    )
    parallel = true
    buildUponDefaultConfig = true
}