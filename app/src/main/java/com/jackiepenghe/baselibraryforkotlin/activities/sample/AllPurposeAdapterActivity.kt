package com.jackiepenghe.baselibraryforkotlin.activities.sample

import android.view.Menu
import android.view.MenuItem
import android.widget.ListView

import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.activity.BaseAppCompatActivity
import com.jackiepenghe.baselibraryforkotlin.adapter.SampleAdapter

import java.util.ArrayList

/**
 * @author jacke
 */
class AllPurposeAdapterActivity : BaseAppCompatActivity() {

    private var listView: ListView? = null
    private val dataList = ArrayList<String>()


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
        for (i in 0..3) {
            dataList.add("test$i")
        }
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    override fun setLayout(): Int {
        return R.layout.activity_all_purpose_adapter
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    override fun doBeforeInitOthers() {}

    /**
     * 初始化布局控件
     */
    override fun initViews() {
        listView = findViewById(R.id.list_view)
    }

    /**
     * 初始化控件数据
     */
    override fun initViewData() {
        val adapter = SampleAdapter(dataList)
        listView!!.adapter = adapter
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
}
