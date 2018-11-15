package com.jackiepenghe.baselibraryforkotlin.tools

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.jackiepenghe.baselibraryforkotlin.image.ImageLoader

@Suppress("MemberVisibilityCanBePrivate", "unused")
/**
 * ListView万能适配器专用的ViewHolder
 *
 * @author alm
 */

class ViewHolder
/*--------------------------------构造函数--------------------------------*/

/**
 * 构造函数
 *
 * @param context      上下文
 * @param parent       ViewGroup
 * @param itemLayoutId 布局文件资源id
 * @param position     当前位置
 */
private constructor(
    context: Context, parent: ViewGroup, @LayoutRes itemLayoutId: Int,
    /*--------------------------------成员变量--------------------------------*/

    /**
     * 当前位置
     */
    /**
     * 获取当前位置
     *
     * @return 当前位置
     */
    private val position: Int
) {

    /**
     * 根据id将不同的View保存起来
     */
    private val viewSparseArray: SparseArray<View> = SparseArray()

    /**
     * 当前item的View
     */
    /*--------------------------------公开函数--------------------------------*/

    /**
     * 获取item的整个布局控件
     *
     * @return item的整个布局控件
     */
    val convertView: View = LayoutInflater.from(context).inflate(itemLayoutId, parent, false)

    /**
     * 图片加载工具
     */
    private val imageLoader: ImageLoader

    init {
        convertView.tag = this
        imageLoader = ImageLoader.getInstance(context)
    }

    /**
     * 根据控件的Id获取控件(先从缓存中获取，如果没有则findViewById,然后保存到缓存)
     *
     * @param viewId 控件的Id
     * @return 对应的控件
     */
    fun getView(@IdRes viewId: Int): View? {
        var view: View? = viewSparseArray.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            viewSparseArray.put(viewId, view)
        }
        return view
    }


    /**
     * 为TextView设置文本内容
     *
     * @param viewId 控件的Id
     * @param text   要设置的文本内容
     * @return ViewHolder本类
     */
    fun setText(@IdRes viewId: Int, text: CharSequence): ViewHolder {
        val view = getView(viewId)
        if (view is TextView) {
            val textView = view as TextView?
            textView!!.text = text
        }
        return this
    }

    /**
     * 获取editText的文本
     *
     * @param viewId 控件的Id
     * @return editText的文本
     */
    fun getEditText(@IdRes viewId: Int): CharSequence? {
        val view = getView(viewId)
        if (view is EditText) {
            val editText = view as EditText?
            return editText!!.text
        }
        return null
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId        控件的Id
     * @param drawableResId 图片的资源Id
     * @return ViewHolder本类
     */
    fun setImageResource(@IdRes viewId: Int, @DrawableRes drawableResId: Int): ViewHolder {
        val view = getView(viewId)
        if (view is ImageView) {
            val imageView = view as ImageView?
            imageView!!.setImageResource(drawableResId)
        }
        return this
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId 控件的Id
     * @param bitmap 位图图片
     * @return ViewHolder本类
     */
    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): ViewHolder {
        val view = getView(viewId)
        if (view is ImageView) {
            val imageView = view as ImageView?
            imageView!!.setImageBitmap(bitmap)
        }
        return this
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId   控件的Id
     * @param url      图片的网络地址
     * @param isCircle 是否将图片显示为圆形
     * @return ViewHolder本类
     */
    fun setImageByUrl(@IdRes viewId: Int, url: String, isCircle: Boolean): ViewHolder {
        val view = getView(viewId)
        if (view is ImageView) {
            imageLoader.displayImage(url, view, isCircle)
        }
        return this
    }

    /**
     * 给view设置背景色
     *
     * @param viewId 控件的Id
     * @param color  背景色
     * @return ViewHolder本类
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): ViewHolder {
        val view = getView(viewId)
        view!!.setBackgroundColor(color)
        return this
    }

    companion object {

        /*--------------------------------静态函数--------------------------------*/

        /**
         * 获取一个ViewHolder本类实例
         *
         * @param context      上下文
         * @param convertView  当前item的View
         * @param parent       ViewGroup
         * @param itemLayoutId 布局文件资源id
         * @param position     当前位置
         * @return ViewHolder本类实例
         */
        operator fun get(
            context: Context,
            convertView: View?,
            parent: ViewGroup, @LayoutRes itemLayoutId: Int,
            position: Int
        ): ViewHolder {
            return if (convertView == null) {
                ViewHolder(context, parent, itemLayoutId, position)
            } else convertView.tag as ViewHolder
        }
    }
}
