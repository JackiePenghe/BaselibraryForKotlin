package com.jackiepenghe.baselibraryforkotlin.activity

import android.annotation.TargetApi
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.*
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.tools.StatusBarUtil

/**
 * 带有侧边栏的Activity基类
 *
 * @author alm
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class BaseDrawerActivity : AppCompatActivity() {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 标题
     */
    private var toolbar: Toolbar? = null
    /**
     * 抽屉布局
     */
    private var drawerLayout: DrawerLayout? = null
    /**
     * 左上角可展示侧边栏状态的小控件
     */
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    /**
     * 侧边栏
     */
    private var navigationView: NavigationView? = null
    /**
     * 标题文字
     */
    private var titleView: TextView? = null
    /**
     * 记录当前侧边栏的打开状态
     */
    /**
     * 获取当前侧边栏的打开状态
     *
     * @return 当前侧边栏的打开状态
     */
    protected var isDrawerOpen: Boolean = false
        private set

    /**
     * 子类继承此类之后，设置的布局都在FrameLayout中
     */
    /**
     * 获取Activity布局的整个布局
     *
     * @return Activity布局的整个布局
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var contentView: FrameLayout? = null
        private set

    /**
     * 抽屉布局侧边栏的相关监听
     */
    private var drawerListener: DrawerLayout.DrawerListener? = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            drawerSlide(drawerView, slideOffset)
        }

        override fun onDrawerOpened(drawerView: View) {
            drawerOpened(drawerView)
            isDrawerOpen = true
        }

        override fun onDrawerClosed(drawerView: View) {
            drawerClosed(drawerView)
            isDrawerOpen = false
        }

        override fun onDrawerStateChanged(newState: Int) {
            when (newState) {
                DrawerLayout.STATE_DRAGGING, DrawerLayout.STATE_SETTLING -> isDrawerOpen = true
                DrawerLayout.STATE_IDLE -> {
                }
                else -> {
                }
            }
            drawerStateChanged(newState)
        }
    }
    /**
     * 侧边栏的选项点击监听
     */
    private var onNavigationItemSelectedListener: NavigationView.OnNavigationItemSelectedListener? =
        NavigationView.OnNavigationItemSelectedListener { item -> navigationItemSelected(item) }
    /*--------------------------------重写父类函数--------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBeforeSetLayout()

        setContentView(R.layout.activity_base_drawer)

        //如果SDK版本低于21（安卓5.0，主动设置标题栏颜色以实现沉浸式）
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val color = ContextCompat.getColor(this, R.color.colorPrimary)
            StatusBarUtil.setColor(this, color)
        }
        toolbar = findViewById(R.id.toolbar)
        titleView = findViewById(R.id.toolbar_title)
        if (titleView != null) {
            titleView!!.setText(R.string.app_name)
        }
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar.setDisplayShowTitleEnabled(false)
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle!!)
        drawerLayout!!.addDrawerListener(drawerListener!!)
        actionBarDrawerToggle!!.syncState()
        navigationView = findViewById(R.id.nav_view)
        val headerView: View = try {
            View.inflate(this@BaseDrawerActivity, setNavigationViewHeaderViewLayoutResId(), null)
        } catch (e: Exception) {
            View.inflate(this@BaseDrawerActivity, R.layout.nav_header_main, null)
        }

        navigationView!!.addHeaderView(headerView)
        try {
            navigationView!!.inflateMenu(setNavigationMenuResId())
        } catch (e: Exception) {
            navigationView!!.inflateMenu(R.menu.com_jackiepenghe_activity_main_drawer)
        }

        navigationView!!.setNavigationItemSelectedListener(onNavigationItemSelectedListener)
        contentView = findViewById(R.id.base_frame_content)
        val view = layoutInflater.inflate(setLayout(), null)
        contentView!!.addView(view)

        doBeforeInitOthers()
        initViews()
        initViewData()
        initOtherData()
        initEvents()
        doAfterAll()
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar!!.setNavigationOnClickListener(null)
        toolbar!!.navigationIcon = null
        toolbar = null
        drawerLayout!!.removeAllViews()
        drawerLayout!!.removeDrawerListener(drawerListener!!)
        drawerLayout!!.removeDrawerListener(actionBarDrawerToggle!!)
        drawerLayout = null
        drawerListener = null
        actionBarDrawerToggle = null
        navigationView!!.setNavigationItemSelectedListener(null)
        navigationView = null
        onNavigationItemSelectedListener = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return createOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return optionsItemSelected(item)
    }

    /*--------------------------------抽象函数--------------------------------*/

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
     * 在最后执行的操作
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

    /**
     * DrawerLayout的滑动监听
     *
     * @param drawerView  侧边栏
     * @param slideOffset 滑动距离
     */
    protected abstract fun drawerSlide(drawerView: View, slideOffset: Float)

    /**
     * DrawerLayout已经完全打开了
     *
     * @param drawerView 侧边栏
     */
    protected abstract fun drawerOpened(drawerView: View)

    /**
     * DrawerLayout已经完全关闭了
     *
     * @param drawerView 侧边栏
     */
    protected abstract fun drawerClosed(drawerView: View)

    /**
     * DrawerLayout的状态改变了
     *
     * @param newState 新的状态
     */
    protected abstract fun drawerStateChanged(newState: Int)

    /**
     * 侧边栏的监听
     *
     * @param menuItem 侧边栏item
     * @return true表示处理了监听事件
     */
    protected abstract fun navigationItemSelected(menuItem: MenuItem): Boolean

    /**
     * 获取侧边栏的头部的资源id，如果不设置此选项，会以默认的头部布局进行填充
     *
     * @return 侧边栏的头部的资源id
     */
    @LayoutRes
    protected abstract fun setNavigationViewHeaderViewLayoutResId(): Int

    /**
     * 获取侧边栏的菜单的资源id，如果不设置此选项，会以默认的菜单进行填充
     *
     * @return 侧边栏的菜单的资源id
     */
    @MenuRes
    protected abstract fun setNavigationMenuResId(): Int

    /*--------------------------------子类可用函数--------------------------------*/

    /**
     * 关闭侧边栏
     */
    protected fun closeDrawer() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        }
    }

    /**
     * 设置标题栏的内容
     *
     * @param titleResId 标题栏的资源id
     */
    protected fun setTitleText(@StringRes titleResId: Int) {
        titleView!!.setText(titleResId)
    }

    /**
     * 设置标题栏的内容
     *
     * @param titleText 标题栏内容
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
     * 设置标题栏文本颜色
     *
     * @param colorRes 文本颜色
     */
    protected fun setTitleTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        setTitleTextColor(color)
    }

    /**
     * 设置标题栏背景色
     *
     * @param color 标题栏背景色
     */
    protected fun setTitleBackgroundColor(@ColorInt color: Int) {
        if (toolbar == null) {
            throw RuntimeException("appBarLayout is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.setBackgroundColor(color)
    }

    /**
     * 设置标题栏背景
     *
     * @param drawable 标题栏背景
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected fun setTitleBackgroundDrawable(drawable: Drawable) {
        if (toolbar == null) {
            throw RuntimeException("appBarLayout is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.background = drawable
    }

    /**
     * 设置标题栏背景
     *
     * @param drawableRes 标题栏背景
     */
    protected fun setTitleBackgroundResource(@DrawableRes drawableRes: Int) {
        if (toolbar == null) {
            throw RuntimeException("appBarLayout is null!Please invoke this method after method \"setLayout()\"")
        }
        toolbar!!.setBackgroundResource(drawableRes)
    }

    /**
     * 获取整个抽屉布局
     *
     * @return 整个抽屉布局
     */
    protected fun getDrawerLayout(): DrawerLayout {
        return findViewById(R.id.drawer_layout)
    }

    /**
     * 获取整个侧边栏
     *
     * @return 整个侧边栏
     */
    protected fun getNavigationView(): NavigationView {
        return findViewById(R.id.nav_view)
    }
}
