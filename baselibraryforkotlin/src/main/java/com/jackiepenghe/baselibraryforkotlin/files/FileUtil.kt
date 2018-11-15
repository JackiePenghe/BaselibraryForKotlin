package com.jackiepenghe.baselibraryforkotlin.files

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("unused")
/**
 * 文件工具类
 *
 * @author ALM
 */
object FileUtil {

    /*--------------------------------静态常量--------------------------------*/

    /**
     * TAG
     */
    private const val TAG = "FileUtil"

    /**
     * 在文件管理器中的程序文件夹名（默认值，未执行初始化函数就是这个值）
     */
    private const val UNNAMED_APP_DIR = "UnnamedAppDir"

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 初始化状态
     */
    private var init = false

    /**
     * 应用程序名(在文件管理器中的程序文件夹名)
     */
    private var APP_NAME: String? = UNNAMED_APP_DIR

    /**
     * 获取SD卡文件目录
     *
     * @return 获取SD卡路径
     */
    //获取挂载状态
    //如果是已挂载,说明了有内存卡
    private val sdCardDir: File
        get() {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                return Environment.getExternalStorageDirectory()
            }
            throw NullPointerException("No SD card was found!")
        }

    /**
     * 获取本项目文件目录
     *
     * @return 本项目文件目录
     */
    val appDir: File?
        get() {

            if (!init) {
                throw NullPointerException("FileUtil not init")
            }
            if (null == APP_NAME || "" == APP_NAME) {
                throw ExceptionInInitializerError("File name invalid")
            }
            val file = File(sdCardDir, APP_NAME)
            val mkdirs: Boolean
            if (!file.exists()) {
                mkdirs = file.mkdirs()
                return if (mkdirs) {
                    file
                } else {
                    null
                }
            }
            return file
        }

    /**
     * 获取项目缓存文件目录
     *
     * @return 项目缓存文件目录
     */
    val cacheDir: File?
        get() {
            val file = File(appDir, "cache")
            val mkdirs: Boolean
            if (!file.exists()) {
                mkdirs = file.mkdirs()
                return if (mkdirs) {
                    file
                } else {
                    null
                }
            }
            return file
        }

    /**
     * 获取项目的异常日志目录
     *
     * @return 项目的异常日志目录
     */
    val crashDir: File?
        get() {
            val file = File(appDir, "crash")
            val mkdirs: Boolean
            if (!file.exists()) {
                mkdirs = file.mkdirs()
                return if (mkdirs) {
                    file
                } else {
                    null
                }
            }
            return file
        }

    /**
     * 获取apk存储目录
     *
     * @return apk存储目录
     */
    val apkDir: File?
        get() {
            val file = File(appDir, "apk")
            val mkdirs: Boolean
            if (!file.exists()) {
                mkdirs = file.mkdirs()
                return if (mkdirs) {
                    file
                } else {
                    null
                }
            }
            return file
        }

    /**
     * 获取图片目录
     *
     * @return 图片目录
     */
    val imageDir: File?
        get() {
            val file = File(appDir, "images")
            val mkdirs: Boolean
            if (!file.exists()) {
                mkdirs = file.mkdirs()
                return if (mkdirs) {
                    file
                } else {
                    null
                }
            }
            return file
        }

    /**
     * 获取项目数据目录
     *
     * @return 项目数据目录
     */
    val dataDir: File?
        get() {
            val file = File(appDir, "data")
            val mkdirs: Boolean
            if (!file.exists()) {
                mkdirs = file.mkdirs()
                return if (mkdirs) {
                    file
                } else {
                    null
                }
            }
            return file
        }

    /*--------------------------------公开静态函数--------------------------------*/

    /**
     * 初始化（没有执行此函数就执行其他函数会抛出异常）
     * @param context 上下文
     */
    fun init(context: Context) {
        val filesDir = context.filesDir
        val absolutePath = filesDir.absolutePath
        Tool.warnOut(TAG, "app absolutePath = $absolutePath")
        val split = absolutePath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var appName = split[split.size - 2]
        Tool.warnOut(TAG, "appName = $appName")
        val split1 = appName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        appName = split1[split1.size - 1]
        Tool.warnOut(TAG, "app name = $appName")
        FileUtil.APP_NAME = appName
        init = true
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param apkFile    文件
     */
    fun installApk(context: Context, apkFile: File) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val type = "application/vnd.android.package-archive"
        intent.setDataAndType(Uri.fromFile(apkFile), type)
        context.startActivity(intent)
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    fun delFile(fileName: String): Boolean {
        val file = File(appDir, fileName)
        if (file.isFile) {

            file.delete()
            return true
        }

        file.exists()
        return false
    }

    /**
     * 保存图片
     *
     * @param bm      位图图片
     * @param picName 文件名
     */
    fun saveBitmap(bm: Bitmap, picName: String) {
        Log.e("", "保存图片")
        try {

            val f = File(cacheDir, "$picName.JPEG")
            if (f.exists()) {

                f.delete()
            }

            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Log.e("", "已经保存")
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 检查文件是否存在
     *
     * @param fileName 文件名
     * @return true表示文件存在
     */
    fun isFileExist(fileName: String): Boolean {
        val file = File(appDir, fileName)

        file.isFile
        return file.exists()
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件路径
     * @return true表示文件存在
     */
    fun fileIsExists(path: String): Boolean {
        try {
            val file = File(path)
            if (!file.exists()) {
                return false
            }
        } catch (e: Exception) {

            return false
        }

        return true
    }
}
