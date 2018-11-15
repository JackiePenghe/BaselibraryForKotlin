package com.jackiepenghe.baselibraryforkotlin.tools

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast

/**
 * 自定义Toast专用的Handler
 */
internal class ToastHandler
/*--------------------------------构造方法--------------------------------*/

/**
 * Default constructor associates this handler with the [Looper] for the
 * current thread.
 *
 *
 * If this thread does not have a looper, this handler won't be able to receive messages
 * so an exception is thrown.
 */
    (
    /*--------------------------------成员常量--------------------------------*/

    /**
     * 上下文
     */
    private val context: Context
) : Handler() {
    /**
     * Toast实例
     */
    private var toast: Toast? = null

    /*--------------------------------重写父类方法--------------------------------*/

    /**
     * Subclasses must implement this to receive messages.
     *
     * @param msg 信息
     */
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        val what = msg.what
        when (what) {
            MESSAGE -> showToast(msg)
            CANCEL -> hideToast()
            SET_RE_USE -> setReuse(msg)
            else -> {
            }
        }
    }

    /*--------------------------------私有方法--------------------------------*/

    /**
     * 设置是否重用未消失的Toast
     * @param msg Message消息
     */
    private fun setReuse(msg: Message) {
        val obj = (msg.obj ?: return) as? Boolean ?: return
        isReuse = obj
    }

    /**
     * 隐藏Toast
     */
    private fun hideToast() {
        if (toast != null) {
            Tool.warnOut(TAG, "hideToast")
            toast!!.cancel()
            toast = null
        }
    }

    /**
     * 显示Toast
     * @param msg Message消息
     */
    @SuppressLint("ShowToast")
    private fun showToast(msg: Message) {
        val obj = msg.obj
        val arg1 = msg.arg1
        if (obj == null) {
            return
        }
        if (obj !is String) {
            return
        }
        if (toast == null) {
            toast = Toast.makeText(context, obj, Toast.LENGTH_LONG)
        } else {
            if (isReuse) {
                toast!!.setText(obj)
            } else {
                if (arg1 != KEEP_TOAST) {
                    hideToast()
                }
                toast = Toast.makeText(context, obj, Toast.LENGTH_LONG)
            }
        }
        Tool.warnOut(TAG, "showToast")
        toast!!.show()
    }

    companion object {

        /*--------------------------------私有静态常量--------------------------------*/

        private val TAG = ToastHandler::class.java.simpleName

        /*--------------------------------库内静态常量--------------------------------*/

        /**
         * 显示Toast
         */
        const val MESSAGE = 1
        /**
         * 取消toast显示
         */
        const val CANCEL = 2
        /**
         * 设置是否重用上次未消失的Toast直接进行显示
         */
        const val SET_RE_USE = 3
        /**
         * 当前是否是用于保持Toast显示（超过3000秒时长的Toast）
         */
        const val KEEP_TOAST = 4
        /**
         * 当前是否为第一次弹出Toast
         */
        const val FIRST_SEND = 5
        /**
         * 是否重用上次还未消失的Toast
         */
        var isReuse = false
            private set
    }
}
