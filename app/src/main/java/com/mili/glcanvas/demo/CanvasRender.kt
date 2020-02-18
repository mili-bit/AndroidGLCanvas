package com.mili.glcanvas.demo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.mili.glcanvas.demo.extensions.dp2px
import com.mili.glcanvas.demo.extensions.readResourceText
import com.mili.glcanvas.demo.utils.compileFragmentShader
import com.mili.glcanvas.demo.utils.compileVertexShader
import com.mili.glcanvas.demo.utils.linkProgram
import com.mili.glcanvas.demo.utils.validateProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CanvasRender(private val context: Context) : GLSurfaceView.Renderer {

    companion object {
        private const val BYTES_PER_FLOAT = 4
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private const val STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

        private const val A_POSITION = "a_Position"
        private const val A_COLOR = "a_Color"
        private const val U_MATRIX = "u_Matrix"
    }

    private val vertices = floatArrayOf(
        // X, Y, R, G, B
        -2.5f, -1f, 1f, 0f, 0f,
        2.5f, -1f, 0f, 1f, 0f,
        2.5f, 1f, 0f, 0f, 1f,
        -2.5f, 1f, 1f, 1f, 0f
    )
    private val vertexData = ByteBuffer
        .allocateDirect(vertices.size * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()

    init {
        vertexData.put(vertices)
    }

    private var program: Int = 0
    private var aPositionLocation: Int = 0
    private var aColorLocation: Int = 0
    private var uMatrixLocation: Int = 0
    private var projectionMatrix = FloatArray(16)
    private var modelMatrix = FloatArray(16)
    private var width: Int = 0
    private var height: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        // 读取顶点着色器、片段着色器程序
        val vertexShaderSource = context.readResourceText(R.raw.simple_vertex_shader)
        val fragmentShaderSource = context.readResourceText(R.raw.simple_fragment_shader)
        // 编译着色器
        val vertexShader = compileVertexShader(vertexShaderSource)
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        // 链接着色器为程序
        program = linkProgram(vertexShader, fragmentShader)
        if (BuildConfig.DEBUG) {
            validateProgram(program)
        }
        // 使用程序
        GLES20.glUseProgram(program)
        // 定位a_Color、a_Position
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        GLES20.glEnableVertexAttribArray(aColorLocation)
        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        this.width = width
        this.height = height
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 2f / width.toFloat(), -2f / height.toFloat(), 1f)
        Matrix.translateM(modelMatrix, 0, -width.toFloat() / 2, -height.toFloat() / 2f, 0f)
        System.arraycopy(modelMatrix, 0, projectionMatrix, 0, modelMatrix.size)
        if (width > height) {
            // val aspectRatio = width.toFloat() / height.toFloat()
            // Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
            // Matrix.scaleM(modelMatrix, 0, 1f / height, 1f / height.toFloat(), 1f)
        } else {
            // val aspectRatio = height.toFloat() / width.toFloat()
            // Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
            // Matrix.scaleM(modelMatrix, 0, 1f / width.toFloat(), 1f / width.toFloat(), 1f)
        }
        // val temp = FloatArray(16)
        // Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        // System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
        // drawSomething()
        // 填充顶点数据
        // vertexData.position(0)
        // GLES20.glVertexAttribPointer(
        //     aPositionLocation,
        //     POSITION_COMPONENT_COUNT,
        //     GLES20.GL_FLOAT,
        //     false,
        //     STRIDE,
        //     vertexData
        // )
        // // 填充颜色数据
        // vertexData.position(POSITION_COMPONENT_COUNT)
        // GLES20.glVertexAttribPointer(
        //     aColorLocation,
        //     COLOR_COMPONENT_COUNT,
        //     GLES20.GL_FLOAT,
        //     false,
        //     STRIDE,
        //     vertexData
        // )
        // 正交投影
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        drawSomething()
    }

    var step = 2f
    private val size = 100.dp2px(context)
    var left = 0f
    var top = 0f
    var right = size
    var bottom = size

    fun drawSomething() {
        val vertices = floatArrayOf(
            // X, Y, R, G, B
            left, top, 1f, 0f, 0f,
            left, bottom, 0f, 1f, 0f,
            right, bottom, 0f, 0f, 1f,
            right, top, 1f, 1f, 0f,
            left, top, 1f, 0f, 0f
        )
        val vertexData = ByteBuffer
            .allocateDirect(vertices.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
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
    }
}