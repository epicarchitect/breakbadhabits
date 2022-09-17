package breakbadhabits.android.app.repository

import breakbadhabits.android.app.data.HabitsAppWidgetConfig
import breakbadhabits.android.app.database.HabitsAppWidgetConfigEntity
import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.coroutines.flow.mapItems
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppWidgetsRepository(
    private val idGenerator: IdGenerator,
    mainDatabase: MainDatabase
) {
    private val habitsAppWidgetConfigDao = mainDatabase.habitsAppWidgetConfigDao

    suspend fun createHabitsAppWidgetConfig(
        title: String,
        appWidgetId: Int,
        habitIds: Set<Int>
    ) {
        habitsAppWidgetConfigDao.insertEntity(
            HabitsAppWidgetConfig(
                idGenerator.nextId(),
                title,
                appWidgetId,
                habitIds
            ).toHabitsAppWidgetConfigEntity()
        )
    }

    suspend fun updateHabitsAppWidget(
        id: Int,
        title: String,
        habitIds: Set<Int>
    ) = habitsAppWidgetConfigDao.updateEntity(
        id,
        title,
        Json.encodeToString(habitIds)
    )

    suspend fun deleteHabitsAppWidgetConfigById(id: Int) =
        habitsAppWidgetConfigDao.deleteEntityById(id)

    suspend fun deleteHabitsAppWidgetConfigByAppWidgetIds(appWidgetIds: List<Int>) =
        habitsAppWidgetConfigDao.deleteEntityByAppWidgetIds(appWidgetIds)

    suspend fun habitsAppWidgetConfigByAppWidgetId(appWidgetId: Int) =
        habitsAppWidgetConfigDao.entityByAppWidgetId(appWidgetId)?.toHabitsAppWidgetConfig()

    fun habitsAppWidgetConfigByIdFlow(id: Int) =
        habitsAppWidgetConfigDao.entityByIdFlow(id).map { it?.toHabitsAppWidgetConfig() }

    fun habitsAppWidgetConfigListFlow() =
        habitsAppWidgetConfigDao.entityListFlow().mapItems { it.toHabitsAppWidgetConfig() }

    private fun HabitsAppWidgetConfig.toHabitsAppWidgetConfigEntity() =
        HabitsAppWidgetConfigEntity(
            id,
            title,
            appWidgetId,
            Json.encodeToString(habitIds)
        )

    private fun HabitsAppWidgetConfigEntity.toHabitsAppWidgetConfig() =
        HabitsAppWidgetConfig(
            id,
            title,
            appWidgetId,
            Json.decodeFromString(habitIdsJson)
        )
}