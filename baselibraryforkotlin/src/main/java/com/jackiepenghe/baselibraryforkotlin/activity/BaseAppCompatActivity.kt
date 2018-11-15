package com.jackiepenghe.baselibraryforkotlin.activity

import android.annotation.TargetApi
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.tools.StatusBarUtil


/**
 * 基于AppCompatActivity的封装的Activity基类
 *
 * @author alm
 */

@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class BaseAppCompatActivity : AppCompatActivity() {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 标题
     */
    private var toolbar: Toolbar? = null
    /**
     * 标题的返回按钮处的点击事件
     */
    private var mTitleBackButtonOnClickListener: View.OnClickListener? = null
    /**
     * 子类继承此类之后，设置的布局都在FrameLayout中
     */
    /**
     * 获取Activity布局的整个布局
     *
     * @return Activity布局的整个布局
     */
    protected var contentView: FrameLayout? = null
        private set
    /**
     * 标题栏的文字
     */
    private var titleView: TextView? = null
    /**
     * 整个BaseActivity的根布局（setContentLayout传入的布局）
     */
    private var rootView: LinearLayout? = null
    /**
     * 标题栏左右两边的文字
     */
    private var titleLeftTv: TextView? = null
    private var titleRightTv: TextView? = null

    /*--------------------------------重写父类函数--------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTitleBackButtonOnClickListener = View.OnClickListener { titleBackClicked() }

        doBeforeSetLayout()

        setContentView(R.layout.activity_base_appcompat)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val color = ContextCompat.getColor(this, R.color.colorPrimary)
            StatusBarUtil.setColor(this, color)
        }
        initThisView()
        initThisData()
        initThisEvents()

        doBeforeInitOthers()
        initViews()
        initViewData()
        initOtherData()
        initEvents()
        doAfterAll()
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     *
     *
     * This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * [.onPrepareOptionsMenu].
     *
     *
     *
     * The default implementation populates the menu with standard system
     * menu items.  These are placed in the [Menu.CATEGORY_SYSTEM] group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     *
     *
     * You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     *
     *
     * When you add items to the menu, you can implement the Activity's
     * [.onOptionsItemSelected] method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see .onPrepareOptionsMenu
     *
     * @see .onOptionsItemSelected
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //创建菜单选项
        return createOptionsMenu(menu)
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     *
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see .onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //菜单的选项被点击时的处理
        return optionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar!!.setNavigationOnClickListener(null)
        toolbar = null
        mTitleBackButtonOnClickListener = null
        contentView!!.removeAllViews()
        contentView = null
    }

    /*--------------------------------抽象函数--------------------------------*/

    /**
     * 标题栏的返回按钮被按下的时候回调此函数
     */
    protected abstract fun titleBackClicked()

    /**
     * 在设置布局之前需要进行的操作
     */
    protected abstract fun doBeforeSetLayout()

    /**
     * 设置布局
     *
     * @return 布局id
     */
    @LayoutRes
    protected abstract fun setLayout(): Int

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    protected abstract fun doBeforeInitOthers()

    /**
     * 初始化布局控件
     */
    protected abstract fun initViews()

    /**
     * 初始化控件数据
     */
    protected abstract fun initViewData()

    /**
     * 初始化其他数据
     */
    protected abstract fun initOtherData()

    /**
     * 初始化事件
     */
    protected abstract fun initEvents()

    /**
     * 在最后进行的操作
     */
    protected abstract fun doAfterAll()

    /**
     * 设置菜单
     *
     * @param menu 菜单
     * @return 只是重写 public boolean onCreateOptionsMenu(Menu menu)
     */
    protected abstract fun createOptionsMenu(menu: Menu): Boolean

    /**
     * 设置菜单监听
     *
     * @param item 菜单的item
     * @return true表示处理了监听事件
     */
    protected abstract fun optionsItemSelected(item: MenuItem): Boolean

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 初始化本类固定的控件
     */
    private fun initThisView() {
        toolbar = findViewById(R.id.toolbar)
        titleView = findViewById(R.id.toolbar_title)
        contentView = findViewById(R.id.base_frame_content)
        rootView = findViewById(R.id.base_root_view)
        titleLeftTv = findViewById(R.id.title_left_text)
        titleRightTv = findViewById(R.id.title_right_text)
    }

    /**
     * 初始化本类固定的数据
     */
    private fun initThisData() {
        toolbar!!.setNavigationIcon(R.drawable.back)
        titleView = findViewById(R.id.toolbar_title)
        titleView!!.setText(R.string.app_name)
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar.setDisplayShowTitleEnabled(false)
        }
        val view = layoutInflater.inflate(setLayout(), null)
        contentView!!.addView(view)
    }

    /**
     * 初始化本类固定的事件
     */
    private fun initThisEvents() {
        toolbar!!.setNavigationOnClickListener(mTitleBackButtonOnClickListener)
    }

    /*--------------------------------子类可用函数--------------------------------*/

    /**
     * 设置title返回按钮的处理事件
     *
     * @param titleBackButtonOnClickListener 返回按钮的处理事件
     */
    protected fun setTitleBackOnClickListener(titleBackButtonOnClickListener: View.OnClickListener) {
        mTitleBackButtonOnClickListener = titleBackButtonOnClickListener
    }

    /**
     * 设置标题栏左边的文字
     *
     * @param text 文字
     */
    protected fun setTitleLeftText(text: String) {
        titleLeftTv!!.text = text
    }

    /**
     * 设置标题栏左边的文字
     *
     * @param textRes 文字
     */
    protected fun setTitleLeftText(@StringRes textRes: Int) {
        titleLeftTv!!.setText(textRes)
    }

    /**
     * 设置标题栏左边文字的颜色
     *
     * @param color 文字的颜色
     */
    protected fun setTitleLeftTextColor(@ColorInt color: Int) {
        titleLeftTv!!.setTextColor(color)
    }

    /**
     * 设置标题栏左边文字的颜色
     *
     * @param colorRes 文字的颜色
     */
    protected fun setTitleLeftTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        setTitleLeftTextColor(color)
    }


    /**
     * 设置标题栏右边的文字
     *
     * @param text 文字
     */
    protected fun setTitleRightText(text: String) {
        titleRightTv!!.text = text
    }

    /**
     * 设置标题栏右边的文字
     *
     * @param textRes 文字
     */
    protected fun setTitleRightText(@StringRes textRes: Int) {
        titleRightTv!!.setText(textRes)
    }

    /**
     * 设置标题栏左边文字的颜色
     *
     * @param color 文字的颜色
     */
    protected fun setTitleRightTextColor(@ColorInt color: Int) {
        titleRightTv!!.setTextColor(color)
    }

    /**
     * 设置标题栏左边文字的颜色
     *
     * @param colorRes 文字的颜色
     */
    protected fun setTitleRightTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        setTitleRightTextColor(color)
    }

    /**
     * 隐藏标题栏的返回按钮
     */
    protected fun hideTitleBackButton() {
        if (null == toolbar) {
            throw RuntimeException("toolbar is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.navigationIcon = null
    }

    /**
     * 显示标题栏的返回按钮
     */
    protected fun showTitleBackButton() {
        if (null == toolbar) {
            throw RuntimeException("toolbar is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.setNavigationIcon(R.drawable.back)
    }

    /**
     * 设置根布局（整个Activity）的背景色
     *
     * @param drawableRes 背景色
     */
    protected fun setRootBackGroundResource(drawableRes: Int) {
        rootView!!.setBackgroundResource(drawableRes)
    }

    /**
     * 设置根布局（整个Activity）的背景色
     *
     * @param color 背景色
     */
    protected fun setRootBackGroundColor(@ColorInt color: Int) {
        rootView!!.setBackgroundColor(color)
    }

    /**
     * 隐藏标题栏
     */
    protected fun hideTitleBar() {
        val supportActionBar = supportActionBar
            ?: throw RuntimeException("supportActionBar is null!Please invoke this method after method \"setLayout()\"")
        supportActionBar.hide()
    }

    /**
     * 显示标题栏
     */
    protected fun showTitleBar() {
        val supportActionBar = supportActionBar
        supportActionBar?.show()
    }

    /**
     * 设置标题栏的内容
     *
     * @param titleRes 标题栏的资源id
     */
    protected fun setTitleText(@StringRes titleRes: Int) {
        titleView!!.setText(titleRes)
    }

    /**
     * 设置标题栏的内容
     *
     * @param titleText 标题栏的文本
     */
    protected fun setTitleText(titleText: String) {
        titleView!!.text = titleText
    }

    /**
     * 设置标题栏文本颜色
     *
     * @param color 文本颜色
     */
    protected fun setTitleTextColor(@ColorInt color: Int) {
        if (titleView == null) {
            throw RuntimeException("titleView is null!Please invoke this method after method \"setLayout()\"")
        }
        titleView!!.setTextColor(color)
    }

    /**
     * 设置标题栏的背景色
     *
     * @param color 标题栏的背景色
     */
    protected fun setTitleBackgroundColor(@ColorInt color: Int) {
        if (toolbar == null) {
            throw RuntimeException("appBarLayout is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.setBackgroundColor(color)
    }

    /**
     * 设置标题栏的背景
     *
     * @param drawable 标题栏的背景
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected fun setTitleBackgroundDrawable(drawable: Drawable) {
        if (toolbar == null) {
            throw RuntimeException("appBarLayout is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.background = drawable
    }

    /**
     * 设置标题栏的背景
     *
     * @param drawableRes 标题栏的背景
     */
    protected fun setTitleBackgroundResource(@DrawableRes drawableRes: Int) {
        if (toolbar == null) {
            throw RuntimeException("appBarLayout is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.setBackgroundResource(drawableRes)
    }

    /**
     * 设置标题栏文本颜色
     *
     * @param colorRes 文本颜色
     */
    protected fun setTitleTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        setTitleTextColor(color)
    }

    /**
     * 设置标题栏左边的图片
     *
     * @param drawableId 标题栏左边的图片资源id
     */
    protected fun setTitleBackIcon(@DrawableRes drawableId: Int) {
        toolbar!!.setNavigationIcon(drawableId)
    }

    /**
     * 设置标题栏左边的文本的点击事件
     *
     * @param onClickListener 标题栏左边的文本的点击事件
     */
    protected fun setOnTitleLeftTextClickListener(onClickListener: View.OnClickListener) {
        titleLeftTv!!.setOnClickListener(onClickListener)
    }

    /**
     * 设置标题栏右边的文本的点击事件
     *
     * @param onClickListener 标题栏的文本的点击事件
     */
    protected fun setOnTitleRightTextClickListener(onClickListener: View.OnClickListener) {
        titleRightTv!!.setOnClickListener(onClickListener)
    }
}
