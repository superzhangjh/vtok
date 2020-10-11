package com.zjh.vtok.focus

import android.view.View
import android.view.ViewTreeObserver

/**
 * Activity焦点监听持有者
 */
open class FocusChangeHolder : ViewTreeObserver.OnGlobalFocusChangeListener {

    var oldFocus: View? = null
    var newFocus: View? = null

    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        this.oldFocus = oldFocus
        this.newFocus = newFocus
    }
}