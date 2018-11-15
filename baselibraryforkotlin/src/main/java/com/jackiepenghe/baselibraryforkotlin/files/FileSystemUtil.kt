package com.jackiepenghe.baselibraryforkotlin.files

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.tools.Tool
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException


@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * 文件管理相关工具类
 *
 * @author ALM
 */
object FileSystemUtil {

    /*--------------------------------静态常量--------------------------------*/

    private const val PRIMARY = "primary"
    private const val IMAGE = "image"
    private const val VIDEO = "video"
    private const val AUDIO = "audio"
    private const val CONTENT = "content"
    private const val FILE = "file"

    /*--------------------------------公开静态函数--------------------------------*/

    /**
     * 根据uri获取文件路径
     *
     * @param context 上下文
     * @param uri     uri
     * @return 文件路径
     */
    fun getPath(context: Context, uri: Uri): String? {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if (PRIMARY.equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when {
                    IMAGE == type -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    VIDEO == type -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    AUDIO == type -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if (CONTENT.equals(uri.scheme!!, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if (FILE.equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * 打开文件管理(需要根据请求码在onActivityResult中获取数据
     * if(requestCode == your requestCode && resultCode == Activity.RESULT_OK)){
     * Uri uri = intent.getData;//获取文件uri
     * }
     *
     * @param activity    对应的Activity
     * @param requestCode 请求码
     * @return true表示打开成功
     */
    fun openSystemFile(activity: Activity, requestCode: Int): Boolean {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        return try {
            activity.startActivityForResult(intent, requestCode)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 根据文件类型打开某一个文件
     *
     * @param context  上下文
     * @param file     文件
     * @param fileType 文件类型
     */
    fun openFile(context: Context, file: File, fileType: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uriFromFile = FileProviderUtil.getUriFromFile(context, file.absoluteFile)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(uriFromFile, fileType)
        context.startActivity(intent)
    }

    /**
     * 打开某一个文件
     *
     * @param context 上下文
     * @param file    文件
     */
    fun openFile(context: Context, file: File) {
        openFile(context, file, "*/*")
    }

    /**
     * 读取文件内容
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param byteLength 要读取的文件内容长度(为0则全部读完)
     * @return 读取的文件内容
     */
    fun readFileFromPath(context: Context, filePath: String, byteLength: Int): ByteArray? {
        //若果SD卡存在
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var fileInputStream: FileInputStream? = null
            val file = File(filePath)
            try {
                fileInputStream = FileInputStream(file)
                val buffer: ByteArray
                buffer = if (byteLength == 0) {
                    val fileSize = fileInputStream.available()
                    ByteArray(fileSize)
                } else {
                    if (byteLength >= fileInputStream.available()) {
                        return null
                    }
                    ByteArray(byteLength)
                }

                fileInputStream.read(buffer)

                return buffer
            } catch (e: FileNotFoundException) {
                Tool.toastL(context, R.string.file_not_found_or_use_chinese)
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return null
        }

        return null
    }

    /**
     * 从当前应用程序的目录中获取某个文件的大小
     *
     * @param filePath 文件绝对路径
     * @return 文件大小(Byte)
     */
    fun getFileSizeFromRealPath(filePath: String): Int {
        //若果SD卡存在
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val file = File(filePath)
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = FileInputStream(file)

                return fileInputStream.available()

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return 0
        }
        return 0
    }

    /*--------------------------------私有静态函数--------------------------------*/

    /**
     * 得到这个Uri的数据列的值
     *
     * @param context       上下文
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return 数据列的值, 通常是一个文件路径
     */
    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)


        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * 检测是否属于外部存储的文件
     *
     * @param uri 要检测的Uri
     * @return uri是否指向外部存储
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * 检测是否属于下载uri
     *
     * @param uri 要检测的Uri
     * @return 是否属于下载uri
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * 检测Uri
     *
     * @param uri Uri
     * @return 是否是多媒体uri
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
