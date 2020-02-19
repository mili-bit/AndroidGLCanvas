package com.mili.glcanvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.opengl.GLES20
import com.mili.glcanvas.program.BaseGLProgram
import com.mili.glcanvas.program.TextureGLProgram

class GLCanvas(private val context: Context) {

    private var width: Int = 0
    private var height: Int = 0
    private var baseGLProgram: BaseGLProgram? = null
    private var textureGLProgram: TextureGLProgram? = null

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES20.glViewport(0, 0, width, height)
    }

    fun clearBuffer() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    private fun assembleBaseGLProgram(): BaseGLProgram? {
        if (baseGLProgram == null) {
            baseGLProgram =
                BaseGLProgram(context, width, height)
        }
        return baseGLProgram
    }

    private fun assembleTextureGLProgram(): TextureGLProgram? {
        if (textureGLProgram == null) {
            textureGLProgram = TextureGLProgram(
                context,
                width,
                height
            )
        }
        return textureGLProgram
    }

    fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        assembleBaseGLProgram()?.drawRect(left, top, right, bottom, paint)
    }

    fun drawLine(startX: Float, startY: Float, stopX: Float, stopY: Float, paint: Paint) {
        assembleBaseGLProgram()?.drawLine(startX, startY, stopX, stopY, paint)
    }

    fun drawBitmap(bitmap: Bitmap, left: Float, top: Float) {
        assembleTextureGLProgram()?.drawBitmap(bitmap, left, top)
    }

    fun release(){
        baseGLProgram?.release()
        textureGLProgram?.release()
    }
}