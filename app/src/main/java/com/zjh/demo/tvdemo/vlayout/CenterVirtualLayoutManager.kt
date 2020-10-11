package com.zjh.demo.tvdemo.vlayout

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.VirtualLayoutManager

/**
 * 滑动居中的LayoutManger
 */
class CenterVirtualLayoutManager(context: Context) : VirtualLayoutManager(context) {

    private val scroller by lazy {
        CenterSmoothScroller(
            context,
            this
        )
    }
    private var lastTargetPosition: Int? = null

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        scroller.targetPosition = position
        lastTargetPosition = position
        startSmoothScroll(scroller)
    }

    fun fixScrollToPosition() {
        dt = null
        lastTargetPosition?.let {
            smoothScrollToPosition(recyclerView, RecyclerView.State(), it)
        }
    }

    var dt: Int? = null

    private class CenterSmoothScroller internal constructor(context: Context, val layoutManager: CenterVirtualLayoutManager) :
        LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            val result = boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
            Log.d("阿三大苏打大苏打的", "vs:$viewStart ve:$viewEnd bs:$boxStart be:$boxEnd 需要滑动的距离:$result")
            layoutManager.dt = result
            return result
        }

//        override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
//            val layoutManager = layoutManager as? CenterVirtualLayoutManager
//            if (view == null || layoutManager == null || !layoutManager.canScrollVertically()) {
//                return 0
//            }
//            val params = view.layoutParams as RecyclerView.LayoutParams
//            val top = layoutManager.getAbsoluteDecoratedTop(view) - params.topMargin
//            val bottom = layoutManager.getAbsoluteDecoratedBottom(view) + params.bottomMargin
//            Log.d("阿三大苏打大苏打的", "calculateDyToMakeVisible top：${layoutManager.getDecoratedTop(view)} ${params.topMargin}")
//            Log.d("阿三大苏打大苏打的", "calculateDyToMakeVisible bottom：${layoutManager.getDecoratedBottom(view)} ${params.bottomMargin}")
//            val start = layoutManager.paddingTop
//            val end = layoutManager.height - layoutManager.paddingBottom
//            return calculateDtToFit(top, bottom, start, end, snapPreference)
//        }


    }

//    fun getAbsoluteDecoratedTop(child: View): Int {
//        val location = IntArray(2)
//        child.getLocationInWindow(location)
//
//        val rvLocation = IntArray(2)
//        recyclerView.getLocationInWindow(rvLocation)
//
//        val top = location[1] - rvLocation[1]
//        Log.d("阿三大苏打大苏打的", "getMyDecoratedTop >> ${location[1]} ${rvLocation[1]} $top width:${child.width} height:${child.height} pos:${recyclerView.findContainingViewHolder(child)?.layoutPosition}")
//        Log.d("阿三大苏打大苏打的", "getMyDecoratedTop ${child.top} ${getTopDecorationHeight(child)} ${child.top - getTopDecorationHeight(child)}")
//        return top - getTopDecorationHeight(child)
//    }
//
//    fun getAbsoluteDecoratedBottom(child: View): Int {
//        val location = IntArray(2)
//        child.getLocationOnScreen(location)
//
//        val rvLocation = IntArray(2)
//        recyclerView.getLocationOnScreen(rvLocation)
//
//        val top = location[1] - rvLocation[1]
//
//        return top + child.height + getBottomDecorationHeight(child)
//    }
}