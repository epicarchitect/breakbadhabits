package epicarchitect.breakbadhabits.resources.strings.app

import epicarchitect.breakbadhabits.resources.strings.appDashboard.AppDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.appSettings.AppSettingsStrings
import epicarchitect.breakbadhabits.resources.strings.durationFormat.DurationFormattingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.creation.HabitCreationStrings
import epicarchitect.breakbadhabits.resources.strings.habits.dashboard.HabitDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.editing.HabitEditingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.records.creation.HabitEventRecordCreationStrings
import epicarchitect.breakbadhabits.resources.strings.habits.records.dashboard.HabitEventRecordsDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.records.editing.HabitEventRecordEditingStrings
import epicarchitect.breakbadhabits.resources.strings.habits.widgets.creation.HabitWidgetCreationStrings
import epicarchitect.breakbadhabits.resources.strings.habits.widgets.dashboard.HabitWidgetsDashboardStrings
import epicarchitect.breakbadhabits.resources.strings.habits.widgets.editing.HabitWidgetEditingStrings

interface AppStrings {
    val appSettingsStrings: AppSettingsStrings
    val appDashboardStrings: AppDashboardStrings
    val habitDashboardStrings: HabitDashboardStrings
    val habitCreationStrings: HabitCreationStrings
    val habitEditingStrings: HabitEditingStrings
    val habitEventRecordsDashboardStrings: HabitEventRecordsDashboardStrings
    val habitEventRecordCreationStrings: HabitEventRecordCreationStrings
    val habitEventRecordEditingStrings: HabitEventRecordEditingStrings
    val habitWidgetsDashboardStrings: HabitWidgetsDashboardStrings
    val habitWidgetCreationStrings: HabitWidgetCreationStrings
    val habitWidgetEditingStrings: HabitWidgetEditingStrings
    val durationFormattingStrings: DurationFormattingStrings
}