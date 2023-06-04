package epicarchitect.breakbadhabits.android.app.base.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import epicarchitect.breakbadhabits.ui.theme.AppColorsSchemes

abstract class ComposeActivity : AppCompatActivity() {

    protected open val themeResourceId: Int? = null
    private lateinit var darkModeManager: DarkModeManager

    @Composable
    abstract fun Content()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        darkModeManager = DarkModeManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeResourceId?.let(::setTheme)
        setContent {
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalDarkModeManager provides darkModeManager
            ) {
                val darkMode by LocalDarkModeManager.current.mode
                AppTheme(
                    colorScheme = AppColorsSchemes.light
                ) {
                    Content()
                }
            }
        }
    }
}