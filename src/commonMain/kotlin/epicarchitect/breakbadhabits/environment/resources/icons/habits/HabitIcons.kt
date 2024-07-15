package epicarchitect.breakbadhabits.environment.resources.icons.habits

import epicarchitect.breakbadhabits.environment.resources.icons.common.CommonIcons
import epicarchitect.breakbadhabits.environment.resources.icons.Icon

class HabitIcons(icons: CommonIcons) : List<Icon> by listOf(
    icons.smoke,
    icons.phone,
    icons.pizza,
    icons.shoppingBag,
    icons.bedtime,
    icons.dinnerDining,
    icons.wineBar,
    icons.laptop,
    icons.coffee,
    icons.message,
    icons.sportsSoccer,
    icons.photoCamera,
    icons.cake,
    icons.bug,
    icons.casino,
    icons.umbrella,
    icons.musicNote,
    icons.infinite,
    icons.shoppingCart,
    icons.frontHand,
    icons.games,
    icons.wifi,
    icons.whatshot,
    icons.bolt,
    icons.light,
    icons.videogameAsset,
    icons.fastfood,
    icons.grass
) {
    fun getById(id: Int) = first { it.id == id }
}