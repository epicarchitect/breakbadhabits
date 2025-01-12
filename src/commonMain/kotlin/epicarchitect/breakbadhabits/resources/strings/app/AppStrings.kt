package epicarchitect.breakbadhabits.resources.strings.app

import epicarchitect.breakbadhabits.resources.strings.appDashboard.AppDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.format.duration.DurationFormattingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.dashboard.HabitDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.editing.HabitEditingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.dashboard.HabitEventRecordsDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.eventRecords.editing.HabitEventRecordEditingStrings
import kotlinx.datetime.format.MonthNames

interface AppStrings {
    val appDashboardStrings: AppDashboardStrings
    val habitDashboardStrings: HabitDashboardStrings
    val habitEditingStrings: HabitEditingStrings
    val habitEventRecordsDashboardStrings: HabitEventRecordsDashboardStrings
    val habitEventRecordEditingStrings: HabitEventRecordEditingStrings
    val durationFormattingStrings: DurationFormattingStrings
    val monthNames: MonthNames
}

