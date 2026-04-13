package com.jessi.grabservice.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    width: Dp = 42.dp,
    height: Dp = 24.dp
) {
    val thumbSize = height - 6.dp
    val thumbToBorderMargin = 3.dp

    // 滑块位移
    val animateOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - thumbToBorderMargin else thumbToBorderMargin,
        animationSpec = tween(durationMillis = 200),
        label = "thumb_offset"
    )

    // 滑块颜色
    val thumbColor by animateColorAsState(
        targetValue = if (checked) {
            ThemeManager.colorTheme.switchCheckedThumbColor
        } else {
            ThemeManager.colorTheme.switchUnCheckedThumbColor
        },
        animationSpec = tween(durationMillis = 200),
        label = "thumb_color"
    )

    // 滑轨颜色
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) {
            ThemeManager.colorTheme.switchCheckedRailColor
        } else {
            ThemeManager.colorTheme.switchUnCheckedRailColor
        },
        animationSpec = tween(durationMillis = 200),
        label = "rail_color"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = animateOffset, y = (height - thumbSize) / 2)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}