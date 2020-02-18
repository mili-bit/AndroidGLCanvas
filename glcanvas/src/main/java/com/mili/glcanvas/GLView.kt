package com.mili.glcanvas

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.mili.glcanvas.extensions.isSupportEs2
import com.mili.glcanvas.extensions.longToast
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class GLView : GLSurfaceView, GLSurfaceView.Renderer {

    private var glCanvas: GLCanvas? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (context.isSupportEs2()) {
            setEGLContextClientVersion(2)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            setRenderer(this)
        } else {
            context.longToast("该设备不支持OpenGL ES2")
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glCanvas = GLCanvas(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glCanvas?.setSize(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glCanvas?.let {
            it.clearBuffer()
            onGLDraw(it)
        }
    }

    abstract fun onGLDraw(canvas: GLCanvas)

}