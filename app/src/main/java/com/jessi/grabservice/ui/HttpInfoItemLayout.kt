package com.jessi.grabservice.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.Logger

@Composable
fun HttpInfoItemLayout(
    modifier: Modifier = Modifier,
    iconDrawable: Drawable,
    url: String?,
    headText: String?,
    onClick: () -> Unit,
    tagContent: @Composable (RowScope.() -> Unit)?
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                bitmap = iconDrawable.toBitmap().asImageBitmap(),
                contentDescription = null
            )

            Spacer(Modifier.width(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = url ?: run {
                    Logger.w("unknown url")
                    "Unknown"
                },
                fontSize = 16.sp,
                color = ThemeManager.colorTheme.globalText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(4.dp))

        if (!headText.isNullOrEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = headText,
                fontSize = 14.sp,
                color = ThemeManager.colorTheme.secondText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
        }

        if (tagContent != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                content = tagContent
            )
        }
    }
}