package com.mili.glcanvas.utils

import android.util.Log
import com.mili.glcanvas.glcanvas.BuildConfig

object Logger {
    var ON = BuildConfig.DEBUG
    private const val TAG = "GLL"

    fun i(subTag: String = "", message: String, throwable: Throwable? = null) {
        if (ON) {
            Log.i(subTag.realTag(), message, throwable)
        }
    }

    fun d(subTag: String = "", message: String, throwable: Throwable? = null) {
        if (ON) {
            Log.d(subTag.realTag(), message, throwable)
        }
    }

    fun w(subTag: String = "", message: String, throwable: Throwable? = null) {
        if (ON) {
            Log.w(subTag.realTag(), message, throwable)
        }
    }

    fun e(subTag: String = "", message: String, throwable: Throwable? = null) {
        if (ON) {
            Log.e(subTag.realTag(), message, throwable)
        }
    }

    private fun String.realTag(): String {
        return if (isBlank() || isEmpty()) {
            TAG
        } else {
            "$TAG-$this"
        }
    }
}