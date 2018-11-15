package com.jackiepenghe.baselibraryforkotlin.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

/**
 * 防止程序启动白屏或黑屏
 *
 * @author alm
 */

abstract class BaseSplashActivity : Activity() {

    /*--------------------------------重写父类函数--------------------------------*/

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling [.setContentView] to inflate the
     * activity's UI, using [.findViewById] to programmatically interact
     * with widgets in the UI, calling
     * [.managedQuery] to retrieve
     * cursors for data being displayed, etc.
     *
     *
     *
     * You can call [.finish] from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ([.onStart], [.onResume],
     * [.onPause], etc) executing.
     *
     *
     *
     * *Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.*
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in [.onSaveInstanceState].  ***Note: Otherwise it is null.***
     * @see .onStart
     *
     * @see .onSaveInstanceState
     *
     * @see .onRestoreInstanceState
     *
     * @see .onPostCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //防止本界面被多次启动
        if (runApp()) {
            return
        }
        onCreate()
    }

    /*--------------------------------抽象函数--------------------------------*/

    /**
     * 在本界面第一次启动时执行的操作
     */
    protected abstract fun onCreate()

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 判断当前程序是否已经在运行了
     * @return true表示已经在运行了
     */
    private fun runApp(): Boolean {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT > 0) {
            //为了防止重复启动多个闪屏页
            finish()
            return true
        }
        return false
    }
}
