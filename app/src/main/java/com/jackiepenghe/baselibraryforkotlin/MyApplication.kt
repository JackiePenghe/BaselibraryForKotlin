package com.jackiepenghe.baselibraryforkotlin

import android.app.Application

import com.jackiepenghe.baselibraryforkotlin.files.FileUtil
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

/**
 *
 * @author alm
 * @date 2017/11/22 0022
 * Application类
 */

class MyApplication : Application() {

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    override fun onCreate() {
        super.onCreate()
        Tool.setDebugFlag(true)
        FileUtil.init(this)

        //初始化捕获全局异常
        //        CrashHandler crashHandler = CrashHandler.getInstance();
        //        crashHandler.init(this);
    }

}
