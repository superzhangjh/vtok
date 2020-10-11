package com.zjh.demo.tvdemo.vlayout

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.zjh.demo.tvdemo.*
import com.zjh.vtok.Vtok
import kotlinx.android.synthetic.main.activity_vlayout.*

class VLayoutActivity : AppCompatActivity() {

    private val layoutManager =
        CenterVirtualLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        Vtok.instance.attach(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vlayout)
        rv.layoutManager = layoutManager

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var mDy = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("阿三大苏打大苏打的", "实际滑动距离:$mDy")
                    if (layoutManager.dt != mDy) {
                        Log.d("阿三大苏打大苏打的", "开始修正")
                        layoutManager.fixScrollToPosition()
                    }
                    mDy = 0
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mDy -= dy
            }
        })

        val adapters = DelegateAdapter(layoutManager, true)
        adapters.addAdapter(
            LinearLayoutAdapter(
                3,
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Log.d("阿三大苏打onItemClick", "$view")


                        Log.d("阿三大苏打大苏打的", "-------------------------------------------------")
                        layoutManager.smoothScrollToPosition(rv, RecyclerView.State(), position)
                    }
                })
        )
        adapters.addAdapter(
            GridLayoutAdapter(
                30,
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Log.d("阿三大苏打大苏打的", "-------------------------------------------------")
                        layoutManager.smoothScrollToPosition(rv, RecyclerView.State(), position + 3)
                    }
                },
                adapters
            )
        )

        adapters.addAdapter(
            LinearLayoutAdapter(
                10,
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Log.d("阿三大苏打大苏打的", "-------------------------------------------------")
                        layoutManager.smoothScrollToPosition(
                            rv,
                            RecyclerView.State(),
                            position + 33
                        )
                    }
                })
        )

        rv.adapter = adapters
    }

}