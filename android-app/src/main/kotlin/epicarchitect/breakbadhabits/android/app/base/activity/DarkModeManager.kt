package epicarchitect.breakbadhabits.android.app.base.activity

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit

class DarkModeManager(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val mutableMode = mutableStateOf(
        preferences.getInt(
            KEY_MODE,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        ).toDarkMode()
    )

    val mode: State<DarkMode> = mutableMode

    fun changeMode(mode: DarkMode) {
        mutableMode.value = mode
        mode.toAppCompatDelegateInt().let {
            preferences.edit { putInt(KEY_MODE, it) }
            AppCompatDelegate.setDefaultNightMode(it)
        }
    }

    init {
        AppCompatDelegate.setDefaultNightMode(mutableMode.value.toAppCompatDelegateInt())
    }

    private fun Int.toDarkMode() = when (this) {
        AppCompatDelegate.MODE_NIGHT_YES -> DarkMode.ENABLED
        AppCompatDelegate.MODE_NIGHT_NO -> DarkMode.DISABLED
        else -> DarkMode.BY_SYSTEM
    }

    private fun DarkMode.toAppCompatDelegateInt() = when (this) {
        DarkMode.ENABLED -> AppCompatDelegate.MODE_NIGHT_YES
        DarkMode.DISABLED -> AppCompatDelegate.MODE_NIGHT_NO
        DarkMode.BY_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    companion object {
        private const val PREFERENCES_NAME = "NightModeManager"
        private const val KEY_MODE = "mode"
    }
}