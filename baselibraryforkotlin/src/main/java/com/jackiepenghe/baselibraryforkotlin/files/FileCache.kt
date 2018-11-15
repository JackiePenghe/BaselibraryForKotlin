package com.jackiepenghe.baselibraryforkotlin.files

import android.content.Context

import java.io.File

@Suppress("unused")
/**
 * 文件缓存工具
 *
 * @author ALM
 */
class FileCache(context: Context) {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 缓存目录
     */
    private var cacheDir: File? = null

    init {
        // 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
        // 没有SD卡就放在系统的缓存目录中
        cacheDir = if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
            FileUtil.cacheDir
        } else {
            context.cacheDir
        }

        if (cacheDir == null) {
            throw NullPointerException("cacheDir == null")
        }

        //如果目录不存在，那么创建一个缓存目录
        if (!cacheDir!!.exists()) {

            cacheDir!!.mkdirs()
        }
    }

    /*--------------------------------公开函数--------------------------------*/

    /**
     * 根据url获取缓存文件
     *
     * @param url 缓存文件url
     * @return 缓存文件
     */
    fun getFile(url: String): File {
        // 将url的hashCode作为缓存的文件名
        val filename = url.hashCode().toString()
        return File(cacheDir, filename)

    }

    /*--------------------------------库内函数--------------------------------*/

    /**
     * 清除缓存
     */
    fun clear() {
        val files = cacheDir!!.listFiles() ?: return

        for (f in files) {

            f.delete()
        }
    }

}
