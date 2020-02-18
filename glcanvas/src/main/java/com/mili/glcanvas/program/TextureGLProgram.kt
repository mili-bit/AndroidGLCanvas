package com.mili.glcanvas.program

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import com.mili.glcanvas.extensions.toTexture
import com.mili.glcanvas.glcanvas.R
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TextureGLProgram(context: Context, width: Int, height: Int) :
    GLProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val TEXTURE_COMPONENT_COUNT = 2
        private const val STRIDE =
            (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_PER_FLOAT

    }

    private var aPositionLocation: Int = 0
    private var uMatrixLocation: Int = 0
    private var aTextureCoordinatesLocation: Int = 0
    private var uTextureUnitLocation: Int = 0
    private var modelMatrix = FloatArray(16)

    init {
        // 生成模型矩阵
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 2f / width.toFloat(), -2f / height.toFloat(), 1f)
        Matrix.translateM(modelMatrix, 0, -width.toFloat() / 2, -height.toFloat() / 2f, 0f)
        // 检索着色器程序 Uniform 位置
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTUREUNIT)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        // 检索着色器程序 Attribute 位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURECOORDINATES)
    }

    fun drawBitmap(bitmap: Bitmap, left: Float, top: Float) {
        //启用GL程序
        GLES20.glUseProgram(program)
        // 纹理绑定
        val textureId = bitmap.toTexture()
        // 告诉纹理均匀采样器在着色器中使用这个纹理，告诉它从纹理单元0读取
        GLES20.glUniform1i(uTextureUnitLocation, 0)
        // 正交投影
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0)

        val bw = bitmap.width
        val bh = bitmap.height
        val vertices = floatArrayOf(
            // X, Y, S, T
            left, top, 0f, 0f,
            left, top + bh, 0f, 1f,
            left + bw, top + bh, 1f, 1f,
            left + bw, top, 1f, 0f,
            left, top, 0f, 0f
        )
        val vertexData = ByteBuffer
            .allocateDirect(vertices.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
        // 填充顶点数据
        GLES20.glEnableVertexAttribArray(aPositionLocation)
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        // 填充颜色数据
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation)
        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aTextureCoordinatesLocation,
            TEXTURE_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 5)
        GLES20.glDisableVertexAttribArray(aPositionLocation)
        GLES20.glDisableVertexAttribArray(aTextureCoordinatesLocation)
        // 解绑纹
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
    }
}