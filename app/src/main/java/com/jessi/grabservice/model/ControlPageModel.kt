package com.jessi.grabservice.model

import androidx.compose.runtime.Stable

@Stable
data class AppInfo(
    val appName: String,
    val packageName: String,
    val uid: Int,
    val isSystemApp: Boolean = false,
    val isGrabSelected: Boolean = false
)

enum class AppInfoLoadStatus {
    NONE, LOADING, FAILURE, SUCCESS
}