package com.zjh.vtok.uitls

import android.view.View
import com.zjh.vtok.R
import com.zjh.vtok.focus.FocusAttr

internal object TagUtils {

    private val ID_FOCUS_ATTR by lazy { R.id.vtok_id_focus_attr }

    fun setFocusAttr(view: View, focusAttr: FocusAttr) {
        view.setTag(ID_FOCUS_ATTR, focusAttr)
    }

    fun getFocusAttr(view: View): FocusAttr? = view.getTag(ID_FOCUS_ATTR) as? FocusAttr
}