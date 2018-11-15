package com.jackiepenghe.baselibraryforkotlin.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.jackiepenghe.baselibraryforkotlin.tools.ViewHolder

import java.util.ArrayList

/**
 * ListView万能适配器
 *
 * @author alm
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class BasePurposeAdapter<T>(
    protected var mDataList: ArrayList<T>,
    @param:LayoutRes @field:LayoutRes private val mItemLayoutId: Int
) : BaseAdapter() {

    /*--------------------------------成员变量--------------------------------*/

    /**
     * 上下文弱引用
     */
    protected var mContext: Context? = null

    /**
     * 自定义适配器总数(适配器会根据这个数与数据源的比例来显示)
     */
    private var mCountSum = -1

    /*--------------------------------实现父类函数--------------------------------*/

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    override fun getCount(): Int {
        return if (mCountSum == -1) {
            mDataList.size
        } else {
            mCountSum
        }
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    override fun getItem(position: Int): T {
        return if (mCountSum == -1) {
            mDataList[position]
        } else {
            mDataList[mCountSum % mDataList.size]
        }
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * [LayoutInflater.inflate]
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     * we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     * is non-null and of an appropriate type before using. If it is not possible to convert
     * this view to display the correct data, this method can create a new view.
     * Heterogeneous lists can specify their number of view types, so that this View is
     * always of the right type (see [.getViewTypeCount] and
     * [.getItemViewType]).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        if (mContext == null) {
            mContext = parent.context
        }
        val viewHolder = getViewHolder(position, convertView, parent)
        convert(viewHolder, position, getItem(position))
        return viewHolder?.convertView
    }

    /*--------------------------------公开函数--------------------------------*/

    /**
     * 更新数据源并刷新适配器进行显示
     *
     * @param dataList 新的数据源
     */
    fun refresh(dataList: ArrayList<T>) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    /**
     * 删除数据源中指定位置的数据
     *
     * @param position 指定删除位置
     */
    fun deleteListByPosition(position: Int) {
        if (position >= mDataList.size) {
            return
        }

        mDataList.removeAt(position)
        notifyDataSetChanged()
    }

    /**
     * 设置自定义数据总量
     *
     * @param countSum 自定义数据总量
     * @return AllPurposeAdapter本类对象
     */
    fun setCount(countSum: Int): BasePurposeAdapter<T> {
        mCountSum = countSum
        return this
    }

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 获取ViewHolder
     *
     * @param position    当前位置
     * @param convertView 复用的View
     * @param parent      父布局
     * @return ViewHolder
     */
    private fun getViewHolder(position: Int, convertView: View?, parent: ViewGroup): ViewHolder? {
        val context: Context? = if (convertView != null) {
            convertView.context
        } else {
            mContext
        }

        return if (context != null) {
            ViewHolder[context, convertView, parent, mItemLayoutId, position]
        } else {
            null
        }
    }

    /*--------------------------------抽象函数--------------------------------*/

    /**
     * 每一个单独的选项的内容设置
     *
     * @param viewHolder ViewHolder
     * @param position   position
     * @param item       item
     */
    protected abstract fun convert(viewHolder: ViewHolder?, position: Int, item: T)
}
