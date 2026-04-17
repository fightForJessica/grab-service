package com.jessi.grabservice.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class AppInfo(
    val appName: String,
    val packageName: String,
    val uid: Int,
    val isSystemApp: Boolean = false,
    val isGrabSelected: Boolean = false
) : Parcelable

enum class AppInfoLoadStatus {
    NONE, LOADING, FAILURE, SUCCESS
}