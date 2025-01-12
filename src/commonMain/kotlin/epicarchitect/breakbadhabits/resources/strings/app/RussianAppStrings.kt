package epicarchitect.breakbadhabits.resources.strings.app

import epicarchitect.breakbadhabits.resources.strings.appDashboard.RussianAppDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.format.datetime.RUSSIAN_FULL
import epicarchitect.breakbadhabits.resources.strings.format.duration.RussianDurationFormattingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.dashboard.RussianHabitDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.editing.RussianHabitEditingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.dashboard.RussianHabitEventRecordsDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.editing.RussianHabitEventRecordEditingStrings
import kotlinx.datetime.format.MonthNames

class RussianAppStrings : AppStrings {
    override val appDashboardStrings = RussianAppDashboardStrings()
    override val habitDashboardStrings = RussianHabitDashboardStrings()
    override val habitEditingStrings = RussianHabitEditingStrings()
    override val habitEventRecordsDashboardStrings = RussianHabitEventRecordsDashboardStrings()
    override val habitEventRecordEditingStrings = RussianHabitEventRecordEditingStrings()
    override val durationFormattingStrings = RussianDurationFormattingStrings()
    override val monthNames = MonthNames.RUSSIAN_FULL
}