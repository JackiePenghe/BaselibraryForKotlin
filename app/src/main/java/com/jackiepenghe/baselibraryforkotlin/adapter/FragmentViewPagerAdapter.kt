package com.jackiepenghe.baselibraryforkotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentViewPagerAdapter(
    fm: FragmentManager,
    private val fragments: Array<Fragment>,
    private val pageTitles: Array<String>
) : FragmentPagerAdapter(fm) {

    init {
        if (fragments.size > pageTitles.size) {
            throw IllegalStateException("The length of array parameter \"pageTitles\" must be equals or more than array parameter \"fragments\"")
        }
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position 当前的位置
     */
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return fragments.size
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitles[position]
    }
}
