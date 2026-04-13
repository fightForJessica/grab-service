package com.jessi.grabservice.ui.theme

import kotlinx.coroutines.flow.MutableStateFlow

object ThemeManager {

    private val _colorTheme = MutableStateFlow(LightColorTheme)
    val colorTheme: ColorTheme get() = _colorTheme.value

    fun updateTheme(isDark: Boolean) {
        _colorTheme.value = if (isDark) DarkColorTheme else LightColorTheme
    }

}