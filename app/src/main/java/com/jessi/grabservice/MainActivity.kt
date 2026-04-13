package com.jessi.grabservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.jessi.grabservice.ui.Main
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.Logger

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(isSystemInDarkTheme) {
                Logger.i("MainActivity: theme update, isDark: $isSystemInDarkTheme")
                ThemeManager.updateTheme(isSystemInDarkTheme)
            }

            Scaffold(Modifier.Companion.fillMaxSize()) { innerPadding ->
                Main(
                    context = this@MainActivity,
                    paddingValues = innerPadding
                )
            }
        }
    }

}