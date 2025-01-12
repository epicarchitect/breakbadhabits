package epicarchitect.breakbadhabits.resources.strings.app

import epicarchitect.breakbadhabits.language.AppLanguage
import epicarchitect.breakbadhabits.resources.strings.appDashboard.EnglishAppDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.format.duration.EnglishDurationFormattingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.dashboard.EnglishHabitDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.editing.EnglishHabitEditingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.dashboard.EnglishHabitEventRecordsDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.editing.EnglishHabitEventRecordEditingStrings
import kotlinx.datetime.format.MonthNames

class EnglishAppStrings : AppStrings {
    override val appDashboardStrings = EnglishAppDashboardStrings()
    override val habitDashboardStrings = EnglishHabitDashboardStrings()
    override val habitEditingStrings = EnglishHabitEditingStrings()
    override val habitEventRecordsDashboardStrings = EnglishHabitEventRecordsDashboardStrings()
    override val habitEventRecordEditingStrings = EnglishHabitEventRecordEditingStrings()
    override val durationFormattingStrings = EnglishDurationFormattingStrings()
    override val monthNames = MonthNames.ENGLISH_FULL
}