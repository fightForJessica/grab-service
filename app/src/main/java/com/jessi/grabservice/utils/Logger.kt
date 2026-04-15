package com.jessi.grabservice.utils

import android.util.Log

object Logger {

    private const val DEFAULT_TAG = "Logger-Default"

    private const val ENABLE = true

    fun i(msg: String, tag: String = DEFAULT_TAG) {
        if (ENABLE) {
            Log.i(tag, msg)
        }
    }

    fun w(msg: String, tag: String = DEFAULT_TAG) {
        if (ENABLE) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String, tag: String = DEFAULT_TAG, throwable: Throwable? = null) {
        if (ENABLE) {
            Log.e(tag, msg, throwable)
        }
    }

}