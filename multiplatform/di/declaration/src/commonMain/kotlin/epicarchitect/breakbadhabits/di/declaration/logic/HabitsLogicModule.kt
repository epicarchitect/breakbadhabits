package epicarchitect.breakbadhabits.di.declaration.logic

import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.logic.habits.creator.HabitCreator
import epicarchitect.breakbadhabits.logic.habits.creator.HabitTrackCreator
import epicarchitect.breakbadhabits.logic.habits.creator.HabitWidgetCreator
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitTrackDeleter
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitWidgetDeleter
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitStatisticsProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitsConfigProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitTrackUpdater
import epicarchitect.breakbadhabits.logic.habits.updater.HabitUpdater
import epicarchitect.breakbadhabits.logic.habits.updater.HabitWidgetUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackDateTimeRangeValidator

interface HabitsLogicModule {
    val habitIconProvider: IconProvider
    val habitAbstinenceProvider: HabitAbstinenceProvider
    val habitCreator: HabitCreator
    val habitsConfigProvider: HabitsConfigProvider
    val habitNewNameValidator: HabitNewNameValidator
    val habitTrackDateTimeRangeValidator: HabitTrackDateTimeRangeValidator
    val habitTrackEventCountValidator: HabitTrackEventCountValidator
    val habitDeleter: HabitDeleter
    val habitProvider: HabitProvider
    val habitUpdater: HabitUpdater
    val habitTrackUpdater: HabitTrackUpdater
    val habitTrackDeleter: HabitTrackDeleter
    val habitStatisticsProvider: HabitStatisticsProvider
    val habitTrackCreator: HabitTrackCreator
    val habitTrackProvider: HabitTrackProvider
    val habitWidgetProvider: HabitWidgetProvider
    val habitWidgetCreator: HabitWidgetCreator
    val habitWidgetUpdater: HabitWidgetUpdater
    val habitWidgetDeleter: HabitWidgetDeleter
}