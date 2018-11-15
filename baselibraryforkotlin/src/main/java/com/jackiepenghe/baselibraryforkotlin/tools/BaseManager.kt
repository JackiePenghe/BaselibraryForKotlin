package com.jackiepenghe.baselibraryforkotlin.tools

import android.os.Handler

import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory

@Suppress("unused")
/**
 * 管理器
 *
 * @author jackie
 */
object BaseManager {

    /*--------------------------------静态常量--------------------------------*/

    /**
     * Handler
     */
    /*--------------------------------getter--------------------------------*/

    val handler = Handler()

    /**
     * 线程工厂
     */
    private val threadFactory: ThreadFactory = ThreadFactory { r -> Thread(r) }

    /**
     * 定时任务执行服务
     */
    internal val scheduledThreadPoolExecutor: ScheduledExecutorService = ScheduledThreadPoolExecutor(1, threadFactory)

    private fun newThreadFactory(): ThreadFactory {
        return ThreadFactory { r ->
            /**
             * Constructs a new `Thread`.  Implementations may also initialize
             * priority, name, daemon status, `ThreadGroup`, etc.
             *
             * @param r a runnable to be executed by new thread instance
             * @return constructed thread, or `null` if the request to
             * create a thread is rejected
             */
            Thread(r)
        }
    }

    internal fun newScheduledExecutorService(): ScheduledExecutorService {
        return ScheduledThreadPoolExecutor(1, newThreadFactory())
    }
}
