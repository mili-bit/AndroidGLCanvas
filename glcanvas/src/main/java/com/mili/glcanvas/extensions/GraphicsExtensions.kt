package com.mili.glcanvas.extensions

import android.graphics.Color
import android.graphics.Paint

fun Paint.glRed(): Float {
    return this.color.run {
        Color.red(this).toFloat() / 255f
    }
}

fun Paint.glGreen(): Float {
    return this.color.run {
        Color.green(this).toFloat() / 255f
    }
}

fun Paint.glBlue(): Float {
    return this.color.run {
        Color.blue(this).toFloat() / 255f
    }
}