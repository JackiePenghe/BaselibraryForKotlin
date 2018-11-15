package com.jackiepenghe.baselibraryforkotlin.tools

import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.util.Log

import java.lang.ref.WeakReference

/**
 * Home键的监听类（广播接收者方式）
 *
 * @author jacke
 */
class HomeWatcher
/*--------------------------------构造函数--------------------------------*/

/**
 * 构造函数
 *
 * @param context 上下文
 */
    (
    /*--------------------------------成员变量--------------------------------*/

    /**
     * 上下文
     */
    private val mContext: Context
) {
    /**
     * 广播过滤器
     */
    private val mFilter: IntentFilter
    /**
     * home键的回调监听
     */
    private var mListener: OnHomePressedListener? = null
    /**
     * home键监听的广播接收者
     */
    private var mReceiver: HomeWatcherReceiver? = null

    /**
     * Home键的接口
     */
    interface OnHomePressedListener {
        /**
         * Home键被按下
         */
        fun onHomePressed()

        /**
         * 长按Home键
         */
        fun onHomeLongPressed()
    }

    init {
        mFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    }

    /*--------------------------------公开函数--------------------------------*/

    /**
     * 设置监听
     *
     * @param listener listener
     */
    fun setOnHomePressedListener(listener: OnHomePressedListener) {
        mListener = listener
        mReceiver = HomeWatcherReceiver(this)
    }

    /**
     * 开始监听，注册广播接收者
     */
    fun startWatch() {
        if (mReceiver != null) {
            mContext.registerReceiver(mReceiver, mFilter)
        }
    }

    /**
     * 停止监听，注销广播接收者
     */
    fun stopWatch() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver)
        }
    }

    /*--------------------------------静态内部类--------------------------------*/

    /**
     * 广播接收者,用于监听home键被按下
     */
    internal class HomeWatcherReceiver
    /*--------------------------------构造函数--------------------------------*/

    /**
     * 构造函数
     * @param homeWatcher HomeWatcher对象
     */
        (homeWatcher: HomeWatcher) : BroadcastReceiver() {

        /*--------------------------------静态常量--------------------------------*/

        val SYSTEM_DIALOG_REASON_KEY = "reason"

        val SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions"

        val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
        val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"

        /*--------------------------------成员变量--------------------------------*/
        /**
         * HomeWatcher弱引用
         */
        private val homeWatcherWeakReference: WeakReference<HomeWatcher>

        init {
            homeWatcherWeakReference = WeakReference(homeWatcher)
        }

        /*--------------------------------实现父类函数--------------------------------*/

        /**
         * This method is called when the BroadcastReceiver is receiving an Intent
         * broadcast.  During this time you can use the other methods on
         * BroadcastReceiver to view/modify the current result values.  This method
         * is always called within the main thread of its process, unless you
         * explicitly asked for it to be scheduled on a different thread using
         * [*][Context.registerReceiver]. When it runs on the main
         * thread you should
         * never perform long-running operations in it (there is a timeout of
         * 10 seconds that the system allows before considering the receiver to
         * be blocked and a candidate to be killed). You cannot launch a popup dialog
         * in your implementation of onReceive().
         *
         *
         *
         * **If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
         * then the object is no longer alive after returning from this
         * function.** This means you should not perform any operations that
         * return a result to you asynchronously. If you need to perform any follow up
         * background work, schedule a [JobService] with
         * [JobScheduler].
         *
         *
         * If you wish to interact with a service that is already running and previously
         * bound using [bindService()][Context.bindService],
         * you can use [.peekService].
         *
         *
         *
         * The Intent filters used in [Context.registerReceiver]
         * and in application manifests are *not* guaranteed to be exclusive. They
         * are hints to the operating system about how to find suitable recipients. It is
         * possible for senders to force delivery to specific recipients, bypassing filter
         * resolution.  For this reason, [onReceive()][.onReceive]
         * implementations should respond only to known actions, ignoring any unexpected
         * Intents that they may receive.
         *
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val homeWatcher = homeWatcherWeakReference.get() ?: return
            if (action == null) {
                return
            }
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)

                if (reason != null) {
                    Log.i(TAG, "action:$action,reason:$reason")
                    if (homeWatcher.mListener != null) {
                        if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                            // 短按home键
                            homeWatcher.mListener!!.onHomePressed()
                        } else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS) {
                            // 长按home键
                            homeWatcher.mListener!!.onHomeLongPressed()
                        }
                    }
                }
            }
        }
    }

    companion object {

        /*--------------------------------静态常量--------------------------------*/

        /**
         * TAG
         */
        private val TAG = "HomeWatcher"
    }
}