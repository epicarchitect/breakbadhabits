import epicarchitect.breakbadhabits.database.main.MainDatabaseFactory
import epicarchitect.breakbadhabits.di.declaration.impl.AppModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.database.MainDatabaseModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.CoroutinesModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.FoundationModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.FoundationModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.IdentificationModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.logic.HabitsLogicModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.logic.LogicModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.logic.LogicModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.presentation.PresentationModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.ui.FormatModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.ui.UiModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.ui.UiModuleImpl
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.coroutines.JvmCoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.ui.format.DateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

fun setupAppModuleHolder() {
    val foundationModule = FoundationModuleImpl(
        external = object : FoundationModuleExternal {
            override val identificationModuleExternal = object : IdentificationModuleExternal {
                override val idGenerator = object : IdGenerator {
                    var x = 0
                    override fun nextId() = x++

                    override fun setLastId(lastId: Int) {
                        x = lastId
                    }
                }
            }
            override val coroutinesModuleExternal = object : CoroutinesModuleExternal {
                override val coroutineDispatchers = JvmCoroutineDispatchers()
            }
        }
    )
    val logicModule = LogicModuleImpl(
        foundation = foundationModule,
        external = object : LogicModuleExternal {
            override val mainDatabaseExternal = object : MainDatabaseModuleExternal {
                override val mainDatabase = MainDatabaseFactory.create()
            }
            override val habitsExternal = object : HabitsLogicModuleExternal {
                override val habitIconProvider = object : IconProvider {
                    val list = listOf(FakeIcon(1), FakeIcon(2), FakeIcon(3))
                    override fun iconsFlow() = flowOf(list)

                    override suspend fun findIcon(id: Int) = list.find { it.id == id }
                }
            }
        }
    )
    val presentationModule = PresentationModuleImpl(
        logic = logicModule
    )
    val uiModule = UiModuleImpl(
        presentation = presentationModule,
        external = object : UiModuleExternal {
            override val formatModuleExternal = object : FormatModuleExternal {
                override val dateTimeFormatter = object : DateTimeFormatter {
                    override fun formatDateTime(instant: Instant) = instant.toString()

                    override fun formatDateTime(dateTime: LocalDateTime) = dateTime.toString()

                }
                override val durationFormatter = object : DurationFormatter {
                    override val defaultAccuracy = DurationFormatter.Accuracy.SECONDS

                    override fun format(
                        duration: Duration,
                        accuracy: DurationFormatter.Accuracy
                    ) = duration.toString()
                }
            }
        }
    )

    AppModuleHolder.current = AppModuleImpl(uiModule)
}
