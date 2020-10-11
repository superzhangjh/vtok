package com.zjh.demo.tvdemo

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * 滑动居中的LayoutManger
 */
class CenterLayoutManager(context: Context) : LinearLayoutManager(context) {

    private val scroller by lazy { CenterSmoothScroller(context) }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }

    private class CenterSmoothScroller internal constructor(context: Context) :
        LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            val result = boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
            Log.d("阿三大苏打大苏打的", "vs:$viewStart ve:$viewEnd bs:$boxStart be:$boxEnd s:$result")
            return result
        }
    }

}