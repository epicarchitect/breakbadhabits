package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.creation

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.habits.tracks.creation.HabitTrackCreationResources


class RussianHabitTrackCreationResources : HabitTrackCreationResources {
    override fun titleText() = "Новое событие"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить это событие."
    override fun finishButton() = "Записать событие"
    override fun habitNameLabel(habitName: String) = "Привычка: $habitName"
}

class EnglishHabitTrackCreationResources : HabitTrackCreationResources {
    override fun titleText() = "New event"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this event."
    override fun finishButton() = "Save event"
    override fun habitNameLabel(habitName: String) = "Habit: $habitName"
}

class LocalizedHabitTrackCreationResources(locale: Locale) : HabitTrackCreationResources by (
    if (locale.language == "ru") {
        RussianHabitTrackCreationResources()
    } else {
        EnglishHabitTrackCreationResources()
    }
)