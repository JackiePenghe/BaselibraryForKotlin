package com.jackiepenghe.baselibraryforkotlin.activities.sample

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.activity.BaseDrawerActivity
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

/**
 * @author jackie
 */
class SampleBaseDrawerActivity : BaseDrawerActivity() {

    /**
     * 在设置布局之前需要进行的操作
     */
    override fun doBeforeSetLayout() {
        //在Activity中，有时候会设置一些属性，这些属性在setContentView()之前，可在这里实现
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    override fun setLayout(): Int {
        //等同于setContentView()
        return R.layout.activity_sample_base_drawer
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    override fun doBeforeInitOthers() {
        //重命名标题栏的内容
        setTitleText(R.string.base_drawer_activity)
        //设置标题栏文本的颜色
        setTitleTextColorRes(android.R.color.white)
    }

    /**
     * 初始化布局控件
     */
    override fun initViews() {
        //通常在这里findViewById()
    }

    /**
     * 初始化控件数据
     */
    override fun initViewData() {

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

    }

    /**
     * 在最后执行的操作
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

      /**
     * DrawerLayout的滑动监听
     *
     * @param drawerView  侧边栏
     * @param slideOffset 滑动距离
     */
    override fun drawerSlide(drawerView: View, slideOffset: Float) {

    }

    /**
     * DrawerLayout已经完全打开了
     *
     * @param drawerView 侧边栏
     */
    override fun drawerOpened(drawerView: View) {

    }

    /**
     * DrawerLayout已经完全关闭了
     *
     * @param drawerView 侧边栏
     */
    override fun drawerClosed(drawerView: View) {

    }

    /**
     * DrawerLayout的状态改变了
     *
     * @param newState 新的状态
     */
    override fun drawerStateChanged(newState: Int) {

    }

    /**
     * 侧边栏的监听
     *
     * @param menuItem 侧边栏item
     * @return true表示处理了监听事件
     */
    override fun navigationItemSelected(menuItem: MenuItem): Boolean {
        Tool.toastL(this, "侧边栏的菜单被点击了：" + menuItem.title)
        return true
    }

    /**
     * 获取侧边栏的头部的资源id，如果不设置此选项，会以默认的头部布局进行填充
     *
     * @return 侧边栏的头部的资源id
     */
    override fun setNavigationViewHeaderViewLayoutResId(): Int {
        return 0
    }

    /**
     * 获取侧边栏的菜单的资源id，如果不设置此选项，会以默认的菜单进行填充
     *
     * @return 侧边栏的菜单的资源id
     */
    override fun setNavigationMenuResId(): Int {
        return 0
    }
}
