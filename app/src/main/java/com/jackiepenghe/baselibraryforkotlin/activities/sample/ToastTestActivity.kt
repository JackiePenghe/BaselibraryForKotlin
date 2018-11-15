package com.jackiepenghe.baselibraryforkotlin.activities.sample

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.activity.BaseAppCompatActivity
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

/**
 * Toast测试
 *
 * @author jackie
 */
class ToastTestActivity : BaseAppCompatActivity() {

    private var longTimeBtn: Button? = null
    private var shortTimeBtn: Button? = null
    private var time100Btn: Button? = null
    private var time500Btn: Button? = null

    private var reuseRadioGroup: RadioGroup? = null
    private val onClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.long_time -> Tool.toastL(this@ToastTestActivity, R.string.long_time)
            R.id.short_time -> Tool.toastS(this@ToastTestActivity, R.string.short_time)
            R.id.time_100 -> Tool.toast(this@ToastTestActivity, R.string.time_100, 100)
            R.id.time_500 -> Tool.toast(this@ToastTestActivity, R.string.time_500, 500)
            else -> {
            }
        }
    }
    private val onCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        when (group.id) {
            R.id.reuse_toast_group -> onReuseGroupButtonCheckedChanged(checkedId)
            else -> {
            }
        }
    }

    private fun onReuseGroupButtonCheckedChanged(checkedId: Int) {
        when (checkedId) {
            R.id.open_reuse -> Tool.isToastReuse = true
            R.id.close_reuse -> Tool.isToastReuse = false
            else -> {
            }
        }
    }

    /**
     * 标题栏的返回按钮被按下的时候回调此函数
     */
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
        return R.layout.activity_toast_test
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    override fun doBeforeInitOthers() {

    }

    /**
     * 初始化布局控件
     */
    override fun initViews() {
        longTimeBtn = findViewById(R.id.long_time)
        shortTimeBtn = findViewById(R.id.short_time)
        time100Btn = findViewById(R.id.time_100)
        time500Btn = findViewById(R.id.time_500)
        reuseRadioGroup = findViewById(R.id.reuse_toast_group)
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
        reuseRadioGroup!!.setOnCheckedChangeListener(onCheckedChangeListener)
        longTimeBtn!!.setOnClickListener(onClickListener)
        shortTimeBtn!!.setOnClickListener(onClickListener)
        time100Btn!!.setOnClickListener(onClickListener)
        time500Btn!!.setOnClickListener(onClickListener)
    }

    /**
     * 在最后进行的操作
     */
    override fun doAfterAll() {
        reuseRadioGroup!!.check(R.id.open_reuse)
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
}
