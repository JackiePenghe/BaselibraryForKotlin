package com.jackiepenghe.baselibraryforkotlin.fragment

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.jackiepenghe.baselibraryforkotlin.R


/**
 * fragment的基类
 *
 * @author alm
 */

@Suppress("unused")
abstract class BaseFragment : Fragment() {

    /*---------------成员变量---------------*/

    /**
     * 整个Fragment的根布局
     */
    private var root: View? = null
    /**
     * 标题
     */
    private var toolBar: Toolbar? = null
    /**
     * 标题文本
     */
    private var titleView: TextView? = null
    /**
     * 标题栏左边的小图标
     */
    private var titleLeftImage: ImageView? = null
    /**
     * 标题栏左边的文本
     */
    private var titleLeftText: TextView? = null
    /**
     * 标题栏右边的小图标
     */
    private var titleRightImage: ImageView? = null
    /**
     * 标题栏右边的文本
     */
    private var titleRightText: TextView? = null
    /**
     * 用于适配沉浸式状态栏的专用控件
     */
    private var statusView: View? = null

    /**
     * 判断当前的Fragment是否对用户可见
     *
     * @return true表示可见
     */
    val isVisibilityForUser: Boolean
        get() = userVisibleHint

    /*---------------重写父类函数---------------*/

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * [.onCreate] and [.onActivityCreated].
     *
     *
     *
     * If you return a View from here, you will later be called in
     * [.onDestroyView] when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView:View = inflater.inflate(R.layout.fragment_base, container, false)
        toolBar = contentView.findViewById(R.id.toolbar)
        titleView = contentView.findViewById(R.id.toolbar_title)
        titleLeftImage = contentView.findViewById(R.id.title_left_image)
        titleLeftText = contentView.findViewById(R.id.title_left_text)
        titleRightImage = contentView.findViewById(R.id.title_right_image)
        titleRightText = contentView.findViewById(R.id.title_right_text)
        statusView = contentView.findViewById(R.id.fragment_base_status_bar)
        doBeforeSetLayout()
        val frameLayout = contentView.findViewById<FrameLayout>(R.id.base_frame_content)
        root = inflater.inflate(setLayout(), frameLayout, false)
        frameLayout.addView(root)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initViews()
        initViewData()
        initEvents()
        doAfterAll()
    }

    /**
     * Set a hint to the system about whether this fragment's UI is currently visible
     * to the user. This hint defaults to true and is persistent across fragment instance
     * state save and restore.
     *
     *
     *
     * An app may set this to false to indicate that the fragment's UI is
     * scrolled out of visibility or is otherwise not directly visible to the user.
     * This may be used by the system to prioritize operations such as fragment lifecycle updates
     * or loader ordering behavior.
     *
     *
     *
     * **Note:** This method may be called outside of the fragment lifecycle.
     * and thus has no ordering guarantees with regard to fragment lifecycle method calls.
     *
     * @param isVisibleToUser true if this fragment's UI is currently visible to the user (default),
     * false if it is not.
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            onVisibleHint()
        } else {
            onUnVisibleHint()
        }
    }

    /*---------------抽象方法---------------*/

    /**
     * 在设置布局之前进行的操作
     */
    protected abstract fun doBeforeSetLayout()

    /**
     * 设置fragment的布局
     *
     * @return 布局id
     */
    protected abstract fun setLayout(): Int

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化控件
     */
    protected abstract fun initViews()

    /**
     * 初始化控件数据
     */
    protected abstract fun initViewData()

    /**
     * 初始化事件
     */
    protected abstract fun initEvents()

    /**
     * 在最后执行的操作
     */
    protected abstract fun doAfterAll()

    /**
     * 当fragment可见时调用此方法
     */
    protected abstract fun onVisibleHint()

    /**
     * 当fragment不可见时调用此方法
     */
    protected abstract fun onUnVisibleHint()

    /*---------------自定义子类可用函数---------------*/

    /**
     * 让fragment可以和Activity一样拥有findViewById函数
     *
     * @param viewId 控件id
     * @return 控件
     */
    protected fun <T : View> findViewById(@IdRes viewId: Int): T {
        return root!!.findViewById(viewId)
    }

    /**
     * 隐藏标题栏
     */
    protected fun hideTitleBar() {
        if (null == toolBar) {
            return
        }
        toolBar!!.visibility = View.GONE
    }

    /**
     * 将整个fragment的所有布局隐藏
     */
    protected fun hideAll() {
        if (root == null) {
            return
        }
        root!!.visibility = View.GONE
    }

    /**
     * 将整个fragment的所有布局显示
     */
    protected fun showAll() {
        if (null == root) {
            return
        }
        root!!.visibility = View.VISIBLE
    }

    /**
     * 显示标题栏
     */
    protected fun showTitle() {
        if (null == toolBar) {
            return
        }
        toolBar!!.visibility = View.VISIBLE
    }


    /**
     * 设置标题栏的内容
     *
     * @param titleRes 标题栏的资源id
     */
    protected fun setTitleText(@StringRes titleRes: Int) {
        if (null == toolBar) {
            return
        }
        if (null == titleView) {
            return
        }
        toolBar!!.visibility = View.VISIBLE
        titleView!!.setText(titleRes)
    }

    /**
     * 设置标题栏的内容
     *
     * @param titleText 标题栏的文本
     */
    protected fun setTitleText(titleText: String) {
        if (null == toolBar) {
            return
        }
        if (null == titleView) {
            return
        }
        toolBar!!.visibility = View.VISIBLE
        titleView!!.text = titleText
    }

    /**
     * 设置标题栏左边的图片
     *
     * @param drawableRes 图片资源文件
     */
    protected fun setTitleLeftImage(@DrawableRes drawableRes: Int) {
        if (null == titleLeftImage) {
            return
        }
        titleLeftImage!!.visibility = View.VISIBLE
        titleLeftImage!!.setImageResource(drawableRes)
    }

    /**
     * 隐藏标题栏左边的图片
     */
    protected fun hideTitleLeftImage() {
        if (null == titleLeftImage) {
            return
        }
        titleLeftImage!!.visibility = View.GONE
    }

    /**
     * 设置标题栏左边的文本内容
     *
     * @param textRes 文本资源
     */
    protected fun setTitleLeftText(@StringRes textRes: Int) {
        if (null == titleLeftImage) {
            return
        }
        if (null == titleLeftText) {
            return
        }
        titleLeftImage!!.visibility = View.VISIBLE
        titleLeftText!!.setText(textRes)
    }

    /**
     * 设置标题栏左边的文本内容
     *
     * @param text 文本
     */
    protected fun setTitleLeftText(text: String) {
        if (null == titleLeftImage) {
            return
        }

        if (null == titleLeftText) {
            return
        }
        titleLeftImage!!.visibility = View.VISIBLE
        titleLeftText!!.text = text
    }

    /**
     * 隐藏标题栏左边的文本
     */
    protected fun hideTitleLeftText() {
        if (null == titleLeftText) {
            return
        }
        titleLeftText!!.visibility = View.GONE
    }

    /**
     * 设置标题栏右边的图片
     *
     * @param drawableRes 图片资源文件
     */
    protected fun setTitleRightImage(@DrawableRes drawableRes: Int) {
        if (null == titleRightImage) {
            return
        }
        titleRightImage!!.visibility = View.VISIBLE
        titleRightImage!!.setImageResource(drawableRes)
    }

    /**
     * 隐藏标题栏右边的小图标
     */
    protected fun hideTitleRightImage() {
        if (null == titleRightImage) {
            return
        }
        titleRightImage!!.visibility = View.GONE
    }

    /**
     * 设置标题栏右边的文字
     *
     * @param textRes 文本资源
     */
    protected fun setTitleRightText(@StringRes textRes: Int) {
        if (null == titleRightText) {
            return
        }
        titleRightText!!.visibility = View.VISIBLE
        titleRightText!!.setText(textRes)
    }

    /**
     * 设置标题栏右边的文字
     *
     * @param text 文本
     */
    protected fun setTitleRightText(text: String) {
        if (null == titleRightText) {
            return
        }
        titleRightText!!.visibility = View.VISIBLE
        titleRightText!!.text = text
    }

    /**
     * 隐藏标题栏右边的文本
     */
    protected fun hideTitleRightText() {
        if (null == titleRightText) {
            return
        }
        titleRightText!!.visibility = View.GONE
    }

    /**
     * 设置标题栏左边的图片的点击事件
     */
    protected fun setTitleLeftImageClickListener(clickListener: View.OnClickListener) {
        if (null == titleLeftImage) {
            return
        }
        titleLeftImage!!.isClickable = true
        titleLeftImage!!.setOnClickListener(clickListener)
    }

    /**
     * 设置标题栏左边的文字的点击事件
     */
    protected fun setTitleLeftTextClickListener(clickListener: View.OnClickListener) {
        if (null == titleLeftText) {
            return
        }
        titleLeftText!!.isClickable = true
        titleLeftText!!.setOnClickListener(clickListener)
    }

    /**
     * 设置标题栏右边的图片的点击事件
     */
    protected fun setTitleRightImageClickListener(clickListener: View.OnClickListener) {
        if (null == titleRightImage) {
            return
        }
        titleRightImage!!.isClickable = true
        titleRightImage!!.setOnClickListener(clickListener)
    }

    /**
     * 设置标题栏右边的文字的点击事件
     */
    protected fun setTitleRightTextClickListener(clickListener: View.OnClickListener) {
        if (null == titleRightText) {
            return
        }
        titleRightText!!.isClickable = true
        titleRightText!!.setOnClickListener(clickListener)
    }

    /**
     * 设置状态栏颜色
     *
     * @param color 状态栏颜色
     */
    protected fun setFragmentStatusColor(@ColorInt color: Int) {
        if (null == statusView) {
            return
        }
        statusView!!.visibility = View.VISIBLE
        statusView!!.setBackgroundColor(color)
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorRes 状态栏颜色
     */
    protected fun setFragmentStatusColorRes(@ColorRes colorRes: Int) {
        if (null == statusView) {
            return
        }
        statusView!!.visibility = View.VISIBLE
        statusView!!.setBackgroundResource(colorRes)
    }

    /**
     * 隐藏Fragment的状态栏控件
     */
    protected fun hideFragmentStatusView() {
        statusView!!.visibility = View.GONE
    }

    /**
     * 显示Fragment的状态栏控件
     */
    protected fun showFragmentStatusView() {
        statusView!!.visibility = View.VISIBLE
    }
}
