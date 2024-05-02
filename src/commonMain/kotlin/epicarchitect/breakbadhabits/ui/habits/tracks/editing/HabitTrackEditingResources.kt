package epicarchitect.breakbadhabits.ui.habits.tracks.editing

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.entity.validator.IncorrectHabitTrackEventCount

val LocalHabitTrackEditingResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianHabitTrackEditingResources()
    } else {
        EnglishHabitTrackEditingResources()
    }
}

interface HabitTrackEditingResources {
    fun habitNameLabel(name: String): String
    fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason): String
    fun titleText(): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
}

class RussianHabitTrackEditingResources : HabitTrackEditingResources {
    override fun titleText() = "Новое событие"
    override fun commentDescription() = "Вы можете написать комментарий, но это не обязательно."
    override fun commentLabel() = "Комментарий"
    override fun finishDescription() = "Вы всегда сможете изменить или удалить это событие."
    override fun finishButton() = "Записать событие"
    override fun habitNameLabel(name: String) = "Привычка: $name"
    override fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason) = when (reason) {
        IncorrectHabitTrackEventCount.Reason.Empty -> {
            "Поле не может быть пустым"
        }
    }
}

class EnglishHabitTrackEditingResources : HabitTrackEditingResources {
    override fun titleText() = "New event"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentLabel() = "Comment"
    override fun finishDescription() = "You can always change or delete this event."
    override fun finishButton() = "Save event"

    override fun habitNameLabel(name: String) = "Habit: $name"
    override fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason) = when (reason) {
        IncorrectHabitTrackEventCount.Reason.Empty -> {
            "Cant be empty"
        }
    }
}