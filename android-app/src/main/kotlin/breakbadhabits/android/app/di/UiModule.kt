package breakbadhabits.android.app.di

import android.content.Context
import breakbadhabits.android.app.format.DateTimeFormatter
import breakbadhabits.android.app.format.DurationFormatter

class UiModule(
    private val context: Context,
    val presentationModule: PresentationModule
) {
    val dateTimeFormatter by lazy {
        DateTimeFormatter(
            dateTimeConfigProvider = presentationModule.logicModule.dateTimeConfigProvider,
            context = context
        )
    }

    val durationFormatter by lazy {
        DurationFormatter(
            resources = context.resources,
            defaultAccuracy = DurationFormatter.Accuracy.SECONDS
        )
    }
}