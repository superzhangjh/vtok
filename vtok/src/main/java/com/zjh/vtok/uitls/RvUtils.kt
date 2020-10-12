package com.zjh.vtok.uitls

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

object RvUtils {

    /**
     * 通过子view找到position
     * @param itemView rv itemView的根布局
     */
    fun findViewPosition(rv: RecyclerView, itemView: View): Int {
        return rv.findContainingViewHolder(itemView)?.absoluteAdapterPosition ?: RecyclerView.NO_POSITION
    }

    /**
     * 找到child的父类itemView
     * @param child RecyclerView的itemView的子view
     */
    fun findItemViewByChild(child: View): View? {
        val parent = child.parent
        if (parent is View) {
            val focusAttr = TagUtils.getFocusAttr(parent)
            return if (focusAttr?.isRvItemView == true) {
                parent
            } else {
                findItemViewByChild(parent)
            }
        }
        return null
    }

    /**
     * 指定Rv滚动到itemView
     */
    fun startSmoothScroll(rv: RecyclerView, itemView: View, scroller: LinearSmoothScroller) {
        val layoutManager = rv.layoutManager
        if (layoutManager is LinearLayoutManager) {
            scroller.targetPosition = findViewPosition(rv, itemView)
            layoutManager.startSmoothScroll(scroller)
        }
    }
}