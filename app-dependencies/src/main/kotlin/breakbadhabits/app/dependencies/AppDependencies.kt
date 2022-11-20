package breakbadhabits.app.dependencies

import breakbadhabits.feature.habits.di.HabitsFeatureFactory

interface AppDependencies {
    val habitsFeatureFactory: HabitsFeatureFactory
}