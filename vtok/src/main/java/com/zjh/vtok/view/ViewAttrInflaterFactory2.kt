package com.zjh.vtok.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zjh.vtok.R
import java.lang.Exception

/**
 * VIEW自定义属性解析Factory
 */
class ViewAttrInflaterFactory2(
    private val activity: Activity,
    private val inflater: LayoutInflater
) : LayoutInflater.Factory2 {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        // 兼容AppCompatActivity
        val view = if (activity is AppCompatActivity && isAppCompatView(name)) {
            activity.delegate.createView(parent, name, context, attrs)
        } else try {
            inflater.createView(name, null, attrs)
        } catch (e: Exception) {
            //部分View在未设置的情况下会出现报错，但不影响，例：Toolbar
            e.printStackTrace()
            null
        }

        // 解析xml属性
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.Vtok)

        // 解析自定义的xml属性
        val isTvFocusable = typeArray.getBoolean(R.styleable.Vtok_tvFocusable, false)

        Log.d("啊实打实大苏打啊实打实的", "$name $isTvFocusable $view")

        //设置可获取焦点
        view?.isFocusable = isTvFocusable
        view?.isFocusableInTouchMode = isTvFocusable
        view?.isClickable = isTvFocusable

        typeArray.recycle()
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    /**
     * 判断是否是AppCompat兼容的控件
     * @param name 控件名
     */
    private fun isAppCompatView(name: String): Boolean {
        return when (name) {
            "TextView",
            "ImageView",
            "Button",
            "EditText",
            "Spinner",
            "ImageButton",
            "CheckBox",
            "RadioButton",
            "CheckedTextView",
            "AutoCompleteTextView",
            "MultiAutoCompleteTextView",
            "RatingBar",
            "SeekBar",
            "ToggleButton" -> true
            else -> false
        }
    }
}