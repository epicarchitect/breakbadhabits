package breakbadhabits.android.app

import android.app.Application

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App

        val architecture by lazy {
            Architecture(instance)
        }
    }
}