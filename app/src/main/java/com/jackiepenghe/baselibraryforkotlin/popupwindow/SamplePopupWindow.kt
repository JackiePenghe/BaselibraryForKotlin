package com.jackiepenghe.baselibraryforkotlin.popupwindow

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.jackiepenghe.baselibraryforkotlin.R

/**
 *
 * @author pengh
 * @date 2017/11/7
 * PopupWindow
 */

class SamplePopupWindow @JvmOverloads constructor(
        contentView: View,
        width: Int = 0,
        height: Int = 0,
        focusable: Boolean = false
) : BasePopupWindow(contentView, width, height, focusable) {

    private var button: Button? = null
    private var textView: TextView? = null

    override fun doBeforeInitOthers() {}

    override fun initViews() {
        button = findViewById(R.id.cancel_btn)
        textView = findViewById(R.id.popup_window_sample_tv)
    }

    @SuppressLint("SetTextI18n")
    override fun initViewData() {
        textView!!.text = "sample popup window"
    }

    override fun initOtherData() {

    }

    override fun initEvents() {
        button!!.setOnClickListener { dismiss() }
    }

    override fun doAfterAll() {}

    companion object {
        private val TAG = "SamplePopupWindow"
    }
}
