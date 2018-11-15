package com.jackiepenghe.baselibraryforkotlin.files

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

import java.io.File

@Suppress("unused")
/**
 * FileProvider工具类
 *
 * @author ALM
 */
object FileProviderUtil {

    /*--------------------------------静态常量--------------------------------*/

    private const val TAG = "FileProviderUtil"

    private const val CONTENT = "content"
    private const val ROOT_PATH = "/root"

    /*--------------------------------公开静态函数--------------------------------*/

    /**
     * 根据File获取文件Uri
     *
     * @param context 上下文
     * @param file    File对象
     * @return 文件Uri
     */
    fun getUriFromFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getUriFromFile24(context, file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * 设置intent数据和type
     *
     * @param context  上下文
     * @param intent   Intent
     * @param type     type
     * @param file     文件
     * @param canWrite 是否可写
     */
    fun setIntentDataAndType(
        context: Context,
        intent: Intent,
        type: String,
        file: File,
        canWrite: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getUriFromFile24(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (canWrite) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }

    /**
     * 设置intent数据
     *
     * @param context  上下文
     * @param intent   intent
     * @param file     文件
     * @param canWrite 是否可写
     */
    fun setIntentData(
        context: Context,
        intent: Intent,
        file: File,
        canWrite: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.data = getUriFromFile24(context, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (canWrite) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.data = Uri.fromFile(file)
        }
    }

    /**
     * 加入权限
     *
     * @param context  上下文
     * @param intent   intent
     * @param uri      文件Uri
     * @param canWrite 是否可写
     */
    fun grantPermissions(context: Context, intent: Intent, uri: Uri, canWrite: Boolean) {

        var flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (canWrite) {
            flag = flag or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
        intent.addFlags(flag)
        val resInfoList = context.packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, flag)
        }
    }

    /**
     * 根据文件Uri获取文件路径
     *
     * @param uri 文件Uri
     * @return 文件路径
     */
    fun getPath(uri: Uri): String {
        Tool.warnOut(TAG, "uri = $uri")
        val scheme = uri.scheme
        Tool.warnOut(TAG, "scheme = " + scheme!!)
        if (CONTENT != scheme) {
            throw RuntimeException("Uri scheme error! Need $CONTENT,find $scheme.")
        }
        if (!uri.isAbsolute) {
            throw RuntimeException("Uri must be absolute")
        }
        var path: String? = uri.path ?: return ""
        if (path!!.startsWith(ROOT_PATH, 0)) {
            path = path.substring(ROOT_PATH.length)
        }
        Tool.warnOut(TAG, "path = $path")
        return path
    }

    /*--------------------------------私有静态函数--------------------------------*/

    /**
     * 根据File获取文件Uri（需要至少API24）
     *
     * @param context 上下文
     * @param file    File对象
     * @return 文件Uri
     */
    @TargetApi(Build.VERSION_CODES.N)
    private fun getUriFromFile24(context: Context, file: File): Uri {
        val authority = context.packageName + ".fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }
}
