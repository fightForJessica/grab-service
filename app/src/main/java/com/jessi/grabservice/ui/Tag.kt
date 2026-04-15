package com.jessi.grabservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun Tag(text: String?) {
    if (text.isNullOrBlank()) return
    Box(
        modifier = Modifier.padding(end = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ThemeManager.colorTheme.tagBackgroundColor)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = ThemeManager.colorTheme.globalText,
            fontSize = 14.sp
        )
    }
}