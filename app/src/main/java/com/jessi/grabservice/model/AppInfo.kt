package com.jessi.grabservice.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable

@Stable
data class AppInfo(
    val appName: String,
    val packageName: String,
    val isSystemApp: Boolean = false
) {

    fun getAppIconDrawable(context: Context): Drawable {
        return context.packageManager.getApplicationIcon(packageName)
    }

}
