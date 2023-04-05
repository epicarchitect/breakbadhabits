package breakbadhabits.app.logic.icons

interface LocalIconProvider {
    fun getIcons(): List<LocalIcon>
    fun getIcon(id: Int): LocalIcon
}