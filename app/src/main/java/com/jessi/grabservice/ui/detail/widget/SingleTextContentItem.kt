package com.jessi.grabservice.ui.detail.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun SingleTextContentItem(
    titleText: String,
    contentText: String
) {
    Text(
        text = titleText,
        fontSize = 18.sp,
        color = ThemeManager.colorTheme.globalText
    )
    Text(
        text = contentText,
        fontSize = 16.sp,
        color = ThemeManager.colorTheme.secondText
    )
}