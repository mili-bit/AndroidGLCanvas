package com.mili.glcanvas.program

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.mili.glcanvas.utils.Logger

class BitmapTexture(var bitmap: Bitmap?) {

    companion object {
        private const val TAG = "BitmapTexture"
    }

    var textureId: Int = 0

    fun uploadTexture() {
        if (textureId > 0 || bitmap == null || bitmap!!.isRecycled) {
            return
        }
        // 生成纹理对象
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            // 纹理生成失败
            textureId = 0
            Logger.w(TAG, "Could not generate a new OpenGL texture object.")
            return
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
        // 暂时解绑纹理，用时再绑定
        // GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        textureId = textureObjectIds[0]
    }

    fun activeTexture() {
        // 绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        // 将纹理单元0设为活动纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    }

    fun inactiveTexture() {
        // 解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        // 将纹理单元0设为活动纹理单元
        // GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    }

    fun finalize() {
        Logger.d(TAG, "Bitmap texture finalize.")
        GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
        if (bitmap?.isRecycled == false) {
            bitmap?.recycle()
        }
        bitmap = null
    }
}