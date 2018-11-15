package com.jackiepenghe.baselibraryforkotlin.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

@Suppress("unused")
/**
 * 流式布局的容器
 *
 * @author pengh
 * @date 2017/7/8
 */
class FlowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ViewGroup(context, attrs, defStyleAttr) {

    /**
     * 记录每一行的子控件的集合
     */
    private val viewLineList: ArrayList<ArrayList<View>> = ArrayList()
    /**
     * j记录每一行的高度的集合
     */
    private val lineHeightList: ArrayList<Int> = ArrayList()

    /**
     * 计算宽高
     *
     * @param widthMeasureSpec  宽度的规格
     * @param heightMeasureSpec 高度的规格
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取测量模式
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        //获取容器给予的建议值
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        //用于记录测量之后，子控件的宽度
        var measuredWidth = 0
        //用于记录测量之后，子控件的高度
        var measuredHeight = 0
        //用于记录当前行的宽度
        var currentLineWidth = 0
        //用于记录当前行的高度
        var currentLineHeight = 0
        //这种情况下我们直接使用建议值作为测量值
        if (widthMode != View.MeasureSpec.EXACTLY && heightMode != View.MeasureSpec.EXACTLY) {
            measuredWidth = widthSize
            measuredHeight = heightSize
        } else { //通过计算得到测量值
            //获取子控件总数
            val childCount = childCount
            //用于保存当前行的所有子控件的集合
            @SuppressLint("DrawAllocation") var viewList = ArrayList<View>()
            for (i in 0 until childCount) {
                val childView = getChildAt(i)
                //测量子控件的宽高
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                //取出子控件的margin属性
                val marginLayoutParams = childView.layoutParams as ViewGroup.MarginLayoutParams
                //计算子控件的宽度
                val childViewWidth =
                    childView.measuredWidth + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + childView.paddingLeft + childView.paddingRight
                //计算子控件的高度
                val childViewHeight =
                    childView.measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + childView.paddingTop + childView.paddingBottom
                // 如果当前行的宽度大于建议的宽度，就需要换行了
                if (currentLineWidth + childViewWidth > widthSize) {
                    //取当前行的数据中，当前宽度与测量之后计算的宽度的最大的一个
                    measuredWidth = Math.max(currentLineWidth, measuredWidth)
                    //将测量高度累加一次（因为现在多了一行，需要增加一行的高度）
                    measuredHeight += currentLineHeight
                    //换行之后，将当前行的子控件集合添加到viewLineList
                    viewLineList.add(viewList)
                    //将行高添加到lineHeightList
                    lineHeightList.add(currentLineHeight)

                    //将当前行的宽度和当前行的高度重置为新一行的第一个子控件的宽度与高度
                    currentLineWidth = childViewWidth
                    currentLineHeight = childViewHeight

                    //将用于保存当前行的所有子控件的集合重新创建（不能清空，会导致之前的数据全部丢失），并添加新一行的第一个子控件
                    viewList = ArrayList()
                    viewList.add(childView)
                } else {//如果不超过建议值，记录当前行的一些数据
                    // 累加当前的行宽度
                    currentLineWidth += childViewWidth
                    //取每行的最大高度
                    currentLineHeight = Math.max(childViewHeight, currentLineHeight)
                    //将当前行的子控件添加到用于记录当前行子控件的集合
                    viewList.add(childView)
                }

                //如果正好是最后一行需要换行
                if (i == childCount - 1) {
                    //取当前行的数据中，当前宽度与测量之后计算的宽度的最大的一个
                    measuredWidth = Math.max(currentLineWidth, measuredWidth)
                    //将测量高度累加一次（因为现在多了一行，需要增加一行的高度）
                    measuredHeight += currentLineHeight
                    //换行之后，将当前行的子控件集合添加到viewLineList
                    viewLineList.add(viewList)
                    //将行高添加到lineHeightList
                    lineHeightList.add(currentLineHeight)
                }
            }
        }
        //设置宽高
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    /**
     * 取出布局中的margin属性
     *
     * @param attrs 属性
     * @return LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

    /**
     * 摆放子控件的位置
     *
     * @param changed 位置是否被改变了
     * @param l       当前容器的左边的坐标
     * @param t       当前容器的顶部的坐标
     * @param r       当前容器的右边的坐标
     * @param b       当前容器的底部的坐标
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //获取总行数
        val lineCount = lineHeightList.size
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int   //子控件需要摆放的坐标
        var currentTop: Int
        var currentLeft: Int //记录当前需要摆放的起始位置
        currentLeft = 0
        currentTop = currentLeft

        for (i in 0 until lineCount) {
            //获取当前行的子控件集合
            val views = viewLineList[i]
            //摆放当前行的所有子控件
            val childViewCount = views.size
            for (j in 0 until childViewCount) {
                //获取子控件
                val childView = views[j]
                val marginLayoutParams = childView.layoutParams as ViewGroup.MarginLayoutParams
                //子控件的左边的坐标
                left = currentLeft + marginLayoutParams.leftMargin + childView.paddingLeft
                //子控件的顶部的坐标
                top = currentTop + marginLayoutParams.topMargin + childView.paddingTop
                //子控件的右边的坐标
                right = left + childView.measuredWidth + childView.paddingRight
                //子控件底部的坐标
                bottom = top + childView.measuredHeight + childView.paddingBottom
                //摆放子控件
                childView.layout(left, top, right, bottom)
                //将起始坐标currentLeft更新一次
                currentLeft = right + marginLayoutParams.rightMargin
            }
            //在摆放完一排之后，更新起始坐标currentTop
            currentTop += lineHeightList[i]
            //同时，将currentLeft重置为0
            currentLeft = 0
        }
        //因为测量
        viewLineList.clear()
        lineHeightList.clear()
    }

    interface OnItemClickListener {
        /**
         * 子控件被点击时进行的回调
         * @param view 子控件
         * @param position 子控件在当前布局中的位置
         */
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.isClickable = true
            childView.setOnClickListener {
                onItemClickListener?.onItemClick(childView, i)
            }
        }
    }
}
