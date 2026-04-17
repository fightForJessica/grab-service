package com.jessi.grabservice

import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.model.HttpRsp
import com.jessi.grabservice.proxy.ProxyHelper
import com.jessi.grabservice.ui.IMainContentCallback
import com.jessi.grabservice.ui.Main
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.Logger
import com.jessi.grabservice.viewmodel.MainViewModel
import com.jessi.grabservice.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private var isHibernateLock = false

    private val hibernateLock by lazy {
        getSystemService<PowerManager>()?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "grab-service::main"
        )
    }

    private val viewModel by lazy {
        ViewModelProvider.create(this, MainViewModelFactory())[MainViewModel::class.java]
    }
    private lateinit var proxyHelper: ProxyHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        proxyHelper = ProxyHelper(this, viewModel)
        setContent {

            // 主题色适配
            val isSystemInDarkTheme = isSystemInDarkTheme()
            ThemeManager.updateTheme(isSystemInDarkTheme)
            Logger.i("MainActivity: theme isDark: $isSystemInDarkTheme")

            Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                Main(
                    context = this@MainActivity,
                    viewModel = viewModel,
                    paddingValues = innerPadding,
                    callback = object : IMainContentCallback {
                        override fun onMainSwitchSelect(select: Boolean) {
                            if (select) {
                                proxyHelper.tryStartGrabService()
                            } else {
                                proxyHelper.stopGrabService()
                            }
                        }

                        override fun onHibernateLockSelect(select: Boolean) {
                            if (select) {
                                enableHibernateLock()
                            } else {
                                releaseHibernateLock()
                            }
                        }

                        override fun onRequestItemClick(
                            request: HttpReq,
                            appInfo: AppInfo
                        ) {
                            openContentDetailPage(appInfo, request, null, 0)
                        }

                        override fun onResponseItemClick(
                            response: HttpRsp,
                            appInfo: AppInfo
                        ) {
                            openContentDetailPage(appInfo, null, response, 1)
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

    private fun openContentDetailPage(
        appInfo: AppInfo,
        request: HttpReq?,
        response: HttpRsp?,
        initPageIndex: Int
    ) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.KEY_APP_INFO, appInfo)
            if (request != null) {
                putExtra(DetailActivity.KEY_REQUEST, request)
            }
            if (response != null) {
                putExtra(DetailActivity.KEY_RESPONSE, response)
            }
            putExtra(DetailActivity.KEY_INIT_PAGE_INDEX, initPageIndex)
        }
        startActivity(intent)
    }

}