package com.jackiepenghe.baselibraryforkotlin.view.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View

import java.util.ArrayList

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * RecyclerView的装饰（分割线）
 * @author jacke
 */

class DefaultItemDecoration @JvmOverloads constructor(
    @ColorInt color: Int,
    private val mDividerWidth: Int = 2,
    private val mDividerHeight: Int = 2,
    excludeViewType: IntArray = intArrayOf(-1)
) : RecyclerView.ItemDecoration() {

    /**
     * 颜色图像
     */
    private val mDivider: Drawable
    /**
     * 保存ViewType集合
     */
    private val mViewTypeList = ArrayList<Int>()

    init {

        mDivider = ColorDrawable(color)
        for (i in excludeViewType) {
            mViewTypeList.add(i)
        }
    }

    /*--------------------------------重写父类函数--------------------------------*/

    /**
     * Retrieve any offsets for the given item. Each field of `outRect` specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     *
     *
     *
     *
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of `outRect` (left, top, right, bottom) to zero
     * before returning.
     *
     *
     *
     *
     * If you need to access Adapter for additional data, you can call
     * [RecyclerView.getChildAdapterPosition] to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position < 0) {
            return
        }
        val adapter = parent.adapter ?: return
        if (mViewTypeList.contains(adapter.getItemViewType(position))) {
            outRect.set(0, 0, 0, 0)
            return
        }

        val columnCount = getSpanCount(parent)
        val childCount = adapter.itemCount

        val firstRaw = isFirstRaw(position, columnCount)
        val lastRaw = isLastRaw(position, columnCount, childCount)
        val firstColumn = isFirstColumn(position, columnCount)
        val lastColumn = isLastColumn(position, columnCount)

        if (columnCount == 1) {
            when {
                firstRaw -> outRect.set(0, 0, 0, mDividerHeight / 2)
                lastRaw -> outRect.set(0, mDividerHeight / 2, 0, 0)
                else -> outRect.set(0, mDividerHeight / 2, 0, mDividerHeight / 2)
            }
        } else {
            // right, bottom
            if (firstRaw && firstColumn) {
                outRect.set(0, 0, mDividerWidth / 2, mDividerHeight / 2)
            } else if (firstRaw && lastColumn) {
                outRect.set(mDividerWidth / 2, 0, 0, mDividerHeight / 2)
            } else if (firstRaw) {
                outRect.set(mDividerWidth / 2, 0, mDividerWidth / 2, mDividerHeight / 2)
            } else if (lastRaw && firstColumn) {
                outRect.set(0, mDividerHeight / 2, mDividerWidth / 2, 0)
            } else if (lastRaw && lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, 0, 0)
            } else if (lastRaw) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth / 2, 0)
            } else if (firstColumn) {
                outRect.set(0, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight / 2)
            } else if (lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, 0, mDividerHeight / 2)
            } else {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight / 2)
            }// left, bottom.
            // left, top, bottom
            // top, right, bottom
            // left, top, right
            // left, top
            // top, right
            // left, right, bottom
            // left, right
        }
    }

    /**
     * Draw any appropriate decorations into the Canvas supplied to the RecyclerView.
     * Any content drawn by this method will be drawn before the item views are drawn,
     * and will thus appear underneath the views.
     *
     * @param c      Canvas to draw into
     * @param parent RecyclerView this ItemDecoration is drawing into
     * @param state  The current state of RecyclerView
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    /*--------------------------------私有函数--------------------------------*/

    /**
     * 根据不同的LayoutManager获取SpanCount
     * @param parent RecyclerView
     * @return SpanCount
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.spanCount
        }
        return 1
    }

    /**
     * 检查是不是第一行
     * @param position 位置
     * @param columnCount 列数
     * @return true代表该位置在第一行
     */
    private fun isFirstRaw(position: Int, columnCount: Int): Boolean {
        return position < columnCount
    }

    /**
     * 检查是不是最后一行
     * @param position 位置
     * @param columnCount 列数
     * @param childCount 子项数量
     * @return true代表该位置在最后一行
     */
    private fun isLastRaw(position: Int, columnCount: Int, childCount: Int): Boolean {
        return if (columnCount == 1) {
            position + 1 == childCount
        } else {
            val lastRawItemCount = childCount % columnCount
            val rawCount = (childCount - lastRawItemCount) / columnCount + if (lastRawItemCount > 0) 1 else 0

            val rawPositionJudge = (position + 1) % columnCount
            if (rawPositionJudge == 0) {
                val rawPosition = (position + 1) / columnCount
                rawCount == rawPosition
            } else {
                val rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1
                rawCount == rawPosition
            }
        }
    }

    /**
     * 检查是不是第一列
     * @param position 位置
     * @param columnCount 列数
     * @return true代表该位置在第一列
     */
    private fun isFirstColumn(position: Int, columnCount: Int): Boolean {
        return columnCount == 1 || position % columnCount == 0
    }

    /**
     * 检查是不是最后一列
     * @param position 位置
     * @param columnCount 列数
     * @return true代表该位置在最后一列
     */
    private fun isLastColumn(position: Int, columnCount: Int): Boolean {
        return columnCount == 1 || (position + 1) % columnCount == 0
    }

    /**
     * 画横线
     * @param c  Canvas
     * @param parent RecyclerView
     */
    fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        c.save()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childPosition = parent.getChildAdapterPosition(child)
            if (childPosition < 0) {
                continue
            }
            val adapter = parent.adapter ?: return
            if (mViewTypeList.contains(adapter.getItemViewType(childPosition))) {
                continue
            }
            val left = child.left
            val top = child.bottom
            val right = child.right
            val bottom = top + mDividerHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
        c.restore()
    }

    /**
     * 画竖线
     * @param c Canvas
     * @param parent RecyclerView
     */
    fun drawVertical(c: Canvas, parent: RecyclerView) {
        c.save()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childPosition = parent.getChildAdapterPosition(child)
            if (childPosition < 0) {
                continue
            }
            val adapter = parent.adapter ?: return
            if (mViewTypeList.contains(adapter.getItemViewType(childPosition))) {
                continue
            }
            //            if (child instanceof SwipeMenuRecyclerView.LoadMoreView) continue;
            val left = child.right
            val top = child.top
            val right = left + mDividerWidth
            val bottom = child.bottom

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
        c.restore()
    }
}
