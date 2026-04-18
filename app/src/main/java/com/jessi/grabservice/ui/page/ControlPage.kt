package com.jessi.grabservice.ui.page

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.R
import com.jessi.grabservice.model.AppInfoLoadStatus
import com.jessi.grabservice.proxy.ProxyHelper
import com.jessi.grabservice.ui.DividerLine
import com.jessi.grabservice.ui.page.widget.DoubleSwitchLine
import com.jessi.grabservice.ui.page.widget.SingleSwitchLine
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.lowerHalfConerBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.ui.upperHalfConerBackground
import com.jessi.grabservice.utils.Logger
import com.jessi.grabservice.utils.getAppIconDrawable
import com.jessi.grabservice.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.sankokomi.wirebare.kernel.common.WireBareHelper

const val CONTROL_PAGE_NAME = "控制面板"

interface IControlPageCallback {
    fun onMainSwitchSelect(select: Boolean)
    fun onHibernateLockSelect(select: Boolean)
}

@Composable
fun ControlPage(
    context: Context,
    viewModel: MainViewModel,
    proxyHelper: ProxyHelper,
    callback: IControlPageCallback,
) {

    val listState = rememberLazyListState()

    val enableGrab by viewModel.enableGrab.collectAsState()
    val enableAutoFilter by viewModel.enableAutoFilter.collectAsState()
    val enableSSL by viewModel.enableSSL.collectAsState()
    val enableHibernateLock by viewModel.enableHibernateLock.collectAsState()
    val enableShowSystemApp by viewModel.filterSystemApp.collectAsState()
    var enableAllSelect by remember { mutableStateOf(false) }

    val appInfoLoadStatus by viewModel.appInfoLoadStatus.collectAsState()
    val targetAppInfos = if (enableShowSystemApp) {
        viewModel.appInfoList.filter { it.isSystemApp }
    } else {
        viewModel.appInfoList
    }

    LaunchedEffect(Unit) {
        viewModel.prepareAppInfos(context)
    }

    // 检测证书是否可信
    var systemTrustCert by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (enableSSL) {
            withContext(Dispatchers.IO){
                systemTrustCert = WireBareHelper.checkSystemTrustCert(proxyHelper.wireBareJKS)
            }
        }
    }


    val checkEnableAllSelect: (() -> Boolean) = {
        var isAllSelect = targetAppInfos.isNotEmpty()
        if (isAllSelect) {
            for (info in targetAppInfos) {
                if (!info.isGrabSelected) {
                    isAllSelect = false
                    break
                }
            }
        }
        isAllSelect
    }

    LaunchedEffect(targetAppInfos) {
        enableAllSelect = checkEnableAllSelect()
    }

    LaunchedEffect(listState.isScrollInProgress) {
        Logger.i("ControlPage scrolling: ${listState.isScrollInProgress}")
        viewModel.setMainPageScrolling(listState.isScrollInProgress)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        state = listState,
        contentPadding = PaddingValues(
            top = dimensionResource(R.dimen.title_bar_height),
            bottom = dimensionResource(R.dimen.tab_layout_height)
        )
    ) {

        // 设置内容相关 item
        item(
            key = "control_setting",
            contentType = "control_setting"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            ) {
                // 总开关
                SingleSwitchLine(
                    drawableRes = R.drawable.ic_main_switch,
                    text = stringResource(R.string.main_switch),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableGrab,
                    onCheckedChange = {
                        Logger.i("总开关: $enableGrab -> $it")
                        callback.onMainSwitchSelect(it)
                    }
                )

                DividerLine()

                // 自动过滤
                DoubleSwitchLine(
                    enable = !enableGrab,
                    drawableRes = R.drawable.ic_filter,
                    firstText = stringResource(R.string.auto_filter),
                    secondText = stringResource(R.string.filter_content),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableAutoFilter,
                    onCheckedChange = {
                        Logger.i("自动过滤开关: $enableAutoFilter -> $it")
                        viewModel.enableAutoFilter(it)
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
                        viewModel.enableHibernateLock(it)
                        callback.onHibernateLockSelect(it)
                    }
                )

                DividerLine()

                // SSL/TLS
                DoubleSwitchLine(
                    enable = !enableGrab,
                    drawableRes = R.drawable.ic_ssl,
                    firstText = stringResource(R.string.common_ssl),
                    secondText = if (enableSSL && !systemTrustCert) {
                        stringResource(R.string.ssl_warning)
                    } else {
                        stringResource(R.string.ssl_content)
                    },
                    secondTextColor = if (enableSSL && !systemTrustCert) {
                        ThemeManager.colorTheme.errorText
                    } else {
                        ThemeManager.colorTheme.secondText
                    },
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableSSL,
                    onCheckedChange = {
                        Logger.i("启用SSL/TLS: $enableSSL -> $it")
                        viewModel.enableSSL(it)
                    }
                )
            }
        }

        item(
            key = "control_app_select",
            contentType = "control_app_select"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
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
                        viewModel.enableFilterSystemApp(it)
                    }
                )

                DividerLine()

                // 全选
                SingleSwitchLine(
                    enable = !enableGrab,
                    drawableRes = R.drawable.ic_all_select,
                    text = stringResource(R.string.all_select),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor),
                    checked = enableAllSelect,
                    onCheckedChange = {
                        Logger.i("全选: $enableAllSelect -> $it")
                        // 只显示系统应用时，全选只对系统应用生效；不只显示系统应用时，全选对所有应用生效
                        viewModel.appInfoList.forEachIndexed { index, info ->
                            if (info.isSystemApp || !enableShowSystemApp) {
                                viewModel.updateAppInfo(index, info.copy(isGrabSelected = it))
                            }
                        }
                        enableAllSelect = it
                    }
                )
            }
        }

        when (appInfoLoadStatus) {
            AppInfoLoadStatus.LOADING -> {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .height(2.dp)
                    )
                }
            }
            AppInfoLoadStatus.FAILURE -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
                            .padding(16.dp)
                            .clickable {
                                viewModel.prepareAppInfos(context)
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.reload_app_infos),
                            fontSize = 18.sp,
                            color = ThemeManager.colorTheme.globalText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            AppInfoLoadStatus.SUCCESS -> {
                item(
                    key = "control_app_select_warning",
                    contentType = "control_app_select_warning"
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        visible = enableGrab,
                        enter = fadeIn(tween()),
                        exit = fadeOut(tween())
                    ) {
                        Text(
                            text = stringResource(R.string.change_app_select_while_switch_off),
                            color = ThemeManager.colorTheme.tipText,
                            fontSize = 16.sp,
                            maxLines = 2
                        )
                    }
                }

                if (targetAppInfos.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.without_load_app_infos_permission),
                                fontSize = 16.sp,
                                color = ThemeManager.colorTheme.globalText
                            )
                        }
                    }
                } else {
                    // app 选择列表
                    items(
                        count = targetAppInfos.size,
                        key = { "control_app_list:${targetAppInfos[it].appName}" },
                        contentType = { "control_app_list" }
                    ) { index ->
                        val appInfo = targetAppInfos[index]
                        val modifier = when (index) {
                            0 -> {
                                if (targetAppInfos.size == 1) {
                                    Modifier.cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
                                } else {
                                    Modifier.upperHalfConerBackground(ThemeManager.colorTheme.cardBackgroundColor)
                                }
                            }
                            targetAppInfos.size - 1 -> {
                                Modifier.lowerHalfConerBackground(ThemeManager.colorTheme.cardBackgroundColor)
                            }
                            else -> {
                                Modifier.background(ThemeManager.colorTheme.cardBackgroundColor)
                            }
                        }

                        DoubleSwitchLine(
                            modifier = modifier,
                            enable = !enableGrab, // 开始抓取后不能变更 app 选择状态
                            firstText = appInfo.appName,
                            secondText = appInfo.packageName,
                            drawable = context.getAppIconDrawable(appInfo.packageName),
                            checked = appInfo.isGrabSelected,
                            onCheckedChange = {
                                viewModel.updateAppInfo(index, appInfo.copy(isGrabSelected = it))
                                // 单个 app 选择状态切换后更新全选状态
                                enableAllSelect = checkEnableAllSelect()
                            }
                        )
                    }
                }
            }
            else -> {
                // do nothing
            }
        }
    }
}
