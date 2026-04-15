package com.jessi.grabservice.ui.theme

import androidx.compose.ui.graphics.Color

interface ColorTheme {
    val globalText: Color get() = Color(0xFF000000)
    val secondText: Color get() = Color(0xFFB6B6B6)
    val defaultFilterColor: Color get() = Color(0xFF000000)
    val backgroundColor: Color get() = Color(0xFFEDEDED)
    val cardBackgroundColor: Color get() = Color(0xFFFDFDFD)
    val switchCheckedThumbColor: Color get() = Color(0xFFFFFFFF)
    val switchUnCheckedThumbColor: Color get() = Color(0xFFFFFFFF)
    val switchCheckedRailColor: Color get() = Color(0xFFF66625)
    val switchUnCheckedRailColor: Color get() = Color(0xFFE3E4E4)
    val dividerColor: Color get() = Color(0x33000000)
    val deleteFloatingButtonBackgroundColor: Color get() = Color(0xCCFF0000)
    val tagBackgroundColor: Color get() = Color(0xFFE3E4E4)
}

open class DefaultColorTheme: ColorTheme

val LightColorTheme = DefaultColorTheme()

val DarkColorTheme = object : DefaultColorTheme() {
    override val globalText: Color get() = Color(0xFFFFFFFF)
    override val secondText: Color get() = Color(0xFFB8B8B8)
    override val defaultFilterColor: Color get() = Color(0xFFFFFFFF)
    override val backgroundColor: Color get() = Color(0xFF1A1A1A)
    override val cardBackgroundColor: Color get() = Color(0xFF242424)
    override val switchCheckedThumbColor: Color get() = Color(0xFFD6B5A3)
    override val switchUnCheckedThumbColor: Color get() = Color(0xFFB1B1B1)
    override val switchCheckedRailColor: Color get() = Color(0xFFA04921)
    override val switchUnCheckedRailColor: Color get() = Color(0xFF3C3C3C)
    override val dividerColor: Color get() = Color(0x33FFFFFF)
    override val deleteFloatingButtonBackgroundColor: Color get() = Color(0xFFCFBBFD)
}