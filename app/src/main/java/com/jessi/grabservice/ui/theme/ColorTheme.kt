package com.jessi.grabservice.ui.theme

import androidx.compose.ui.graphics.Color

interface ColorTheme {
    val globalText: Color get() = Color(0xFF000000)
}

open class DefaultColorTheme: ColorTheme

val LightColorTheme = DefaultColorTheme()

val DarkColorTheme = object : DefaultColorTheme() {
    override val globalText: Color get() = Color(0xFFFFFFFF)
}