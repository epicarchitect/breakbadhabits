package epicarchitect.breakbadhabits.data.resources.strings

import androidx.compose.ui.text.intl.Locale

interface AppStrings {
    val appSettingsStrings: AppSettingsStrings
    val dashboardStrings: DashboardStrings
    val habitDetailsStrings: HabitDetailsStrings
    val habitCreationStrings: HabitCreationStrings
    val habitEditingStrings: HabitEditingStrings
    val habitTracksStrings: HabitTracksStrings
    val habitTrackCreationStrings: HabitTrackCreationStrings
    val habitTrackEditingStrings: HabitTrackEditingStrings
    val habitWidgetsStrings: HabitWidgetsStrings
    val habitWidgetCreationStrings: HabitWidgetCreationStrings
    val habitWidgetEditingStrings: HabitWidgetEditingStrings
    val durationFormattingStrings: DurationFormattingStrings
}

class RussianAppStrings : AppStrings {
    override val appSettingsStrings = RussianAppSettingsStrings()
    override val dashboardStrings = RussianDashboardStrings()
    override val habitDetailsStrings = RussianHabitDetailsStrings()
    override val habitCreationStrings = RussianHabitCreationStrings()
    override val habitEditingStrings = RussianHabitEditingStrings()
    override val habitTracksStrings = RussianHabitTracksStrings()
    override val habitTrackCreationStrings = RussianHabitTrackCreationStrings()
    override val habitTrackEditingStrings = RussianHabitTrackEditingStrings()
    override val habitWidgetsStrings = RussianHabitWidgetsStrings()
    override val habitWidgetCreationStrings = RussianHabitWidgetCreationStrings()
    override val habitWidgetEditingStrings = RussianHabitWidgetEditingStrings()
    override val durationFormattingStrings = RussianDurationFormattingStrings()
}

class EnglishAppStrings : AppStrings {
    override val appSettingsStrings = EnglishAppSettingsStrings()
    override val dashboardStrings = EnglishDashboardStrings()
    override val habitDetailsStrings = EnglishHabitDetailsStrings()
    override val habitCreationStrings = EnglishHabitCreationStrings()
    override val habitEditingStrings = EnglishHabitEditingStrings()
    override val habitTracksStrings = EnglishHabitTracksStrings()
    override val habitTrackCreationStrings = EnglishHabitTrackCreationStrings()
    override val habitTrackEditingStrings = EnglishHabitTrackEditingStrings()
    override val habitWidgetsStrings = EnglishHabitWidgetsStrings()
    override val habitWidgetCreationStrings = EnglishHabitWidgetCreationStrings()
    override val habitWidgetEditingStrings = EnglishHabitWidgetEditingStrings()
    override val durationFormattingStrings = EnglishDurationFormattingStrings()
}

class LocalizedAppStrings(locale: Locale) : AppStrings by resolve(locale)

private fun resolve(locale: Locale) = when (locale.language) {
    "ru" -> RussianAppStrings()
    else -> EnglishAppStrings()
}