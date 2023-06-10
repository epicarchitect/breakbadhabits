package epicarchitect.breakbadhabits.di.declaration.main.foundation

import com.russhwolf.settings.Settings
import epicarchitect.breakbadhabits.di.declaration.foundation.IdentificationModule
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator

class IdentificationModule : IdentificationModule {
    override val idGenerator by lazy {
        IdGenerator(settings = Settings())
    }
}