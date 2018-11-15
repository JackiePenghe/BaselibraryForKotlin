package com.jackiepenghe.baselibraryforkotlin.activities.sample

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.popupwindow.SamplePopupWindow
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

class SampleBasePopupWindowActivity : AppCompatActivity() {
    private var button: Button? = null
    private var content: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_base_popup_window)
        content = findViewById(R.id.content_sample_popupWindow)
        button = findViewById(R.id.show_popup_window_btn)
        button!!.setOnClickListener { showPopupWindow() }
    }

    private fun showPopupWindow() {
        Tool.warnOut(TAG, "弹出popupWindow")
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        Tool.warnOut(TAG, "point.x = " + point.x + ",point.y = " + point.y)
        val view = View.inflate(this, R.layout.popup_window_sample, null)
        val samplePopupWindow = SamplePopupWindow( view, point.x * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT)
        samplePopupWindow.isFocusable = true
        samplePopupWindow.isOutsideTouchable = false
        samplePopupWindow.showAtLocation(
            content,
            Gravity.CENTER,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {

        private const val TAG = "SampleBasePopupWindowAc"
    }
}
