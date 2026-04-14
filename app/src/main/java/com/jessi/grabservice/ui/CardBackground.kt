package com.jessi.grabservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.cardBackground(
    color: Color,
    radius: Dp = 20.dp
): Modifier {
    return this
        .clip(RoundedCornerShape(radius))
        .background(color)
}

fun Modifier.upperHalfConerBackground(
    color: Color,
    radius: Dp = 20.dp
): Modifier {
    return this.clip(RoundedCornerShape(topStart = radius, topEnd = radius))
        .background(color)
}

fun Modifier.lowerHalfConerBackground(
    color: Color,
    radius: Dp = 20.dp
): Modifier {
    return this.clip(RoundedCornerShape(bottomStart = radius, bottomEnd = radius))
        .background(color)
}