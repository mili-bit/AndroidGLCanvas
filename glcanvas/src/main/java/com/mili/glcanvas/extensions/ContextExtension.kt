package com.mili.glcanvas.extensions

import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import java.io.IOException

/**检查是否支持OpenGL ES 2*/
fun Context.isSupportEs2(): Boolean {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).run {
        deviceConfigurationInfo.reqGlEsVersion >= 0x20000
    }
}

fun Context.shortToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.shortToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Context.longToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.readResourceText(resourceId: Int): String {
    try {
        return resources.openRawResource(resourceId).use {
            it.reader().readText()
        }
    } catch (e: Resources.NotFoundException) {
        throw RuntimeException("Resource not found: $resourceId")
    } catch (e: IOException) {
        throw RuntimeException("Could not open resource: $resourceId")
    }
}