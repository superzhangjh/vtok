package com.zjh.vtok

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import com.zjh.vtok.holder.FocusHolder
import com.zjh.vtok.focus.FocusAttrInflaterFactory2
import com.zjh.vtok.uitls.TagUtils


class Vtok private constructor(){

    private object SingleTonHolder {
        val holder = Vtok()
    }

    companion object {
        val instance = SingleTonHolder.holder
    }

    //所有Activity的FocusHolder
    private val focusHolderMap by lazy { mutableMapOf<Activity, FocusHolder>() }

    /**
     * 绑定到Activity中，该方法需要在super.onCreate之前调用
     * @param customAttr xml是否自定义属性
     */
    fun attach(activity: AppCompatActivity, customAttr: Boolean = true) {
        //拦截View生成，解析自定义属性
        if (customAttr) {
            val inflater = LayoutInflater.from(activity)
            LayoutInflaterCompat.setFactory2(inflater, FocusAttrInflaterFactory2(activity, inflater))
        }

        //activity全局焦点监听
        val focusHolder = FocusHolder(activity)

        //生成焦点框View
        focusHolder.contentView.let {
            val border = LayoutInflater.from(activity).inflate(R.layout.view_border, it, false)
            focusHolder.border = border

            //添加焦点监听
            it.viewTreeObserver.addOnGlobalFocusChangeListener(focusHolder)

            //当ContentView构建完成时，将border添加进入，提前添加会无法显示
            it.post {
                it.addView(border)

                //如果添加的时候已经有了新的焦点，则执行一次焦点事件处理
                focusHolder.newFocus?.let {
                    focusHolder.handleFocusEvent(focusHolder.oldFocus, focusHolder.newFocus, border)
                }
            }
        }
    }

    fun detach(activity: Activity) {
        focusHolderMap[activity]?.let {
            it.contentView.viewTreeObserver.removeOnGlobalFocusChangeListener(it)
        }
        focusHolderMap.remove(activity)
    }
}