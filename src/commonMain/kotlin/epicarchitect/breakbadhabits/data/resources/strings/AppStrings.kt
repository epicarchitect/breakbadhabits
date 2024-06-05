package epicarchitect.breakbadhabits.data.resources.strings

import androidx.compose.ui.text.intl.Locale

interface AppStrings {
    val appSettingsStrings: AppSettingsStrings
    val dashboardStrings: DashboardStrings
    val habitDetailsStrings: HabitDetailsStrings
    val habitCreationStrings: HabitCreationStrings
    val habitEditingStrings: HabitEditingStrings
    val habitEventRecordsStrings: HabitEventRecordsStrings
    val habitEventRecordCreationStrings: HabitEventRecordCreationStrings
    val habitEventRecordEditingStrings: HabitEventRecordEditingStrings
    val habitWidgetsStrings: HabitWidgetsStrings
    val habitWidgetCreationStrings: HabitWidgetCreationStrings
    val habitWidgetEditingStrings: HabitWidgetEditingStrings
    val durationFormattingStrings: DurationFormattingStrings
    val rangeSelectionCalendarDialogStrings: RangeSelectionCalendarDialogStrings
}

class RussianAppStrings : AppStrings {
    override val appSettingsStrings = RussianAppSettingsStrings()
    override val dashboardStrings = RussianDashboardStrings()
    override val habitDetailsStrings = RussianHabitDetailsStrings()
    override val habitCreationStrings = RussianHabitCreationStrings()
    override val habitEditingStrings = RussianHabitEditingStrings()
    override val habitEventRecordsStrings = RussianHabitEventRecordsStrings()
    override val habitEventRecordCreationStrings = RussianHabitEventRecordCreationStrings()
    override val habitEventRecordEditingStrings = RussianHabitEventRecordEditingStrings()
    override val habitWidgetsStrings = RussianHabitWidgetsStrings()
    override val habitWidgetCreationStrings = RussianHabitWidgetCreationStrings()
    override val habitWidgetEditingStrings = RussianHabitWidgetEditingStrings()
    override val durationFormattingStrings = RussianDurationFormattingStrings()
    override val rangeSelectionCalendarDialogStrings = RussianRangeSelectionCalendarDialogStrings()
}

class EnglishAppStrings : AppStrings {
    override val appSettingsStrings = EnglishAppSettingsStrings()
    override val dashboardStrings = EnglishDashboardStrings()
    override val habitDetailsStrings = EnglishHabitDetailsStrings()
    override val habitCreationStrings = EnglishHabitCreationStrings()
    override val habitEditingStrings = EnglishHabitEditingStrings()
    override val habitEventRecordsStrings = EnglishHabitEventRecordsStrings()
    override val habitEventRecordCreationStrings = EnglishHabitEventRecordCreationStrings()
    override val habitEventRecordEditingStrings = EnglishHabitEventRecordEditingStrings()
    override val habitWidgetsStrings = EnglishHabitWidgetsStrings()
    override val habitWidgetCreationStrings = EnglishHabitWidgetCreationStrings()
    override val habitWidgetEditingStrings = EnglishHabitWidgetEditingStrings()
    override val durationFormattingStrings = EnglishDurationFormattingStrings()
    override val rangeSelectionCalendarDialogStrings = EnglishRangeSelectionCalendarDialogStrings()
}

class LocalizedAppStrings(locale: Locale) : AppStrings by resolve(locale)

private fun resolve(locale: Locale) = when (locale.language) {
    "ru" -> RussianAppStrings()
    else -> EnglishAppStrings()
}