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

/**
 * Activity焦点监听持有者
 */
class FocusHolder(activity: Activity) : ViewTreeObserver.OnGlobalFocusChangeListener {

    private val defaultSmoothScroller by lazy { AlignLinearSmoothScroller(activity) }
    val contentView: FrameLayout by lazy { activity.findViewById<FrameLayout>(android.R.id.content) }
    var oldFocus: View? = null
    var newFocus: View? = null
    var border: View? = null

    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        this.oldFocus = oldFocus
        this.newFocus = newFocus
        handleFocusEvent(oldFocus, newFocus, border)
    }

    fun handleFocusEvent(oldFocus: View?, newFocus: View?, border: View?) {
        Log.d("阿斯顿撒大苏打", "oldFocus >> $oldFocus \n newFocus >> $newFocus")
        if (newFocus == null) {
            //TODO: 当无焦点时，清除焦点框
        } else {
            val focusAttr = TagUtils.getFocusAttr(newFocus)
            when {
                //...
                focusAttr?.isRvItemView == true -> {
                    RvUtils.startSmoothScroll(newFocus.parent as RecyclerView, newFocus, defaultSmoothScroller)
                }

                //...
                focusAttr?.isTvAlignRvChild == true -> {
                    RvUtils.findItemViewByChild(newFocus)?.let {
                        RvUtils.startSmoothScroll(it.parent as RecyclerView, it, defaultSmoothScroller)
                    }
                }
            }

//            border?.let {
//                //置于顶层
//                it.bringToFront()
//
//                //修改大小
//                it.layoutParams.run {
//                    width = newFocus.width
//                    height = newFocus.height
//                }
//
//                //移动位置
//                ViewCompat.animate(border)
//                    .translationX(newFocus.x)
//                    .translationY(newFocus.y)
//                    .setDuration(300L)
//                    .start()
//            }
        }
    }
}