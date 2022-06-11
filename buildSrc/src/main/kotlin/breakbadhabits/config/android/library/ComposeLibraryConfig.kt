package breakbadhabits.config.android.library

import breakbadhabits.config.android.base.baseComposeConfig
import com.android.build.gradle.LibraryExtension

fun LibraryExtension.composeLibraryConfig() {
    libraryConfig()
    baseComposeConfig()
}