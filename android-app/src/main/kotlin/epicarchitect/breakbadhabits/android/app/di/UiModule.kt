package epicarchitect.breakbadhabits.android.app.di

import android.content.Context
import epicarchitect.breakbadhabits.android.app.format.DateTimeFormatter
import epicarchitect.breakbadhabits.android.app.format.DurationFormatter

class UiModule(
    private val context: Context,
    val presentationModule: PresentationModule
) {
    val dateTimeFormatter by lazy {
        DateTimeFormatter(
            dateTimeProvider = presentationModule.logicModule.dateTimeProvider,
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