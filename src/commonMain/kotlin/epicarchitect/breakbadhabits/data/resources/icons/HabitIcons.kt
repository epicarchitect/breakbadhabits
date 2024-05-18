package epicarchitect.breakbadhabits.data.resources.icons

class HabitIcons(commonIcons: CommonIcons) : List<Icon> by listOf(
    commonIcons.Smoke,
    commonIcons.Phone,
    commonIcons.Pizza,
    commonIcons.ShoppingBag,
    commonIcons.Bedtime,
    commonIcons.DinnerDining,
    commonIcons.WineBar,
    commonIcons.Laptop,
    commonIcons.Coffee,
    commonIcons.Message,
    commonIcons.SportsSoccer,
    commonIcons.PhotoCamera,
    commonIcons.Cake,
    commonIcons.Bug,
    commonIcons.Casino,
    commonIcons.Umbrella,
    commonIcons.MusicNote,
    commonIcons.Infinite,
    commonIcons.ShoppingCart,
    commonIcons.FrontHand,
    commonIcons.Games,
    commonIcons.Wifi,
    commonIcons.Whatshot,
    commonIcons.Bolt,
    commonIcons.Light,
    commonIcons.VideogameAsset,
    commonIcons.Fastfood,
    commonIcons.Grass
) {
    fun getById(id: Int) = first { it.id == id }
}