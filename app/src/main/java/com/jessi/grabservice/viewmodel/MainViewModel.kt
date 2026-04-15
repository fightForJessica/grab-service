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

    private val _appInfoLoadStatus = MutableStateFlow(AppInfoLoadStatus.NONE)
    val appInfoLoadStatus = _appInfoLoadStatus.asStateFlow()

    private val _filterSystemApp = MutableStateFlow(false)
    val filterSystemApp = _filterSystemApp.asStateFlow()

    val appInfoList = SnapshotStateList<AppInfo>()

    val requestList = SnapshotStateList<HttpReq>()
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

    fun updateAppInfo(index: Int, info: AppInfo) {
        appInfoList[index] = info
    }

    fun enableFilterSystemApp(enable: Boolean) {
        _filterSystemApp.value = enable
    }

    fun addRequest(session: HttpSession) {
        requestList.add(0, session.toHttpReq())
    }

    fun addResponse(session: HttpSession) {
        responseList.add(0, session.toHttpRsp())
    }

}