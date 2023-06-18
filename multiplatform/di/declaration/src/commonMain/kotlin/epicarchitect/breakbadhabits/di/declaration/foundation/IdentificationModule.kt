package epicarchitect.breakbadhabits.di.declaration.foundation

import epicarchitect.breakbadhabits.foundation.identification.IdGenerator

interface IdentificationModule {
    val idGenerator: IdGenerator
}
