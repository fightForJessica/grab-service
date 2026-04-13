package com.jessi.grabservice.model

import androidx.annotation.DrawableRes

data class TabModel(
    val tabName: String,
    @DrawableRes val iconDrawableRes: Int
)