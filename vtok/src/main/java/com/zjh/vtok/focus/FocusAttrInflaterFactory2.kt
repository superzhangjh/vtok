package com.zjh.vtok.focus

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zjh.vtok.R
import com.zjh.vtok.uitls.TagUtils
import java.lang.Exception

/**
 * VIEW自定义属性解析Factory
 */
class FocusAttrInflaterFactory2(
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

        view?.let {
            //设置自定义属性，并记录
            val focusAttr = getFocusAttr(parent, context, attrs)
            initViewFocusAttr(it, focusAttr)
            TagUtils.setFocusAttr(it, focusAttr)
        }
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

    /**
     * 解析焦点的xml属性
     */
    private fun getFocusAttr(parent: View?, context: Context, attrs: AttributeSet): FocusAttr {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Vtok)

        val isTvFocusable = typedArray.getBoolean(R.styleable.Vtok_tvFocusable, false)
        val isTvAlignRvChild = typedArray.getBoolean(R.styleable.Vtok_tvAlignRvChild, false)

        val isRvItemView = parent is RecyclerView

        typedArray.recycle()
        return FocusAttr(isTvFocusable, isRvItemView, isTvAlignRvChild)
    }

    /**
     * 初始化View设置
     */
    private fun initViewFocusAttr(view: View, focusAttr: FocusAttr) {
        if (focusAttr.isTvFocusable) {
            view.isClickable = true
        }
        view.isFocusableInTouchMode = focusAttr.isTvFocusable
    }
}