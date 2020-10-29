package com.zjh.vtok.scroller

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.zjh.vtok.uitls.RvUtils

/**
 * 指定比例对齐的scroller
 */
class AlignLinearSmoothScroller(context: Context, var ratio: Float = 0.5f) : LinearSmoothScroller(context) {

    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        val result = boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        Log.d("阿三大苏打大苏打的", "vs:$viewStart ve:$viewEnd bs:$boxStart be:$boxEnd s:$result")
        //TODO：可设置对齐的值，不一定是居中对齐
        return result
    }
}