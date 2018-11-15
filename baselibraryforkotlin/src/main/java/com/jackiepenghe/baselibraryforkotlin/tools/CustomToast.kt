package com.jackiepenghe.baselibraryforkotlin.tools

import android.annotation.SuppressLint
import android.content.Context
import android.os.Message
import android.support.annotation.StringRes


import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * 自定义Toast，可实现自定义显示时间,兼容至安卓N及以上版本
 */
internal class CustomToast private constructor(

    private val context: Context,
    private var messageText: String?,
    private var duration: Int
) {

    /*--------------------------------公开方法--------------------------------*/

    /**
     * 显示吐司
     */
    fun show() {
        showMyToast(context, messageText, duration)
    }

    companion object {

        /**
         * 长时间的吐司持续时间
         */
        const val LENGTH_LONG = 3500
        /**
         * 短时间的吐司持续时间
         */
        const val LENGTH_SHORT = 2000

        /*--------------------------------静态变量--------------------------------*/

        /**
         * 自定义吐司本类单例
         */
        @SuppressLint("StaticFieldLeak")
        private var customToast: CustomToast? = null

        /**
         * 吐司专用的handler(使用Handler可以避免定时器在非主线程中导致的线程问题)
         */
        @SuppressLint("StaticFieldLeak")
        private var toastHandler: ToastHandler? = null
        /**
         * 是否重用上次未消失的Toast的标志（缓存标志），实际标志在handler中
         */
        private var reuse = false
        /**
         * showToast的定时任务
         */
        private var SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE: ScheduledExecutorService? = null
        /**
         * hideToast的定时任务
         */
        private var HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE: ScheduledExecutorService? = null

        /*--------------------------------私有静态方法--------------------------------*/

        /**
         * 显示Toast
         *
         * @param context     上下文
         * @param messageText Toast文本内容
         * @param duration    Toast持续时间（单位：毫秒）
         */
        @SuppressLint("ShowToast")
        private fun showMyToast(context: Context, messageText: String?, duration: Int) {
            if (toastHandler == null) {
                toastHandler = ToastHandler(context.applicationContext)
            }
            setHandlerReuse()
            if (SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE != null) {
                SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE!!.shutdownNow()
                SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE = null
            }

            if (HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE != null) {
                HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE!!.shutdownNow()
                HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE = null
            }
            SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE = BaseManager.newScheduledExecutorService()
            HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE = BaseManager.newScheduledExecutorService()
            val first = booleanArrayOf(true)
            SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE!!.scheduleAtFixedRate({
                if (first[0]) {
                    handlerShowToast(messageText, ToastHandler.FIRST_SEND)
                    first[0] = false
                } else {
                    handlerShowToast(messageText, ToastHandler.KEEP_TOAST)
                }
            }, 0, 3000, TimeUnit.MILLISECONDS)

            HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE!!.schedule({
                SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE!!.shutdownNow()
                SHOW_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE = null
                handlerCancelToast()
                HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE!!.shutdownNow()
                HIDE_TOAST_KEEP_SCHEDULED_EXECUTOR_SERVICE = null
            }, duration.toLong(), TimeUnit.MILLISECONDS)
        }

        /**
         * 使用Handler显示Toast
         *
         * @param messageText Toast文本内容
         * @param arg         是否为定时器保持消息显示
         */
        private fun handlerShowToast(messageText: String?, arg: Int) {
            val message = Message()
            message.obj = messageText
            message.what = ToastHandler.MESSAGE
            message.arg1 = arg
            toastHandler!!.sendMessage(message)
        }

        /**
         * 使用Handler取消Toast
         */
        private fun handlerCancelToast() {
            val message = Message()
            message.what = ToastHandler.CANCEL
            toastHandler!!.sendMessage(message)
        }

        /**
         * 设置Handler是否重用未消失的Toast
         */
        private fun setHandlerReuse() {
            val message = Message()
            message.what = ToastHandler.SET_RE_USE
            message.obj = reuse
            toastHandler!!.sendMessage(message)
        }

        /**
         * 设置是否重用（缓存位，每次在显示Toast前会将其设置到Handler中）
         *
         * @param reuse true表示开启重用
         */
        fun setReuse(reuse: Boolean) {
            CustomToast.reuse = reuse
        }

        /*--------------------------------公开静态方法--------------------------------*/

        /**
         * 获取CustomToast本类
         *
         * @param context  上下文
         * @param message  吐司显示信息
         * @param duration 吐司显示时长
         * @return CustomToast本类
         */
        fun makeText(context: Context, message: String, duration: Int): CustomToast {
            if (customToast == null) {
                synchronized(CustomToast::class.java) {
                    if (customToast == null) {
                        customToast = CustomToast(context.applicationContext, message, duration)
                    } else {
                        customToast!!.messageText = message
                        customToast!!.duration = duration
                    }
                }
            } else {
                customToast!!.messageText = message
                customToast!!.duration = duration
            }
            return customToast!!
        }

        /**
         * CustomToast本类
         *
         * @param context    上下文
         * @param messageRes 吐司显示信息
         * @param duration   吐司显示时长
         * @return CustomToast本类
         */
        fun makeText(context: Context, @StringRes messageRes: Int, duration: Int): CustomToast {
            val message = context.getString(messageRes)
            if (customToast == null) {
                synchronized(CustomToast::class.java) {
                    if (customToast == null) {
                        customToast = CustomToast(context.applicationContext, message, duration)
                    } else {
                        customToast!!.messageText = message
                        customToast!!.duration = duration
                    }
                }
            } else {
                customToast!!.messageText = message
                customToast!!.duration = duration
            }
            return customToast!!
        }
    }
}
