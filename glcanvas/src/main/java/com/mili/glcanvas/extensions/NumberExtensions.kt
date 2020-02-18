package com.mili.glcanvas.extensions

import android.content.Context
import android.util.TypedValue

fun Number.dp2px(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
}