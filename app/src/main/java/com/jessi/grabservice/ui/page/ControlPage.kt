package com.jessi.grabservice.ui.page

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jessi.grabservice.R
import com.jessi.grabservice.ui.SingleSwitchLine
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.Logger

const val CONTROL_PAGE_NAME = "控制面板"

@Composable
fun ControlPage(
    context: Context
) {

    val listState = rememberLazyListState()

    var enableMainSwitch by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp),
        state = listState,
    ) {

        // 设置内容相关 item
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            ) {
                // 总开关
                SingleSwitchLine(
                    drawableRes = R.drawable.ic_main_switch,
                    text = stringResource(R.string.main_switch),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableMainSwitch,
                    onCheckedChange = {
                        Logger.i("总开关变化：${enableMainSwitch} to $it")
                        enableMainSwitch = it
                    }
                )
            }
        }

    }
}