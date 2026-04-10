package com.jessi.grabservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jessi.grabservice.intent.MainIntent
import com.jessi.grabservice.utils.Logger

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}

class MainViewModel() : ViewModel() {

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

}