package com.jessi.grabservice.ui.page.widget

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun SingleSwitchLine(
    enable: Boolean = true,
    text: String,
    @DrawableRes drawableRes: Int? = null,
    drawable: Drawable? = null,
    colorFilter: ColorFilter? = null,
    checked: Boolean,
    formatModifier: (Modifier.() -> Modifier)? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    if (drawableRes == null && drawable == null) {
        throw IllegalArgumentException("drawableRes and drawable cannot be null at the same time.")
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).run {
            formatModifier?.invoke(this) ?: this
        },
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

        Spacer(Modifier.width(12.dp)) // marginStart

        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontSize = 16.sp,
            color = ThemeManager.colorTheme.globalText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.width(4.dp))

        CustomSwitch(
            enable = enable,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
