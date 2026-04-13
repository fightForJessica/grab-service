package com.jessi.grabservice.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

fun Context.drawableResIdToBitmap(@DrawableRes resId: Int): ImageBitmap {
    return getDrawable(resId)?.toBitmap()?.asImageBitmap() ?: throw IllegalArgumentException(
        "target resource id $resId cannot find target Drawable"
    )
}