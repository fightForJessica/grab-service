package com.jessi.grabservice.viewmodel

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jessi.grabservice.intent.MainIntent
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.AppInfoLoadStatus
import com.jessi.grabservice.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}

class MainViewModel() : ViewModel() {

    private val _appInfoLoadStatus = MutableStateFlow(AppInfoLoadStatus.NONE)
    val appInfoLoadStatus = _appInfoLoadStatus.asStateFlow()
    val appInfoList = SnapshotStateList<AppInfo>()

    // 处理 ui 内部事件
//    private val _effect = MutableSharedFlow<MainEffect>()
//    val effect = _effect.asSharedFlow()
//
//
//    fun sendEffect(effect: MainEffect) {
//        viewModelScope.launch {
//            Logger.i("sendMainEffect: $effect")
//            _effect.emit(effect)
//        }
//    }

    fun handleIntent(intent: MainIntent) {
        Logger.i("handleMainIntent: $intent")
        when (intent) {

            else -> throw IllegalArgumentException("unknow intent: $intent")
        }
    }

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
                        context.packageManager.getApplicationLabel(it).toString(),
                        it.packageName,
                        it.flags and ApplicationInfo.FLAG_SYSTEM != 0
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

}