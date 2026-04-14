package com.jessi.grabservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun DividerLine() {
    Box(
        Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(0.5.dp)
            .background(ThemeManager.colorTheme.dividerColor)
    )
}