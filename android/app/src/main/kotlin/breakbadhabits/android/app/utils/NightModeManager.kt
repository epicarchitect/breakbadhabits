package breakbadhabits.android.app.utils

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class NightModeManager(private val application: Application) {

    private val preferences = application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var mode = preferences.getInt(KEY_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).toThemeMode()
        set(value) {
            field = value
            value.toAppCompatDelegateInt().let {
                preferences.edit { putInt(KEY_MODE, it) }
                AppCompatDelegate.setDefaultNightMode(it)
            }
        }

    val isNightModeActive get() = application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    
    init {
        AppCompatDelegate.setDefaultNightMode(mode.toAppCompatDelegateInt())
    }

    private fun Int.toThemeMode() = when (this) {
        AppCompatDelegate.MODE_NIGHT_YES -> Mode.NIGHT
        AppCompatDelegate.MODE_NIGHT_NO -> Mode.NOT_NIGHT
        else -> Mode.FOLLOW_SYSTEM
    }

    private fun Mode.toAppCompatDelegateInt() = when (this) {
        Mode.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
        Mode.NOT_NIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        Mode.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    enum class Mode {
        NIGHT,
        NOT_NIGHT,
        FOLLOW_SYSTEM
    }

    companion object {
        private const val PREFERENCES_NAME = "NightModeManager"
        private const val KEY_MODE = "mode"
    }
}