package com.jackiepenghe.baselibraryforkotlin.activities.sample

import android.graphics.Color
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.activity.BaseAppCompatActivity
import com.jackiepenghe.baselibraryforkotlin.tools.OSHelper
import com.jackiepenghe.baselibraryforkotlin.tools.StatusBarUtil
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

/**
 * @author jacke
 */
class SampleBaseAppcompatActivity : BaseAppCompatActivity() {


    /**
     * 标题栏的返回按钮被按下的时候回调此函数
     */
    override fun titleBackClicked() {
        //一般情况下直接调用onBackPressed即可
        onBackPressed()
    }

    /**
     * 在设置布局之前需要进行的操作
     */
    override fun doBeforeSetLayout() {
        //在Activity中，有时候会设置一些属性，这些属性在setContentView()之前，可在这里实现
        if (OSHelper.isEMUI) {
            OSHelper.miuiSetStatusBarLightMode(window, true)
            StatusBarUtil.setColor(this, Color.WHITE, 60)
        } else if (OSHelper.isFlyme) {
            OSHelper.flymeSetStatusBarLightMode(window, true)
            StatusBarUtil.setColor(this, Color.WHITE, 60)
        } else {
            Log.w(TAG, "不是miui或者flyme,不设置状态栏字体深色")
        }
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    override fun setLayout(): Int {
        //等同于setContentView()
        return R.layout.activity_sample_base_appcompat
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    override fun doBeforeInitOthers() {

        val bytes = Tool.intToBytesLength4(0x9ABCDEF)
        Tool.warnOut(TAG, "bytes = " + Tool.bytesToHexStr(bytes))
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
        //在这里设置View的数据。如：ListView.setAdapter()
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
        //在这里设置监听事件
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

    companion object {

        private val TAG = SampleBaseAppcompatActivity::class.java.simpleName
    }
}
