package com.jackiepenghe.baselibraryforkotlin.tools

import android.graphics.Bitmap

import java.util.Collections
import java.util.LinkedHashMap

/**
 * 内存缓存工具类
 * Created by ALM on 2016/7/7.
 */
class MemoryCache {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 放入缓存时是个同步操作
     * LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
     * 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
     */
    private val cache = Collections
        .synchronizedMap(LinkedHashMap<String, Bitmap>(20, 1.5f, true))

    /**
     * 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
     * current allocated size
     */
    private var size: Long = 0

    /**
     * 缓存只能占用的最大堆内存
     * max memory in bytes
     */
    private var limit: Long = 1000000
    /*--------------------------------构造函数--------------------------------*/

    /**
     * 构造器
     */
    init {
        // use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4)
    }

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 设置内存可用大小
     *
     * @param new_limit 内存可用大小
     */
    private fun setLimit(new_limit: Long) {
        limit = new_limit
        Tool.infoOut(TAG, "MemoryCache will use up to " + limit.toDouble() / 1024.0 / 1024.0 + "MB")
    }

    /**
     * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
     */
    private fun checkSize() {
        Tool.infoOut(TAG, "cache size=" + size + " length=" + cache.size)
        if (size > limit) {
            // 先遍历最近最少使用的元素
            val iter = cache.entries
                .iterator()
            while (iter.hasNext()) {
                val entry = iter.next()
                size -= getSizeInBytes(entry.value)
                iter.remove()
                if (size <= limit) {
                    break
                }
            }
            Tool.infoOut(TAG, "Clean cache. New size " + cache.size)
        }
    }

    /**
     * 图片占用的内存
     *
     * @param bitmap 位图图片
     * @return 图片占用的内存大小
     */
    private fun getSizeInBytes(bitmap: Bitmap?): Long {
        return if (bitmap == null) {
            0
        } else (bitmap.rowBytes * bitmap.height).toLong()
    }

    /*--------------------------------库内函数--------------------------------*/

    /**
     * 从链表里获取图片
     *
     * @param key 链表对应的key
     * @return 位图图片
     */
    operator fun get(key: String): Bitmap? {
        return try {
            if (!cache.containsKey(key)) {
                null
            } else cache[key]
        } catch (ex: NullPointerException) {
            null
        }

    }

    /**
     * 往链表里添加一张图片
     *
     * @param key    链表对应的key
     * @param bitmap 位图图片
     */
    fun put(key: String, bitmap: Bitmap) {
        try {
            if (cache.containsKey(key)) {
                size -= getSizeInBytes(cache[key])
            }
            cache[key] = bitmap
            size += getSizeInBytes(bitmap)
            checkSize()
        } catch (th: Throwable) {
            th.printStackTrace()
        }

    }

    /**
     * 清除缓存
     */
    fun clear() {
        cache.clear()
    }

    companion object {

        /*--------------------------------静态常量--------------------------------*/

        /**
         * 日志打印的TAG
         */
        private const val TAG = "MemoryCache"
    }
}