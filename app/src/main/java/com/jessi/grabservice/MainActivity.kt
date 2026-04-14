package com.jessi.grabservice

import android.os.Bundle
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import com.jessi.grabservice.ui.IMainContentCallback
import com.jessi.grabservice.ui.Main
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.Logger

class MainActivity : ComponentActivity() {

    private var isHibernateLock = false

    private val hibernateLock by lazy {
        getSystemService<PowerManager>()?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "grab-service::main"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            ThemeManager.updateTheme(isSystemInDarkTheme)
            Logger.i("MainActivity: theme isDark: $isSystemInDarkTheme")

            Scaffold(Modifier.Companion.fillMaxSize()) { innerPadding ->
                Main(
                    context = this@MainActivity,
                    paddingValues = innerPadding,
                    callback = object : IMainContentCallback {
                        override fun onHibernateLockSelect(select: Boolean) {
                            if (select) {
                                enableHibernateLock()
                            } else {
                                releaseHibernateLock()
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseHibernateLock()
    }

    private fun enableHibernateLock() {
        if (isHibernateLock) return
        isHibernateLock = true
        hibernateLock?.acquire()
    }

    private fun releaseHibernateLock() {
        if (!isHibernateLock) return
        isHibernateLock = false
        hibernateLock?.release()
    }

}