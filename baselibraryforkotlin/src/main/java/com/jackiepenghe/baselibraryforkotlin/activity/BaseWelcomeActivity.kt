package com.jackiepenghe.baselibraryforkotlin.activity

import android.support.annotation.DrawableRes
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.tools.StatusBarUtil

/**
 * 欢迎页
 *
 * @author alm
 */

abstract class BaseWelcomeActivity : BaseAppCompatActivity() {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 欢迎页的图片
     */
    private var imageView: ImageView? = null
    /**
     * 欢迎页图片的动画
     */
    private var animation: Animation? = null
    /**
     * 欢迎页图片动画的监听
     */
    private var animationListener: Animation.AnimationListener? = null

    /*--------------------------------实现父类函数--------------------------------*/

    /**
     * 标题栏的返回按钮被按下的时候回调此函数
     */
    override fun titleBackClicked() {

    }


    /**
     * 在设置布局之前需要进行的操作
     */
    override fun doBeforeSetLayout() {
        animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                doAfterAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        }
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    override fun setLayout(): Int {
        return R.layout.activity_welcome
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    override fun doBeforeInitOthers() {
        hideTitleBar()
        StatusBarUtil.hideFakeStatusBarView(this@BaseWelcomeActivity)
    }

    /**
     * 初始化布局控件
     */
    override fun initViews() {
        imageView = findViewById(R.id.welcome_activity_image_view)
    }

    /**
     * 初始化控件数据
     */
    override fun initViewData() {
        val imageSource = setImageViewSource()
        if (imageSource != 0) {
            try {
                imageView!!.setImageResource(imageSource)
            } catch (e: Exception) {
                imageView!!.setImageResource(R.drawable.com_jackiepenghe_baselibrary_default_main)
            }

        } else {
            imageView!!.setImageResource(R.drawable.com_jackiepenghe_baselibrary_default_main)
        }
        animation =
                AnimationUtils.loadAnimation(this@BaseWelcomeActivity, R.anim.com_jackiepenghe_baselibrary_anim_welcome)
    }

    /**
     * 初始化其他数据
     */
    override fun initOtherData() {

    }

    /**
     * 初始化事件
     */
    override fun initEvents() {
        animation!!.setAnimationListener(animationListener)
        imageView!!.startAnimation(animation)
    }

    /**
     * 在最后进行的操作
     */
    override fun doAfterAll() {

    }

    /**
     * 设置菜单
     *
     * @param menu 菜单
     * @return 只是重写 public boolean onCreateOptionsMenu(Menu menu)
     */
    override fun createOptionsMenu(menu: Menu): Boolean {
        return false
    }

    /**
     * 设置菜单监听
     *
     * @param item 菜单的item
     * @return true表示处理了监听事件
     */
    override fun optionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    /*--------------------------------重写父类函数--------------------------------*/

    override fun onDestroy() {
        super.onDestroy()
        imageView = null
        animation = null
        animationListener = null
    }

    /*--------------------------------抽象函数--------------------------------*/

    /**
     * 当动画执行完成后调用这个函数
     */
    protected abstract fun doAfterAnimation()

    /**
     * 设置ImageView的图片资源
     *
     * @return 图片资源ID
     */
    @DrawableRes
    protected abstract fun setImageViewSource(): Int
}
