package epicarchitect.breakbadhabits.database

interface IdGenerator {

    fun nextId(): Int

    fun setLastId(lastId: Int)
}