package com.mili.glcanvas.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.mili.glcanvas.demo.extensions.dp2px
import com.mili.glcanvas.GLCanvas
import com.mili.glcanvas.GLView

class GameView : GLView {

    private var step = 2f
    private val size = 100.dp2px(context)
    private var left = 0f
    private var top = 0f
    private var right = size
    private var bottom = size
    private val paint = Paint().apply {
        color = Color.RED
    }
    private lateinit var bitmap: Bitmap

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.orange,
            BitmapFactory.Options().apply { inScaled = false })
    }

    override fun onGLDraw(canvas: GLCanvas) {
        paint.color = Color.RED
        canvas.drawRect(left, top, right, bottom, paint)
        left += step
        top += step
        right += step
        bottom += step
        if (right > 3 * size) {
            step = -2f
            left = 2 * size
            top = 2 * size
            right = 3 * size
            bottom = 3 * size
        } else if (left < 0) {
            step = 2f
            left = 0f
            top = 0f
            right = size
            bottom = size
        }
        paint.color = Color.WHITE
        canvas.drawLine(left, top + size / 2, right, top + size / 2, paint)
        canvas.drawBitmap(bitmap, (measuredWidth - bitmap.width).toFloat(), 0f)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bitmap.recycle()
    }
}