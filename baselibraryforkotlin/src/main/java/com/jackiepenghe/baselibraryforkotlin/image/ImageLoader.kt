package com.jackiepenghe.baselibraryforkotlin.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.widget.ImageView
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.files.FileCache
import com.jackiepenghe.baselibraryforkotlin.tools.MemoryCache
import com.jackiepenghe.baselibraryforkotlin.tools.Tool


import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


@Suppress("unused")
/**
 * 图片加载类
 * Created by ALM on 2016/7/7.
 */
class ImageLoader
/*--------------------------------构造函数--------------------------------*/

/**
 * 构造器
 *
 * @param context 上下文
 */
private constructor(context: Context) {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 内存缓存工具
     */
    internal var memoryCache = MemoryCache()

    /**
     * 文件缓存工具
     */
    private val fileCache: FileCache = FileCache(context)

    /**
     * 存储图片空间的集合
     */
    private val imageViews = Collections
        .synchronizedMap(WeakHashMap<ImageView, String>())

    /**
     * 线程池
     */
    private val threadPoolExecutor: ThreadPoolExecutor

    /**
     * 默认图片
     */
    internal val defaultDrawable = R.drawable.ic_launcher

    /**
     * 边线的宽度
     */
   private var strokeWidth = 0

    /**
     * 图片是否是圆形
     */
    private var isCircle: Boolean = false

    /**
     * 是否压缩图片
     */
    private var compress: Boolean = false

    init {
        val threadFactory = ThreadFactory { r -> Thread(r) }
        threadPoolExecutor = ThreadPoolExecutor(
            2,
            10,
            0,
            TimeUnit.MINUTES,
            ArrayBlockingQueue(1024),
            threadFactory,
            ThreadPoolExecutor.AbortPolicy()
        )
    }

    /*--------------------------------公开函数--------------------------------*/

    /**
     * 显示图片
     *
     * @param url       文件url
     * @param imageView ImageView
     * @param circle    是否显示圆形图片
     */
    fun displayImage(url: String, imageView: ImageView, circle: Boolean) {
        isCircle = circle
        imageViews[imageView] = url
        // 先从内存缓存中查找
        val bitmap = memoryCache[url]
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(defaultDrawable)
            // 若没有的话则开启新线程加载图片
            loadBitmapFromNet(url, imageView)
        }
    }

    /**
     * 获取图片 先从缓存中去查找，如果没有再从网络下载
     *
     * @param url 图片地址
     * @return 位图图片
     */
    fun getBitmap(url: String): Bitmap? {
        val f = fileCache.getFile(url)

        // 先从文件缓存中查找是否有
        val b = decodeFile(f)
        if (b != null) {
            return b
        }

        // 最后从指定的url中下载图片
        try {
            val bitmap: Bitmap? = decodeFile(f)
            val imageUrl = URL(url)
            val conn = imageUrl
                .openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.instanceFollowRedirects = true
            val `is` = conn.inputStream
            val os = FileOutputStream(f)
            copyStream(`is`, os)
            os.close()
            return bitmap
        } catch (e: Exception) {
            Tool.debugOut("ImageLoader getBitmap Exception")
            return null
        }

    }

    /**
     * 清除缓存
     */
    fun clearCache() {
        memoryCache.clear()
        fileCache.clear()
    }

    /**
     * 设置是否压缩图片
     *
     * @param isCompress 要设置是否压缩图片的标志
     */
    fun setCompress(isCompress: Boolean) {
        compress = isCompress
    }

    /*--------------------------------库内函数--------------------------------*/

    /**
     * 防止图片错位
     *
     * @param holder BitmapHolder
     * @return false表示错位
     */
    internal fun imageViewReused(holder: BitmapDisplayer.Companion.BitmapHolder): Boolean {
        val tag = imageViews[holder.imageView]

        return tag == null || tag != holder.url
    }

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 下载图片操作
     *
     * @param is InputStream
     * @param os OutputStream
     */
    private fun copyStream(`is`: InputStream, os: OutputStream) {
        val bufferSize = 1024
        try {
            val bytes = ByteArray(bufferSize)
            while (true) {
                val count = `is`.read(bytes, 0, bufferSize)
                if (count == -1) {
                    break
                }
                os.write(bytes, 0, count)
            }
        } catch (e: Exception) {
            Tool.errorOut("ImageLoader", "ImageLoader 保存文件失败...")
        }

    }

    /**
     * 加载网络图片
     *
     * @param url       网络url
     * @param imageView ImageView
     */
    private fun loadBitmapFromNet(url: String, imageView: ImageView) {
        val bitmapHolder = BitmapDisplayer.Companion.BitmapHolder(url, imageView)
        threadPoolExecutor.submit(BitmapLoader(bitmapHolder, this@ImageLoader))
    }

    /**
     * 压缩图片
     *
     * @param f 图片的本地路径
     * @return 位图图片
     */
    private fun decodeFile(f: File): Bitmap? {
        try {
            val bitmap: Bitmap?
            if (compress) {
                // 不加载图片的情况下获得图片的宽高
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(FileInputStream(f), null, o)

                val requiredSize = 70
                var widthTmp = o.outWidth
                var heightTmp = o.outHeight
                var scale = 1
                // 如果长或宽大于70，那么把图片的高宽缩小一半
                while (widthTmp / 2 >= requiredSize && heightTmp / 2 >= requiredSize) {
                    widthTmp /= 2
                    heightTmp /= 2
                    scale *= 2
                }

                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                // 把图片的高宽缩小一半
                bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, o2)
            } else {
                val o = BitmapFactory.Options()
                bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, o)
            }
            if (bitmap == null) {
                return null
            }
            return if (isCircle) createCircleBitmap(bitmap) else bitmap
        } catch (e: FileNotFoundException) {
            val message = e.message?:return null
            Tool.errorOut("ImageLoader", message)
        }

        return null
    }

    /**
     * 把 源圆片 加工成 圆形图片
     *
     * @param resource 源圆片
     * @return 位图图片
     */
    private fun createCircleBitmap(resource: Bitmap): Bitmap {
        val width = resource.width
        val paint = Paint()
        // 画圆或者弧形图，需要抗锯齿
        paint.isAntiAlias = true

        // 创建一张空图片, 这张图片只有宽高，没有内容

        val target = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(target)

        // 画一个和原图片宽高一样的内切圆
        canvas.drawCircle(
            (width / 2).toFloat(), (width / 2).toFloat(), ((width - strokeWidth) / 2).toFloat(),
            paint
        )

        // 取两图的交集(也就是重合的部分)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 把源图覆盖上去
        canvas.drawBitmap(resource, 0f, 0f, paint)

        return target
    }

    companion object {

        /**
         * 图片加载器单例
         */
        private var instance: ImageLoader? = null

        /*--------------------------------公开静态函数--------------------------------*/

        /**
         * 获取ImageLoader单例
         *
         * @param context 上下文对象
         * @return 当前ImageLoader对象
         */
        fun getInstance(context: Context): ImageLoader {
            if (instance == null) {
                synchronized(ImageLoader::class.java) {
                    if (instance == null) {
                        instance = ImageLoader(context)
                    }
                }
            }
            return instance!!
        }
    }
}
