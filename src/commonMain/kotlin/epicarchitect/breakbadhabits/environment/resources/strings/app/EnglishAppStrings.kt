package epicarchitect.breakbadhabits.environment.resources.strings.app

import epicarchitect.breakbadhabits.environment.resources.strings.appDashboard.EnglishAppDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.appSettings.EnglishAppSettingsStrings
import epicarchitect.breakbadhabits.environment.resources.strings.durationFormat.EnglishDurationFormattingStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.creation.EnglishHabitCreationStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.dashboard.EnglishHabitDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.editing.EnglishHabitEditingStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.records.creation.EnglishHabitEventRecordCreationStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.records.dashboard.EnglishHabitEventRecordsDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.records.editing.EnglishHabitEventRecordEditingStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.widgets.creation.EnglishHabitWidgetCreationStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.widgets.dashboard.EnglishHabitWidgetsDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.widgets.editing.EnglishHabitWidgetEditingStrings

class EnglishAppStrings : AppStrings {
    override val appSettingsStrings = EnglishAppSettingsStrings()
    override val appDashboardStrings = EnglishAppDashboardStrings()
    override val habitDashboardStrings = EnglishHabitDashboardStrings()
    override val habitCreationStrings = EnglishHabitCreationStrings()
    override val habitEditingStrings = EnglishHabitEditingStrings()
    override val habitEventRecordsDashboardStrings = EnglishHabitEventRecordsDashboardStrings()
    override val habitEventRecordCreationStrings = EnglishHabitEventRecordCreationStrings()
    override val habitEventRecordEditingStrings = EnglishHabitEventRecordEditingStrings()
    override val habitWidgetsDashboardStrings = EnglishHabitWidgetsDashboardStrings()
    override val habitWidgetCreationStrings = EnglishHabitWidgetCreationStrings()
    override val habitWidgetEditingStrings = EnglishHabitWidgetEditingStrings()
    override val durationFormattingStrings = EnglishDurationFormattingStrings()
}