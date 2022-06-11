package breakbadhabits.config.android.application

import breakbadhabits.config.android.base.baseComposeConfig
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

fun BaseAppModuleExtension.composeApplicationConfig() {
    applicationConfig()
    baseComposeConfig()
}