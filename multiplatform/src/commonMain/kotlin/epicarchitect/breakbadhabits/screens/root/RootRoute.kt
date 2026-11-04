package epicarchitect.breakbadhabits.screens.root

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed interface RootRoute : NavKey {

    @Serializable
    data object Dashboard : RootRoute

    @Serializable
    data class Paywall(
        val source: String = "unknown"
    ) : RootRoute

    @Serializable
    data object Settings : RootRoute

    @Serializable
    data class HabitDetails(
        val habitId: Uuid
    ) : RootRoute

    @Serializable
    data class HabitEditing(
        val habitId: Uuid?
    ) : RootRoute

    @Serializable
    data class HabitEventRecordEditing(
        val habitEventRecordId: Uuid?,
        val habitId: Uuid
    ) : RootRoute

    @Serializable
    data class HabitEventRecordsDetails(
        val habitId: Uuid
    ) : RootRoute

    @Serializable
    data class HabitStages(
        val habitId: Uuid
    ) : RootRoute
}
