package com.jackiepenghe.baselibraryforkotlin

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button

import com.jackiepenghe.baselibraryforkotlin.activities.sample.*
import com.jackiepenghe.baselibraryforkotlin.activity.BaseAppCompatActivity
import com.jackiepenghe.baselibraryforkotlin.tools.Tool


/**
 * @author jacke
 */
class MainActivity : BaseAppCompatActivity() {

    private var baseAppcompatActivityBtn: Button? = null
    private var baseDrawerActivityBtn: Button? = null
    private var baseFragmentBtn: Button? = null
    private var basePopupWindowBtn: Button? = null
    private var allPurposeAdapterBtn: Button? = null
    private var homeWatcherBtn: Button? = null
    private var toastTestBtn: Button? = null

    private val onClickListener = View.OnClickListener { view ->
        var intent: Intent? = null
        when (view.id) {
            R.id.all_purpose_adapter -> intent = Intent(this@MainActivity, AllPurposeAdapterActivity::class.java)
            R.id.base_appcompat_activity -> intent = Intent(this@MainActivity, SampleBaseAppcompatActivity::class.java)
            R.id.base_drawer_activity -> intent = Intent(this@MainActivity, SampleBaseDrawerActivity::class.java)
            R.id.base_fragment -> intent = Intent(this@MainActivity, SampleBaseFragmentActivity::class.java)
            R.id.base_popup_window -> intent = Intent(this@MainActivity, SampleBasePopupWindowActivity::class.java)
            R.id.home_watcher -> intent = Intent(this@MainActivity, HomeWatcherActivity::class.java)
            R.id.toast_test -> intent = Intent(this@MainActivity, ToastTestActivity::class.java)
            R.id.title_left_text -> Tool.warnOut(TAG, "文字左被点击")
            R.id.title_right_text -> Tool.warnOut(TAG, "文字右被点击")
            else -> {
            }
        }
        if (intent != null) {
            startActivity(intent)
        }
    }

    override fun titleBackClicked() {
        onBackPressed()
    }

    /**
     * 在设置布局之前需要进行的操作
     */
    override fun doBeforeSetLayout() {

    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    override fun setLayout(): Int {
        return R.layout.activity_main
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    override fun doBeforeInitOthers() {
        setTitleLeftText("文字左")
        setTitleRightText("文字右")
        hideTitleBackButton()
    }

    /**
     * 初始化布局控件
     */
    override fun initViews() {
        baseAppcompatActivityBtn = findViewById(R.id.base_appcompat_activity)
        baseDrawerActivityBtn = findViewById(R.id.base_drawer_activity)
        baseFragmentBtn = findViewById(R.id.base_fragment)
        basePopupWindowBtn = findViewById(R.id.base_popup_window)
        allPurposeAdapterBtn = findViewById(R.id.all_purpose_adapter)
        homeWatcherBtn = findViewById(R.id.home_watcher)
        toastTestBtn = findViewById(R.id.toast_test)
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
        baseAppcompatActivityBtn!!.setOnClickListener(onClickListener)
        baseDrawerActivityBtn!!.setOnClickListener(onClickListener)
        baseFragmentBtn!!.setOnClickListener(onClickListener)
        basePopupWindowBtn!!.setOnClickListener(onClickListener)
        allPurposeAdapterBtn!!.setOnClickListener(onClickListener)
        homeWatcherBtn!!.setOnClickListener(onClickListener)
        toastTestBtn!!.setOnClickListener(onClickListener)
        setOnTitleLeftTextClickListener(onClickListener)
        setOnTitleRightTextClickListener(onClickListener)
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

        private val TAG = MainActivity::class.java.simpleName
    }
}
