package com.jackiepenghe.baselibraryforkotlin.popupwindow

import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.widget.PopupWindow

import com.jackiepenghe.baselibraryforkotlin.tools.BaseManager


/**
 * 自定义PopupWindow基类
 *
 * @author pengh
 */
@Suppress("unused")
abstract class BasePopupWindow @JvmOverloads constructor(contentView: View,
                                                         width: Int = 0,
                                                         height: Int = 0,
                                                         focusable: Boolean = false
) : PopupWindow(contentView, width, height, focusable) {

    private var root: View = contentView
    /**
     * 返回创建这个PopupWindow时的上下文
     *
     * @return 上下文
     */
    protected val context: Context
        get() = root.context

    init {
        BaseManager.handler.post {
            doBeforeInitOthers()
            initViews()
            initViewData()
            initOtherData()
            initEvents()
            doAfterAll()
        }

    }

    /*--------------------------------抽象函数--------------------------------*/

    /**
     * 在初始化其他数据之前执行的操作
     */
    protected abstract fun doBeforeInitOthers()

    /**
     * 初始化控件
     */
    protected abstract fun initViews()

    /**
     * 初始化控件的数据
     */
    protected abstract fun initViewData()

    /**
     * 初始化其他数据
     */
    protected abstract fun initOtherData()

    /**
     * 初始化事件
     */
    protected abstract fun initEvents()

    /**
     * 在最后执行的操纵
     */
    protected abstract fun doAfterAll()

    /*--------------------------------子类可用函数--------------------------------*/

    /**
     * 让PopupWindow可以和Activity一样拥有findViewById函数
     *
     * @param viewId 控件id
     * @return 控件
     */
    protected fun <T : View> findViewById(@IdRes viewId: Int): T {
        return root.findViewById(viewId)
    }
}
