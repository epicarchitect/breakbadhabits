package epicarchitect.breakbadhabits.habits

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LaptopChromebook
import androidx.compose.material.icons.filled.Light
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WineBar

class HabitIcons {
    val items = listOf(
        HabitIcon(0, Icons.Filled.SmokingRooms),
        HabitIcon(1, Icons.Filled.PhoneAndroid),
        HabitIcon(2, Icons.Filled.LocalPizza),
        HabitIcon(3, Icons.Filled.ShoppingBag),
        HabitIcon(4, Icons.Filled.Bedtime),
        HabitIcon(5, Icons.Filled.DinnerDining),
        HabitIcon(6, Icons.Filled.WineBar),
        HabitIcon(7, Icons.Filled.LaptopChromebook),
        HabitIcon(8, Icons.Filled.Coffee),
        HabitIcon(9, Icons.AutoMirrored.Filled.Message),
        HabitIcon(10, Icons.Filled.SportsSoccer),
        HabitIcon(11, Icons.Filled.PhotoCamera),
        HabitIcon(12, Icons.Filled.Cake),
        HabitIcon(13, Icons.Filled.BugReport),
        HabitIcon(14, Icons.Filled.Casino),
        HabitIcon(15, Icons.Filled.Umbrella),
        HabitIcon(16, Icons.Filled.MusicNote),
        HabitIcon(17, Icons.Filled.AllInclusive),
        HabitIcon(18, Icons.Filled.ShoppingCart),
        HabitIcon(19, Icons.Filled.FrontHand),
        HabitIcon(20, Icons.Filled.Games),
        HabitIcon(21, Icons.Filled.Wifi),
        HabitIcon(22, Icons.Filled.Whatshot),
        HabitIcon(23, Icons.Filled.Bolt),
        HabitIcon(24, Icons.Filled.Light),
        HabitIcon(25, Icons.Filled.VideogameAsset),
        HabitIcon(26, Icons.Filled.Fastfood),
        HabitIcon(27, Icons.Filled.Grass)
    )

    fun getById(id: Int) = items.first { it.id == id }
}