package epicarchitect.breakbadhabits.habits

import epicarchitect.breakbadhabits.uikit.titleResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class HabitProgressTest {

    @Test
    fun zeroDurationStartsResistanceStage() {
        val progress = habitProgress(Duration.ZERO)

        assertEquals(0, progress.days)
        assertEquals(HabitStage.RESISTANCE, progress.stage)
        assertEquals(0f, progress.stageFraction)
        assertFalse(progress.isComplete)
    }

    @Test
    fun stageBoundariesUseNextStage() {
        assertEquals(HabitStage.ADJUSTMENT, habitProgress(7.days).stage)
        assertEquals(HabitStage.REWIRING, habitProgress(21.days).stage)
        assertEquals(HabitStage.CONSOLIDATION, habitProgress(45.days).stage)
        assertEquals(HabitStage.LIBERATION, habitProgress(69.days).stage)
    }

    @Test
    fun stageFractionIsRelativeToStage() {
        val progress = habitProgress(7.days + 12.hours)

        assertEquals(0.5f / 14f, progress.stageFraction, absoluteTolerance = 0.0001f)
    }

    @Test
    fun goalIsCompleteAtNinetyDays() {
        val progress = habitProgress(90.days)

        assertEquals(90, progress.days)
        assertEquals(HabitStage.LIBERATION, progress.stage)
        assertEquals(1f, progress.stageFraction)
        assertTrue(progress.isComplete)
    }

    @Test
    fun durationAboveGoalIsClamped() {
        val progress = habitProgress(120.days)

        assertEquals(HabitGoalDays, progress.days)
        assertEquals(HabitStage.LIBERATION, progress.stage)
        assertEquals(1f, progress.stageFraction)
        assertTrue(progress.isComplete)
    }

    @Test
    fun negativeDurationIsClampedToStart() {
        val progress = habitProgress((-1).days)

        assertEquals(0, progress.days)
        assertEquals(HabitStage.RESISTANCE, progress.stage)
        assertEquals(0f, progress.stageFraction)
    }

    @Test
    fun incompleteProgressUsesStageTitle() {
        assertEquals(
            "habit_stage_liberation",
            habitProgress(89.days).titleResource().key
        )
    }

    @Test
    fun completedProgressUsesFreedomTitle() {
        assertEquals(
            "habit_freedom",
            habitProgress(90.days).titleResource().key
        )
    }
}
