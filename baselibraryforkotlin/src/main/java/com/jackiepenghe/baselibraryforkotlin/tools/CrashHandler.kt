package com.jackiepenghe.baselibraryforkotlin.tools

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.files.FileUtil

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Suppress("unused")
/**
 * 全局异常捕获工具类
 *
 * @author alm
 */

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 系统默认的UncaughtException处理类
     */
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    /**
     * 程序的Context对象
     */
    private var mContext: Context? = null
    /**
     * 用来存储设备信息和异常信息
     */
    private val stringStringHashMap = HashMap<String, String>()
    /**
     * 存储异常日志的文件目录
     */
    private var crashFileDirPath = Environment.getExternalStorageDirectory().path

    /*--------------------------------实现接口函数--------------------------------*/

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread 线程
     * @param ex     Throwable
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                Log.e(TAG, "error : ", e)
            }

            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /*--------------------------------公开函数--------------------------------*/

    /**
     * 初始化
     *
     * @param context 上下文
     */
    fun init(context: Context, crashFileDirPath: String) {
        mContext = context
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        this.crashFileDirPath = crashFileDirPath
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        val crashDir = FileUtil.crashDir ?: throw NullPointerException("crashDir == null")
        init(context, crashDir.absolutePath)
    }

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 错误
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }

        val threadFactory = ThreadFactory { r ->
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

        val executorService = ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.MILLISECONDS,
            ArrayBlockingQueue(1024),
            threadFactory,
            ThreadPoolExecutor.AbortPolicy()
        )

        val runnable = Runnable {
            Looper.prepare()
            Toast.makeText(mContext, R.string.exit_with_error, Toast.LENGTH_LONG).show()
            Looper.loop()
        }

        //使用Toast来显示异常信息
        executorService.execute(runnable)
        //收集设备参数信息
        collectDeviceInfo(mContext!!)
        //保存日志文件
        saveCrashInfo2File(ex)
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param context 上下文
     */
    @SuppressLint("NewApi")
    private fun collectDeviceInfo(context: Context) {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    pi.longVersionCode.toString()
                } else {
                    @Suppress("DEPRECATION")
                    pi.versionCode.toString() + ""
                }

                stringStringHashMap["versionName"] = versionName
                stringStringHashMap["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                stringStringHashMap[field.name] = field.get(null).toString()
                Log.d(TAG, field.name + " : " + field.get(null))
            } catch (e: Exception) {
                Log.e(TAG, "an error occurred when collect crash info", e)
            }

        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常
     */
    @Synchronized
    private fun saveCrashInfo2File(ex: Throwable) {

        val sb = StringBuilder()
        for ((key, value) in stringStringHashMap) {
            sb.append(key).append("=").append(value).append("\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = DATE_FORMAT.format(Date())
            val fileName = "crash-$time-$timestamp.log"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val dir = File(crashFileDirPath)
                if (!dir.exists()) {
                    val mkdirs = dir.mkdirs()
                    Log.e(TAG, "mkdirs $mkdirs")
                }
                val fos = FileOutputStream("$crashFileDirPath/$fileName")
                fos.write(sb.toString().toByteArray())
                fos.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "an error occurred while writing file...", e)
        }

    }

    companion object {

        /*--------------------------------静态常量--------------------------------*/

        private const val TAG = "CrashHandler"
        /**
         * CrashHandler实例
         */
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: CrashHandler? = null

        /**
         * 用于格式化日期,作为日志文件名的一部分
         */
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA)

        /*--------------------------------静态函数--------------------------------*/

        /**
         * 获取CrashHandler本类单例
         *
         * @return CrashHandler本类单例
         */
        val instance: CrashHandler
            get() {
                if (INSTANCE == null) {
                    synchronized(CrashHandler::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = CrashHandler()
                        }
                    }
                }
                return INSTANCE!!
            }
    }
}
