package com.mili.glcanvas.extensions

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.mili.glcanvas.utils.Logger

private const val TAG = "GLExt"

fun Bitmap.toTexture(): Int {
    if (this.isRecycled) {
        Logger.w(TAG, "The bitmap to generate texture is recycled.")
        return 0
    }
    // 生成纹理对象
    val textureObjectIds = IntArray(1)
    GLES20.glGenTextures(1, textureObjectIds, 0)
    if (textureObjectIds[0] == 0) {
        // 纹理生成失败
        Logger.w(TAG, "Could not generate a new OpenGL texture object.")
        return 0
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
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, this, 0)
    GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
    // 将纹理单元0设为活动纹理单元
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    return textureObjectIds[0]
}