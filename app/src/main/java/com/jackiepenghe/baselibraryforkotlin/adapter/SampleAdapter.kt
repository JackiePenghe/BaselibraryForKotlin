package com.jackiepenghe.baselibraryforkotlin.adapter

import com.jackiepenghe.baselibraryforkotlin.tools.ViewHolder
import java.util.*

/**
 * @author alm
 * @date 2017/10/12
 * AllPurposeAdapter举例
 */

class SampleAdapter(dataList: ArrayList<String>) : BasePurposeAdapter<String>(dataList, android.R.layout.simple_list_item_2) {
    /**
     * 每一个单独的选项的内容设置
     *
     * @param viewHolder ViewHolder
     * @param position   position
     * @param item       item
     */
    override fun convert(viewHolder: ViewHolder?, position: Int, item: String) {
        viewHolder?.setText(android.R.id.text1, item)
                ?.setText(android.R.id.text2, item)
    }
}
