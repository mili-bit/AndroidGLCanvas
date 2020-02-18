package com.mili.glcanvas.program

import android.content.Context
import com.mili.glcanvas.extensions.readResourceText
import com.mili.glcanvas.utils.buildProgram

abstract class GLProgram(
    context: Context,
    vertexShaderResId: Int,
    fragmentShaderResId: Int
) {

    companion object {
        const val BYTES_PER_FLOAT = 4
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val U_MATRIX = "u_Matrix"
        const val A_TEXTURECOORDINATES = "a_TextureCoordinates"
        const val U_TEXTUREUNIT = "u_TextureUnit"
    }

    protected var program: Int = buildProgram(
        context.readResourceText(vertexShaderResId),
        context.readResourceText(fragmentShaderResId)
    )

}