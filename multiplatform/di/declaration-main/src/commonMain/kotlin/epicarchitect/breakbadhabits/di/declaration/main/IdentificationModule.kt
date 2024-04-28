package epicarchitect.breakbadhabits.di.declaration.main

import com.russhwolf.settings.Settings
import epicarchitect.breakbadhabits.di.declaration.IdentificationModule
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator

class IdentificationModule : IdentificationModule {
    override val idGenerator = IdGenerator(settings = Settings())
}