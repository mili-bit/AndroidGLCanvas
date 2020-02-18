package com.mili.glcanvas.demo.utils

import android.content.Context

fun readTextFileFromResource(context: Context, rawId: Int): String {
    return context.resources.openRawResource(rawId).use {
        it.reader().readText()
    }
}