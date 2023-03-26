package breakbadhabits.android.app.di

import androidx.compose.runtime.compositionLocalOf

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalLogicModule = compositionLocalOf<LogicModule> {
    error("LocalLogicModule not provided")
}

val LocalUiModule = compositionLocalOf<UiModule> {
    error("LocalUiModule not provided")
}