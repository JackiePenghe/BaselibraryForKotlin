package com.jackiepenghe.baselibraryforkotlin.activities.sample

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.jackiepenghe.baselibraryforkotlin.R
import com.jackiepenghe.baselibraryforkotlin.adapter.FragmentViewPagerAdapter
import com.jackiepenghe.baselibraryforkotlin.fragment.SampleFragment1
import com.jackiepenghe.baselibraryforkotlin.fragment.SampleFragment2
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

/**
 * @author alm
 */
class SampleBaseFragmentActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_base_fragment)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        button = findViewById(R.id.button)

        val sampleFragment1 = SampleFragment1()
        val sampleFragment2 = SampleFragment2()
        val fragments = arrayOf<Fragment>(sampleFragment1, sampleFragment2)
        val titles = arrayOf("fragment1", "fragment2")
        val fragmentViewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager, fragments, titles)
        viewPager!!.adapter = fragmentViewPagerAdapter
        tabLayout!!.setupWithViewPager(viewPager)

        button!!.setOnClickListener {
            if (sampleFragment1.isVisibilityForUser) {
                Tool.warnOut(TAG, "sampleFragment1 可见")
            } else {
                Tool.warnOut(TAG, "sampleFragment1 不可见")
            }

            if (sampleFragment2.isVisibilityForUser) {
                Tool.warnOut(TAG, "sampleFragment2 可见")
            } else {
                Tool.warnOut(TAG, "sampleFragment2 不可见")
            }
        }
    }

    companion object {

        private val TAG = SampleBaseFragmentActivity::class.java.simpleName
    }
}
