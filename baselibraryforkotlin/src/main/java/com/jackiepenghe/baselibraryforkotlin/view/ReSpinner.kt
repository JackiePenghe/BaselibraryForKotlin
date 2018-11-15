package com.jackiepenghe.baselibraryforkotlin.view

import android.content.Context
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * 可重复选择同一选项并触发监听的Spinner
 *
 * @author jackie
 */
class ReSpinner : AppCompatSpinner {

    /*---------------------------------------成员变量---------------------------------------*/

    /**
     * 标志下拉列表是否正在显示
     */
    /*---------------------------------------公开方法---------------------------------------*/

    var isDropDownMenuShown = false

    /*---------------------------------------构造方法---------------------------------------*/

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    /*---------------------------------------重写父类方法---------------------------------------*/

    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position, animate)
        if (sameSelected) {
            val onItemSelectedListener = onItemSelectedListener
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun performClick(): Boolean {
        this.isDropDownMenuShown = true
        return super.performClick()
    }

    override fun setSelection(position: Int) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position)
        if (sameSelected) {
            val onItemSelectedListener = onItemSelectedListener
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}
