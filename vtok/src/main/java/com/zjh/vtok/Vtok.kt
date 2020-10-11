package com.zjh.vtok

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import com.zjh.vtok.focus.FocusChangeHolder
import com.zjh.vtok.view.ViewAttrInflaterFactory2


class Vtok private constructor(){

    private object SingleTonHolder {
        val holder = Vtok()
    }
    companion object {
        val instance = SingleTonHolder.holder
    }

    /**
     * 绑定到Activity中，该方法需要在super.onCreate之前调用
     * @param customAttr xml是否自定义属性
     */
    fun attach(activity: AppCompatActivity, customAttr: Boolean = true) {
        //拦截View生成，解析自定义属性
        if (customAttr) {
            val inflater = LayoutInflater.from(activity)
            LayoutInflaterCompat.setFactory2(inflater, ViewAttrInflaterFactory2(activity, inflater))
        }

        val contentView = activity.findViewById<FrameLayout>(android.R.id.content)

        //生成焦点框View
        val border = LayoutInflater.from(activity).inflate(R.layout.view_border, contentView, false)

        //activity全局焦点监听
        val focusHolder = object : FocusChangeHolder() {
            override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
                super.onGlobalFocusChanged(oldFocus, newFocus)

                handleFocusEvent(oldFocus, newFocus, border)
            }
        }
        contentView.viewTreeObserver.addOnGlobalFocusChangeListener(focusHolder)

        //当ContentView构建完成时，将border添加进入，提前添加会无法显示
        contentView.post {
            contentView.addView(border)

            //如果添加的时候已经有了新的焦点，则执行一次焦点事件处理
            focusHolder.newFocus?.let {
                handleFocusEvent(focusHolder.oldFocus, focusHolder.newFocus, border)
            }
        }
    }

    /**
     * 执行焦点事件
     * @param oldFocus
     * @param newFocus
     * @param border
     */
    private fun handleFocusEvent(oldFocus: View?, newFocus: View?, border: View?) {
        Log.d("阿斯顿撒大苏打", "oldFocus >> $oldFocus \n newFocus >> $newFocus")
//        newFocus?.scaleX = 1.1f
//        newFocus?.scaleY = 1.1f
//        oldFocus?.scaleX = 1.0f
//        oldFocus?.scaleY = 1.0f

        border?.let {
            //置于顶层
            it.bringToFront()

            //修改大小
            it.layoutParams.run {
                width = newFocus?.width ?: 0
                height = newFocus?.height ?: 0
            }

            //移动位置
            ViewCompat.animate(border)
                .translationX(newFocus?.x ?: 0f)
                .translationY(newFocus?.y ?: 0f)
                .setDuration(300L)
                .start()
        }

    }

    fun detach(activity: Activity) {
        //TODO: 移除OnGlobalFocusChangeListener
    }
}