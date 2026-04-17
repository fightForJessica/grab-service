package com.jessi.grabservice.ui.detail.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun <T> DetailInfoStatusPage(
    info: T?,
    invalidText: String,
    content: @Composable BoxScope.(T) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            modifier = Modifier.align(Alignment.Center),
            targetState = info == null,
            transitionSpec = {
                fadeIn(tween()) togetherWith fadeOut(tween())
            }
        ) { invalid ->
            if (invalid) {
                Text(
                    text = invalidText,
                    fontSize = 18.sp,
                    color = ThemeManager.colorTheme.globalText,
                    fontWeight = FontWeight.W500
                )
            } else {
                info?.let {
                    content(it)
                }
            }
        }
    }
}