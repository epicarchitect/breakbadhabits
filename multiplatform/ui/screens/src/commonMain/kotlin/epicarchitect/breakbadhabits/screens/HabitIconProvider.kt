package epicarchitect.breakbadhabits.screens

import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.ui.icons.Icons
import kotlinx.coroutines.flow.flowOf

class HabitIconProvider : IconProvider {
    private val icons = listOf(
        Icons.Smoke,
        Icons.Phone,
        Icons.Pizza,
        Icons.ShoppingBag,
        Icons.Bedtime,
        Icons.DinnerDining,
        Icons.WineBar,
        Icons.Laptop,
        Icons.Coffee,
        Icons.Message,
        Icons.SportsSoccer,
        Icons.PhotoCamera,
        Icons.Cake,
        Icons.Bug,
        Icons.Casino,
        Icons.Umbrella,
        Icons.MusicNote,
        Icons.Infinite,
        Icons.ShoppingCart,
        Icons.FrontHand,
        Icons.Games,
        Icons.Wifi,
        Icons.Whatshot,
        Icons.Bolt,
        Icons.Light,
        Icons.VideogameAsset,
        Icons.Fastfood,
        Icons.Grass
    )

    override fun iconsFlow() = flowOf(icons)
}