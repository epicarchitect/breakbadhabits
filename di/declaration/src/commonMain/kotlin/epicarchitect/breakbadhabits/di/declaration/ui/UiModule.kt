package epicarchitect.breakbadhabits.di.declaration.ui

import epicarchitect.breakbadhabits.di.declaration.presentation.PresentationModule

interface UiModule {
    val presentation: PresentationModule
    val format: FormatModule
}