package com.zjh.vtok.uitls

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 计算View距离的工具类
 */
object ViewDistancesUtils {

    /**
     * 计算Rv的ItemView移动到对齐点时偏移量
     * @param rv RecyclerView
     * @param view RV的ItemView
     * @param ratio ItemView对齐点在RV的比例(0-1 >>  0置顶 1顶部)
     * @param layoutManager
     * @param vertical
     */
    fun calRvItemViewTranslation(rv: RecyclerView, view: View, layoutManager: LinearLayoutManager,
                                 vertical: Boolean, ratio: Float): Float {
        //边长
        val length = if (vertical) rv.height else rv.width

        //rv的对齐点
        val rvRatio = length * ratio

        //view的中心点
        val vCenter = view.x + length / 2f

        //目标点
        val p = when {
            //view的中点在start边
            rvRatio > vCenter -> {
                //左边缘的VIEW是第一个
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    layoutManager.findViewByPosition(0)!!.let { firstView ->
                        //第一个VIEW的start边距与view.margin
                        val start = if (vertical) firstView.y else firstView.x
                        val margin = (firstView.layoutParams as RecyclerView.LayoutParams).run {
                            if (vertical) topMargin else leftMargin
                        }
                        //实际移动位置：view的中心点 + 偏移值
                        vCenter - (start - margin)
                    }
                } else { //不是第一个
                    //TODO:默认前面未加载的VIEW的宽度与当前的相同（实际需要通过viewType去判断）, 则不移动
                    rvRatio
                }
            }

            //view的中点在end边
            rvRatio < vCenter -> {
                //右边缘的VIEW是最后一个
                val last = layoutManager.itemCount - 1
                if (layoutManager.findLastVisibleItemPosition() == last) {
                    layoutManager.findViewByPosition(last)!!.let { lastView ->
                        //最后一个VIEW的end边距与view.endMargin的差值
                        val end = (lastView.layoutParams as RecyclerView.LayoutParams).run {
                            if (vertical) bottomMargin else rightMargin
                        }
                        val margin = length - if (vertical) (lastView.y + lastView.height) else (lastView.x + lastView.width)
                        //实际移动位置：view的中心点 - 偏移值
                        vCenter - end - margin
                    }
                } else { //不是最后一个
                    //TODO:默认前面未加载的VIEW的宽度与当前的相同（实际需要通过viewType去判断）, 则不移动
                    rvRatio
                }
            }

            //view的中点在对齐点
            else -> rvRatio
        }

        //这里再减去View的宽度的一半 TODO:这里要通过锚点来判断对齐点
        return p - length / 2f
    }

    /**
     * 计算滑动过程中的偏移值
     */
    fun calDistances(rv: RecyclerView, layoutManager: LinearLayoutManager, view: View): Float {
        //rv的中点
        val rvCenter = rv.width / 2f
        //view宽度的一半
        val vHalfWidth = view.width / 2f
        //view的中心点
        val vCenter = view.x + vHalfWidth

        //目标点
        val p = when {
            //view的中点在左边
            rvCenter > vCenter -> {
                //左边缘的VIEW是第一个
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    layoutManager.findViewByPosition(0)!!.let { firstView ->
                        //第一个VIEW的左边距与view.x
                        val offset = firstView.x - (firstView.layoutParams as RecyclerView.LayoutParams).leftMargin
                        //实际移动位置：view的中心点 + 偏移值
                        vCenter - offset
                    }
                } else { //不是第一个
                    //TODO:默认前面未加载的VIEW的宽度与当前的相同（实际需要通过viewType去判断）, 则不移动
                    rvCenter
                }
            }

            //view的中点在右边
            rvCenter < vCenter -> {
                //右边缘的VIEW是最后一个
                val last = layoutManager.itemCount - 1
                if (layoutManager.findLastVisibleItemPosition() == last) {
                    layoutManager.findViewByPosition(last)!!.let { lastView ->
                        //最后一个VIEW的右边距与view.right的差值
                        val offset = (lastView.layoutParams as RecyclerView.LayoutParams).rightMargin - (rv.width - lastView.x - lastView.width)
                        //实际移动位置：view的中心点 - 偏移值
                        vCenter - offset
                    }
                } else { //不是最后一个
                    //TODO:默认前面未加载的VIEW的宽度与当前的相同（实际需要通过viewType去判断）, 则不移动
                    rvCenter
                }
            }

            //view的中点在中间
            else -> rvCenter
        }

        //这里再减去View的宽度的一半 TODO:这里要通过锚点来判断对齐点
        return p - vHalfWidth
    }

}