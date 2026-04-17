package com.jessi.grabservice.ui.detail.widget

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.jessi.grabservice.ui.DividerLine
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun SingleInfoItem(
    @DrawableRes drawableRes: Int? = null,
    drawable: Drawable? = null,
    title: String,
    contentText: String,
    colorFilter: ColorFilter? = null
) {
    if (drawableRes == null && drawable == null) {
        throw IllegalArgumentException("drawableRes and drawable cannot be null at the same time.")
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (drawableRes != null) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(drawableRes),
                    contentDescription = null,
                    colorFilter = colorFilter
                )
            } else if (drawable != null) {
                Image(
                    modifier = Modifier.size(20.dp),
                    bitmap = drawable.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    colorFilter = colorFilter
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                color = ThemeManager.colorTheme.globalText,
                fontWeight = FontWeight.W500
            )
        }

        Spacer(Modifier.height(4.dp))
        DividerLine(horizontalPadding = 0.dp)
        Spacer(Modifier.height(4.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = contentText,
            fontSize = 16.sp,
            color = ThemeManager.colorTheme.secondText
        )
    }

}