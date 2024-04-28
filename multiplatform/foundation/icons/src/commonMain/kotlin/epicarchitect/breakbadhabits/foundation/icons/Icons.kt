package epicarchitect.breakbadhabits.foundation.icons

interface Icons {
    val list: List<Icon>
}

operator fun Icons.get(id: Int) = list.first { it.id == id }