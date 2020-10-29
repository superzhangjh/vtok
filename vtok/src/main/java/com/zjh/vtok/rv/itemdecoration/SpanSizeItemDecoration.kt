package com.zjh.vtok.rv.itemdecoration

import android.graphics.Rect
import android.util.SparseIntArray
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpanSizeItemDecoration(parent: RecyclerView,
private val xOffset: Float, private val yOffset: Float) : RecyclerView.ItemDecoration() {

    //每组ItemCount的缓存
    private val groupItemCountSparseArray by lazy { SparseIntArray() }

    init {
        //注册数据监听，当数据发生变化时，清除对应Group的缓存
        parent.adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

        })
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val position = parent.findContainingViewHolder(view)?.layoutPosition ?: RecyclerView.NO_POSITION

            //当前组的下标
            val spanGroupIndex = layoutManager.spanSizeLookup.getSpanGroupIndex(position, layoutManager.spanCount)

            //当前组的总个数
            val groupItemCount = getGroupItemCount(spanGroupIndex, position, layoutManager)

            //item在组的下标
            val groupItemIndex = getGroupItemIndex(layoutManager, position, spanGroupIndex)

            //头部间隔的份数
            val startOffsetPer = groupItemIndex % groupItemCount

            //尾部间隔的份数
            val endOffsetPer = groupItemCount - 1 -startOffsetPer

            //是否垂直
            val vertical = layoutManager.orientation == GridLayoutManager.VERTICAL

            //将每个间距划分成groupItemCount份，并求出每份的值per
            val per = if (vertical) xOffset else yOffset

            //根据方向判断offset
            if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
                outRect.left = (per * startOffsetPer).toInt()
                outRect.top = if (spanGroupIndex == 0) 0 else yOffset.toInt()
                outRect.right = (per * endOffsetPer).toInt()
                outRect.bottom = 0
            } else {
                outRect.left = if (spanGroupIndex == 0) 0 else xOffset.toInt()
                outRect.top = (per * startOffsetPer).toInt()
                outRect.right = 0
                outRect.bottom = (per * endOffsetPer).toInt()
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }

    /**
     * @return 组的总item个数
     */
    private fun getGroupItemCount(spanGroupIndex: Int, adapterPosition: Int, layoutManager: GridLayoutManager): Int {
        groupItemCountSparseArray.get(spanGroupIndex, 0).also {
            return if (it == 0) {
                val count = -1 + getPreGetGroupItemCount(adapterPosition, layoutManager) + getAlterGetGroupItemCount(adapterPosition, layoutManager)

                //保存当前的个数统计
                groupItemCountSparseArray.put(spanGroupIndex, count)

                count
            } else {
                it
            }
        }
    }

    /**
     * @return 获取当前之前的ItemCount
     */
    private fun getPreGetGroupItemCount(adapterPosition: Int, layoutManager: GridLayoutManager): Int {
        val spanIndex = layoutManager.spanSizeLookup.getSpanIndex(adapterPosition, layoutManager.spanCount)
        return if (spanIndex == 0) 1
        else 1 + getPreGetGroupItemCount(adapterPosition - 1, layoutManager)
    }

    /**
     * @return 获取当前之后的ItemCount
     */
    private fun getAlterGetGroupItemCount(adapterPosition: Int, layoutManager: GridLayoutManager): Int {
        val spanIndex = layoutManager.spanSizeLookup.getSpanIndex(adapterPosition, layoutManager.spanCount)
        val spanSize = layoutManager.spanSizeLookup.getSpanSize(adapterPosition)
        return if (spanIndex + spanSize >= layoutManager.spanCount) 1
        else  1+ getAlterGetGroupItemCount(adapterPosition + 1, layoutManager)
    }

    /**
     * @return Item在当前组的下标
     */
    private fun getGroupItemIndex(layoutManager: GridLayoutManager, position: Int, spanGroupIndex: Int): Int {
        //TODO:这里需要缓存 spanGroupIndex、position获得的GroupItemIndex

        val spanIndex = layoutManager.spanSizeLookup.getSpanIndex(position, layoutManager.spanCount)
        if (spanIndex == 0) {
            return 0
        } else {
            val spanSize = layoutManager.spanSizeLookup.getSpanSize(position)
            val groupItemCount = getGroupItemCount(spanGroupIndex, position, layoutManager)
            if (spanIndex + spanSize == layoutManager.spanCount) {
                return groupItemCount - 1
            } else {
                var resSpanCount = spanIndex
                var index: Int
                //判断计算的起点
                if (groupItemCount / 2 < spanIndex) { //往后面增加
                    index = groupItemCount
                    while (resSpanCount < layoutManager.spanCount) {
                        //TODO:这里判断不对
                        resSpanCount += layoutManager.spanSizeLookup.getSpanSize(position + 1)
                        index --
                    }
                } else { //往前面减少值
                    index = 0
                    while (resSpanCount > 0) {
                        //TODO:这里的position一直没有变化，需要一个变化的position
                        resSpanCount -= layoutManager.spanSizeLookup.getSpanSize(position - 1)
                        index ++
                    }
                }
                return index
            }
        }
    }
}