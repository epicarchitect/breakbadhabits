package epicarchitect.breakbadhabits.data.resources.icons

class HabitIcons(commonIcons: CommonIcons) : List<Icon> by listOf(
    commonIcons.smoke,
    commonIcons.phone,
    commonIcons.pizza,
    commonIcons.shoppingBag,
    commonIcons.bedtime,
    commonIcons.dinnerDining,
    commonIcons.wineBar,
    commonIcons.laptop,
    commonIcons.coffee,
    commonIcons.message,
    commonIcons.sportsSoccer,
    commonIcons.photoCamera,
    commonIcons.cake,
    commonIcons.bug,
    commonIcons.casino,
    commonIcons.umbrella,
    commonIcons.musicNote,
    commonIcons.infinite,
    commonIcons.shoppingCart,
    commonIcons.frontHand,
    commonIcons.games,
    commonIcons.wifi,
    commonIcons.whatshot,
    commonIcons.bolt,
    commonIcons.light,
    commonIcons.videogameAsset,
    commonIcons.fastfood,
    commonIcons.grass
) {
    fun getById(id: Int) = first { it.id == id }
}