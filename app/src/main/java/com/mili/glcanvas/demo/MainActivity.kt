package com.mili.glcanvas.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mili.glcanvas.demo.extensions.isSupportEs2
import com.mili.glcanvas.demo.extensions.longToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isSupportEs2()) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            glSurfaceView.setRenderer(CanvasRender(this))
        } else {
            longToast("该设备不支持OpenGL ES2")
            return
        }
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
        gameView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
        gameView.onResume()
    }
}
