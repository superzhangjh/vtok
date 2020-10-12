package com.zjh.vtok.focus

/**
 * 焦点参数
 */
data class FocusAttr(
    //是否可以获得焦点
    val isTvFocusable: Boolean,

    //是否是RecyclerView的itemView(根布局)
    val isRvItemView: Boolean,

    //RecyclerView的非itemView获得焦点时，是否执行对齐效果
    val isTvAlignRvChild: Boolean
)