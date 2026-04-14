package com.jessi.grabservice.ui.page

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.jessi.grabservice.ui.DividerLine
import com.jessi.grabservice.ui.DoubleSwitchLine
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
    var enableAutoFilter by remember { mutableStateOf(false) }
    var enableHibernateLock by remember { mutableStateOf(false) }
    var enableShowSystemApp by remember { mutableStateOf(false) }
    var enableAllSelect by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 设置内容相关 item
        item(
            key = "control_setting",
            contentType = "control_setting"
        ) {
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
                        Logger.i("总开关: $enableMainSwitch -> $it")
                        enableMainSwitch = it
                    }
                )

                DividerLine()

                // 自动过滤
                DoubleSwitchLine(
                    drawableRes = R.drawable.ic_filter,
                    firstText = stringResource(R.string.auto_filter),
                    secondText = stringResource(R.string.filter_content),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableAutoFilter,
                    onCheckedChange = {
                        Logger.i("自动过滤开关: $enableAutoFilter -> $it")
                        enableAutoFilter = it
                    }
                )

                DividerLine()

                // 休眠锁
                DoubleSwitchLine(
                    drawableRes = R.drawable.ic_hibernate_lock,
                    firstText = stringResource(R.string.hibernate_lock),
                    secondText = stringResource(R.string.hibernate_lock_content),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableHibernateLock,
                    onCheckedChange = {
                        Logger.i("休眠锁开关: $enableHibernateLock -> $it")
                        enableHibernateLock = it
                    }
                )
            }
        }

        item(
            key = "control_app_select",
            contentType = "control_app_select"
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            ) {
                // 显示系统应用
                SingleSwitchLine(
                    drawableRes = R.drawable.ic_system_app,
                    text = stringResource(R.string.show_system_app),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableShowSystemApp,
                    onCheckedChange = {
                        Logger.i("显示系统应用: $enableShowSystemApp -> $it")
                        enableShowSystemApp = it
                    }
                )

                DividerLine()

                // 全选
                SingleSwitchLine(
                    drawableRes = R.drawable.ic_all_select,
                    text = stringResource(R.string.all_select),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableAllSelect,
                    onCheckedChange = {
                        Logger.i("全选: $enableAllSelect -> $it")
                        enableAllSelect = it
                    }
                )
            }
        }

        // app 选择列表
        item(
            key = "app_detail_list",
            contentType = "app_detail_list"
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState,
                ) {
//                    items(
//
//                    )
                }
            }
        }

    }
}