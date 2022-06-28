@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(":android:app")
include(":android:compose:molecules")
include(":android:compose:organisms:histogram")
include(":android:compose:organisms:icons-selection")
include(":android:compose:organisms:events-calendar")
include(":android:compose:organisms:statistics")
include(":android:compose:theme")