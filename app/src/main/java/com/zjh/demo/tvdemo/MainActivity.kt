package com.zjh.demo.tvdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val layoutManager = CenterLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //列表
        layoutManager.orientation = RecyclerView.HORIZONTAL
        rv.layoutManager = layoutManager
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun getItemCount() = 10

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//                holder.itemView.layoutParams.width = when (position) {
//                    0, 1 -> 400
//                    else -> 100
//                }
                holder.itemView.findViewById<TextView>(R.id.tv).text = position.toString()
                holder.itemView.setOnClickListener {
                    //判断位置
                    //如果view的中点在居中线的左边
                    //  >> 判断左边缘的VIEW是否是第一个
                    //      >> 不是: 判断居中时将要移动的距离是否大于前面的VIEW的大小（这里先假设是小于，多样式的布局可能不小），
                    //      如果不大于，则需要计算实际移动的距离（在onCreateViewHolder的时候可以获得前面的VIEW的宽度）
                    //      >> 是： 统计前面几个VIEW的总距离，从而计算出实际移动到中点的距离
                    //TODO:这里的动画时间，通过layoutManager获取跟滑动时间一致
                    ViewCompat.animate(tv).translationX(calDistances(holder.itemView)).setDuration(1000L).start()

                    layoutManager.smoothScrollToPosition(rv, RecyclerView.State(), position)
                }
            }
        }
    }

    /**
     * 计算滑动过程中的偏移值
     */
    private fun calDistances(view: View): Float {
        Log.d("View++", "${view.pivotX}")
        //rv的中点
        val rvCenter = rv.width / 2f
        //view宽度的一半
        val vHalfWidth = view.width / 2f
        //view的中心点
        val vCenter = view.x + vHalfWidth

        //目标点
        val p = when {
            //view的中点在左边
            rvCenter > vCenter -> {
                //左边缘的VIEW是第一个
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    layoutManager.findViewByPosition(0)!!.let { firstView ->
                        //第一个VIEW的左边距与view.x
                        val offset = firstView.x - (firstView.layoutParams as RecyclerView.LayoutParams).leftMargin
                        //实际移动位置：view的中心点 + 偏移值
                        vCenter - offset
                    }
                } else { //不是第一个
                    //TODO:默认前面未加载的VIEW的宽度与当前的相同（实际需要通过viewType去判断）, 则不移动
                    rvCenter
                }
            }

            //view的中点在右边
            rvCenter < vCenter -> {
                //右边缘的VIEW是最后一个
                val last = layoutManager.itemCount - 1
                if (layoutManager.findLastVisibleItemPosition() == last) {
                    layoutManager.findViewByPosition(last)!!.let { lastView ->
                        //最后一个VIEW的右边距与view.right的差值
                        val offset = (lastView.layoutParams as RecyclerView.LayoutParams).rightMargin - (rv.width - lastView.x - lastView.width)
                        //实际移动位置：view的中心点 - 偏移值
                        vCenter - offset
                    }
                } else { //不是最后一个
                    //TODO:默认前面未加载的VIEW的宽度与当前的相同（实际需要通过viewType去判断）, 则不移动
                    rvCenter
                }
            }

            //view的中点在中间
            else -> rvCenter
        }

        //这里再减去View的宽度的一半 TODO:这里要通过锚点来判断对齐点
        return p - vHalfWidth
    }
}

