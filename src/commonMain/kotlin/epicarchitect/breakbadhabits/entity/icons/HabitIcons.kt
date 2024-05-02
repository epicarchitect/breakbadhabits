package epicarchitect.breakbadhabits.entity.icons

object HabitIcons {
    val list = listOf(
        VectorIcons.Smoke,
        VectorIcons.Phone,
        VectorIcons.Pizza,
        VectorIcons.ShoppingBag,
        VectorIcons.Bedtime,
        VectorIcons.DinnerDining,
        VectorIcons.WineBar,
        VectorIcons.Laptop,
        VectorIcons.Coffee,
        VectorIcons.Message,
        VectorIcons.SportsSoccer,
        VectorIcons.PhotoCamera,
        VectorIcons.Cake,
        VectorIcons.Bug,
        VectorIcons.Casino,
        VectorIcons.Umbrella,
        VectorIcons.MusicNote,
        VectorIcons.Infinite,
        VectorIcons.ShoppingCart,
        VectorIcons.FrontHand,
        VectorIcons.Games,
        VectorIcons.Wifi,
        VectorIcons.Whatshot,
        VectorIcons.Bolt,
        VectorIcons.Light,
        VectorIcons.VideogameAsset,
        VectorIcons.Fastfood,
        VectorIcons.Grass
    )

    operator fun get(id: Int) = list.first { it.id == id }
}