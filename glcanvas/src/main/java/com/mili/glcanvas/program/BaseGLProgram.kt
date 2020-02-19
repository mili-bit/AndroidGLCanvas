package com.mili.glcanvas.program

import android.content.Context
import android.graphics.Paint
import android.opengl.GLES20
import android.opengl.Matrix
import com.mili.glcanvas.extensions.glBlue
import com.mili.glcanvas.extensions.glGreen
import com.mili.glcanvas.extensions.glRed
import com.mili.glcanvas.glcanvas.R
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BaseGLProgram(context: Context, width: Int, height: Int) :
    GLProgram(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private const val STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    private var aPositionLocation: Int = 0
    private var aColorLocation: Int = 0
    private var uMatrixLocation: Int = 0
    private var modelMatrix = FloatArray(16)

    init {
        // 生成模型矩阵
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 2f / width.toFloat(), -2f / height.toFloat(), 1f)
        Matrix.translateM(modelMatrix, 0, -width.toFloat() / 2, -height.toFloat() / 2f, 0f)
        // 定位a_Color、a_Position、uMatrix属性
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
    }

    fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        //启用GL程序
        GLES20.glUseProgram(program)
        // 正交投影
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0)

        val red = paint.glRed()
        val green = paint.glGreen()
        val blue = paint.glBlue()
        val vertices = floatArrayOf(
            // X, Y, R, G, B
            left, top, red, green, blue,
            left, bottom, red, green, blue,
            right, bottom, red, green, blue,
            right, top, red, green, blue,
            left, top, red, green, blue
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
        GLES20.glEnableVertexAttribArray(aColorLocation)
        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 5)
        GLES20.glDisableVertexAttribArray(aPositionLocation)
        GLES20.glDisableVertexAttribArray(aColorLocation)
    }

    fun drawLine(startX: Float, startY: Float, stopX: Float, stopY: Float, paint: Paint) {
        //启用GL程序
        GLES20.glUseProgram(program)
        // 定位a_Color、a_Position、uMatrix属性
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        // 正交投影
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0)

        val red = paint.glRed()
        val green = paint.glGreen()
        val blue = paint.glBlue()
        val vertices = floatArrayOf(
            // X, Y, R, G, B
            startX, startY, red, green, blue,
            stopX, stopY, red, green, blue
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
        GLES20.glEnableVertexAttribArray(aColorLocation)
        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2)
        GLES20.glDisableVertexAttribArray(aPositionLocation)
        GLES20.glDisableVertexAttribArray(aColorLocation)
    }

    override fun release() {

    }
}