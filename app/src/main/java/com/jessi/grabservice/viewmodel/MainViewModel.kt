package com.jessi.grabservice.viewmodel

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.AppInfoLoadStatus
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.model.HttpRsp
import com.jessi.grabservice.utils.Logger
import com.jessi.grabservice.utils.toHttpReq
import com.jessi.grabservice.utils.toHttpRsp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.sankokomi.wirebare.kernel.interceptor.http.HttpSession

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}

class MainViewModel() : ViewModel() {

    // 总开关
    private val _enableGrab = MutableStateFlow(false)
    val enableGrab = _enableGrab.asStateFlow()

    // 自动过滤
    private val _enableAutoFilter = MutableStateFlow(true)
    val enableAutoFilter = _enableAutoFilter.asStateFlow()

    // 休眠锁
    private val _enableHibernateLock = MutableStateFlow(false)
    val enableHibernateLock = _enableHibernateLock.asStateFlow()

    // 是否显示系统应用
    private val _filterSystemApp = MutableStateFlow(false)
    val filterSystemApp = _filterSystemApp.asStateFlow()

    // app 列表加载状态
    private val _appInfoLoadStatus = MutableStateFlow(AppInfoLoadStatus.NONE)
    val appInfoLoadStatus = _appInfoLoadStatus.asStateFlow()

    // app 信息列表
    val appInfoList = SnapshotStateList<AppInfo>()

    // 请求列表
    val requestList = SnapshotStateList<HttpReq>()
    // 相应列表
    val responseList = SnapshotStateList<HttpRsp>()

    fun prepareAppInfos(context: Context) {
        viewModelScope.launch {
            _appInfoLoadStatus.emit(AppInfoLoadStatus.LOADING)
            runCatching {
                val appInfos = context.packageManager.getInstalledApplications(
                    PackageManager.MATCH_UNINSTALLED_PACKAGES
                ).filter {
                    it != null
                }.map {
                    AppInfo(
                        appName = context.packageManager.getApplicationLabel(it).toString(),
                        packageName = it.packageName,
                        uid = it.uid,
                        isSystemApp = it.flags and ApplicationInfo.FLAG_SYSTEM != 0
                    )
                }
                Logger.i("prepareAppInfos: list size: ${appInfos.size}")
                appInfoList.clear()
                appInfoList.addAll(appInfos)
                _appInfoLoadStatus.emit(AppInfoLoadStatus.SUCCESS)
            }.onFailure {
                _appInfoLoadStatus.emit(AppInfoLoadStatus.FAILURE)
            }
        }
    }

    fun enableGrab(enable: Boolean) {
        _enableGrab.value = enable
    }

    fun enableAutoFilter(enable: Boolean) {
        _enableAutoFilter.value = enable
    }

    fun enableHibernateLock(enable: Boolean) {
        _enableHibernateLock.value = enable
    }

    fun updateAppInfo(index: Int, info: AppInfo) {
        appInfoList[index] = info
    }

    fun enableFilterSystemApp(enable: Boolean) {
        _filterSystemApp.value = enable
    }

    fun addRequest(session: HttpSession) {
        val request = session.toHttpReq()
        if (enableAutoFilter.value) {
            if (request.url.isNullOrBlank()) return
        }
        requestList.add(0, request)
    }

    fun addResponse(session: HttpSession) {
        val response = session.toHttpRsp()
        if (enableAutoFilter.value) {
            if (response.url.isNullOrBlank()) return
        }
        responseList.add(0, response)
    }

    fun clearRequest() {
        requestList.clear()
    }

    fun clearResponse() {
        responseList.clear()
    }

}