package com.jessi.grabservice.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable

@Stable
data class TabModel(
    val tabName: String,
    @DrawableRes val iconDrawableRes: Int
)