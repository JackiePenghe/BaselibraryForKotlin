package com.jackiepenghe.baselibraryforkotlin.image

import android.graphics.Bitmap
import android.widget.ImageView


import java.lang.ref.WeakReference

/**
 * 图片显示器
 *
 * @author alm
 */

class BitmapDisplayer(
    private val mBitmap: Bitmap?,
    private val mBitmapHolder: BitmapHolder,
    imageLoader: ImageLoader
) : Runnable {
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
        val imageLoader = imageLoaderWeakReference.get() ?: return
        if (imageLoader.imageViewReused(mBitmapHolder)) {
            return
        }

        if (mBitmap != null) {
            mBitmapHolder.imageView.setImageBitmap(mBitmap)
        } else {
            mBitmapHolder.imageView.setImageResource(imageLoader.defaultDrawable)
        }
    }


    companion object {
        /**
         * Created by alm on 17-6-6.
         * 用来封装Url和ImageView，防止图片错位里用
         */
        class BitmapHolder(
            var url: String,
            var imageView: ImageView


        )

    }
}