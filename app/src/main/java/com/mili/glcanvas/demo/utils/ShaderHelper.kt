package com.mili.glcanvas.demo.utils

import android.opengl.GLES20

private const val TAG = "ShaderHelper"

fun compileVertexShader(source: String): Int {
    return compileShader(GLES20.GL_VERTEX_SHADER, source)
}

fun compileFragmentShader(source: String): Int {
    return compileShader(GLES20.GL_FRAGMENT_SHADER, source)
}

private fun compileShader(type: Int, source: String): Int {
    val shaderObjectId = GLES20.glCreateShader(type)
    if (shaderObjectId == 0) {
        Logger.w(TAG, "Could not create new shader.")
        return 0
    }
    GLES20.glShaderSource(shaderObjectId, source)
    GLES20.glCompileShader(shaderObjectId)
    val compileStatus = IntArray(1)
    GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
    Logger.d(
        TAG,
        "Result of compiling source: \n" +
                "$shaderObjectId\n"
                + GLES20.glGetShaderInfoLog(shaderObjectId)
    )
    if (compileStatus[0] == 0) {
        GLES20.glDeleteShader(shaderObjectId)
        Logger.w(TAG, "Compilation of shader failed.")
        return 0
    }
    return shaderObjectId
}

fun linkProgram(vertexShader: Int, fragmentShader: Int): Int {
    val program = GLES20.glCreateProgram()
    if (program == 0) {
        Logger.w(TAG, "Could not create new program.")
        return 0
    }
    GLES20.glAttachShader(program, vertexShader)
    GLES20.glAttachShader(program, fragmentShader)
    GLES20.glLinkProgram(program)
    val linkStatus = IntArray(1)
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
    Logger.d(TAG, "Result of linking program:\n${GLES20.glGetProgramInfoLog(program)}")
    if (linkStatus[0] == 0) {
        GLES20.glDeleteProgram(program)
        Logger.w(TAG, "Linking of program failed.")
        return 0
    }
    return program
}

fun validateProgram(program: Int): Boolean {
    GLES20.glValidateProgram(program)
    val validateStatus = IntArray(1)
    GLES20.glGetProgramiv(program, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
    Logger.d(TAG, "Result of validating program:\n${GLES20.glGetProgramInfoLog(program)}")
    return validateStatus[0] != 0
}