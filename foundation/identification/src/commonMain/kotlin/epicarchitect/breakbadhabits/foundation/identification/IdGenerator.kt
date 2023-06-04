package epicarchitect.breakbadhabits.foundation.identification

interface IdGenerator {

    fun nextId(): Int

    fun setLastId(lastId: Int)
}