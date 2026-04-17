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
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.model.HttpRsp
import com.jessi.grabservice.ui.detail.Detail
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.Logger

class DetailActivity : ComponentActivity() {

    companion object {
        const val KEY_APP_INFO = "key_app_info"
        const val KEY_REQUEST = "key_request"
        const val KEY_RESPONSE = "key_response"
        const val KEY_INIT_PAGE_INDEX = "key_init_page_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i("DetailActivity onCreate")
        enableEdgeToEdge()
        val appInfo = intent.getParcelableExtra(KEY_APP_INFO) as? AppInfo
        require(appInfo != null)
        val request = intent.getParcelableExtra(KEY_REQUEST) as? HttpReq
        val response = intent.getParcelableExtra(KEY_RESPONSE) as? HttpRsp
        val initPageIndex = intent.getIntExtra(KEY_INIT_PAGE_INDEX, 0)
        setContent {

            // 主题色适配
            val isSystemInDarkTheme = isSystemInDarkTheme()
            ThemeManager.updateTheme(isSystemInDarkTheme)
            Logger.i("DetailActivity: theme isDark: $isSystemInDarkTheme")

            Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                // intent 传递参数
                Detail(
                    context = this@DetailActivity,
                    paddingValues = innerPadding,
                    appInfo = appInfo,
                    request = request,
                    response = response,
                    initPageIndex = initPageIndex
                )

            }
        }
    }

}