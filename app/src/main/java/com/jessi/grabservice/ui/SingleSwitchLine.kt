package com.jessi.grabservice.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun SingleSwitchLine(
    text: String,
    @DrawableRes drawableRes: Int,
    colorFilter: ColorFilter? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(drawableRes),
            contentDescription = null,
            colorFilter = colorFilter
        )

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
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
