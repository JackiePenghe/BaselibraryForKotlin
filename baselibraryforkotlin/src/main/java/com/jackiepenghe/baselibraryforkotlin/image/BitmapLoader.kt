package com.jackiepenghe.baselibraryforkotlin.image


import java.lang.ref.WeakReference

/**
 * 位图图像加载工具
 *
 * @author alm
 */

class BitmapLoader
/*--------------------------------构造函数--------------------------------*/

/**
 * 构造函数
 * @param bitmapHolder 位图图像Holder
 * @param imageLoader ImageLoader
 */
(bitmapHolder: BitmapDisplayer.Companion.BitmapHolder, imageLoader: ImageLoader) : Runnable {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 位图图像Holder弱引用
     */
    private val bitmapHolderWeakReference: WeakReference<BitmapDisplayer.Companion.BitmapHolder> = WeakReference(bitmapHolder)
    /**
     * ImageLoader弱引用
     */
    private val imageLoaderWeakReference: WeakReference<ImageLoader> = WeakReference(imageLoader)

    /*--------------------------------实现接口函数--------------------------------*/

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see Thread.run
     */
    override fun run() {
        val bitmapHolder = bitmapHolderWeakReference.get() ?: return
        val imageLoader = imageLoaderWeakReference.get() ?: return
// 如果没有错位那么不做任何处理，如果错位那么需要再做一次加载处理
        if (imageLoader.imageViewReused(bitmapHolder)) {
            return
        }
        val bmp = imageLoader.getBitmap(bitmapHolder.url) ?: return
        imageLoader.memoryCache.put(bitmapHolder.url, bmp)

        if (imageLoader.imageViewReused(bitmapHolder)) {
            return
        }
        val displayer = BitmapDisplayer(bmp, bitmapHolder, imageLoader)
        // 更新的操作放在UI线程中
        bitmapHolder.imageView.post(displayer)

    }
}

