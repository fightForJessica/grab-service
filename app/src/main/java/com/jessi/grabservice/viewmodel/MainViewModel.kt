package com.jessi.grabservice.viewmodel

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jessi.grabservice.intent.MainIntent
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.utils.Logger

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}

class MainViewModel() : ViewModel() {

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
    }

}