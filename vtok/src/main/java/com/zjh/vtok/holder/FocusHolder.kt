package com.zjh.vtok.holder

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.vtok.focus.FocusAttr
import com.zjh.vtok.scroller.AlignLinearSmoothScroller
import com.zjh.vtok.uitls.RvUtils
import com.zjh.vtok.uitls.TagUtils
import com.zjh.vtok.uitls.ViewDistancesUtils

/**
 * Activity焦点监听持有者
 */
class FocusHolder(activity: Activity) : ViewTreeObserver.OnGlobalFocusChangeListener {

    //默认的Scroller
    private val defaultSmoothScroller by lazy { AlignLinearSmoothScroller(activity) }

    //activity根布局的parent
    val contentView: FrameLayout by lazy { activity.findViewById<FrameLayout>(android.R.id.content) }

    //旧焦点
    var oldFocus: View? = null
    //新焦点
    var newFocus: View? = null
    //焦点框
    var border: View? = null

    /**
     * Activity全局焦点变化监听
     */
    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        this.oldFocus = oldFocus
        this.newFocus = newFocus
        handleFocusEvent(oldFocus, newFocus, border)
    }

    /**
     * 处理新的焦点事件
     */
    fun handleFocusEvent(oldFocus: View?, newFocus: View?, border: View?) {
        Log.d("阿斯顿撒大苏打", "oldFocus >> $oldFocus \n newFocus >> $newFocus")
        if (newFocus == null) {
            //TODO: 当无焦点时，清除焦点框
        } else {
            val focusAttr = TagUtils.getFocusAttr(newFocus)

            //移动X坐标
            var toX = 0f
            var toY = 0f

            when {
                //RecyclerView的ItemView获得焦点时，对齐滑动
                focusAttr?.isRvItemView == true -> {
                    val rv = newFocus.parent as RecyclerView
                    val layoutManager = rv.layoutManager as LinearLayoutManager
                    val isVertical = layoutManager.orientation == RecyclerView.VERTICAL

                    RvUtils.startSmoothScroll(rv, newFocus, defaultSmoothScroller)

                    //根据方向的偏移量
//                    val distances = ViewDistancesUtils.calRvItemViewTranslation(rv, newFocus, layoutManager, isVertical, defaultSmoothScroller.ratio)

                    val distances = ViewDistancesUtils.calDistances(rv, layoutManager, newFocus)
                    Log.d("阿三大苏打撒旦", "isRvItemView: $distances")
                    toX = if (isVertical) newFocus.x else distances
                    toY = if (isVertical) distances else newFocus.y
                }

                //RecyclerView ItemView的子View获得焦点时，找到ItemView对齐滑动
                focusAttr?.isTvAlignRvChild == true -> RvUtils.findItemViewByChild(newFocus)?.let {
                    val rv = it.parent as RecyclerView
                    val layoutManager = rv.layoutManager as LinearLayoutManager
                    val isVertical = layoutManager.orientation == RecyclerView.VERTICAL

                    RvUtils.startSmoothScroll(rv, it, defaultSmoothScroller)

                    //根据方向的偏移量
                    val distances = ViewDistancesUtils.calRvItemViewTranslation(rv, it, layoutManager, isVertical, defaultSmoothScroller.ratio)
                    Log.d("阿三大苏打撒旦", "isTvAlignRvChild: $distances")
                    toX = if (isVertical) it.x else distances
                    toY = if (isVertical) distances else it.y
                }
            }

            border?.let {
                //置于顶层
                it.bringToFront()

                //修改大小
                it.layoutParams.run {
                    width = newFocus.width
                    height = newFocus.height
                }

                //移动位置
                ViewCompat.animate(border)
                    .translationX(toX)
                    .translationY(toY)
                    .setDuration(300L)
                    .start()
            }
        }
    }
}