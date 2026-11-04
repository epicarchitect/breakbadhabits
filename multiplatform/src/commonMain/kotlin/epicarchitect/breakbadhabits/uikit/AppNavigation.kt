package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

fun appNavTransition(): ContentTransform =
    fadeIn(tween(160)) togetherWith fadeOut(tween(120))
