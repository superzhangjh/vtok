package com.zjh.demo.tvdemo.multi

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjh.demo.tvdemo.R
import com.zjh.vtok.Vtok
import kotlinx.android.synthetic.main.activity_main.*

class MultiTypeActivity : AppCompatActivity(R.layout.activity_multi_type) {

    override fun onCreate(savedInstanceState: Bundle?) {
        Vtok.instance.attach(this)
        super.onCreate(savedInstanceState)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = MultiTypeAdapter()
        rv.isFocusable = false
        rv.isFocusableInTouchMode = false
        (rv.layoutManager as LinearLayoutManager).startSmoothScroll(object : RecyclerView.SmoothScroller() {
            override fun onSeekTargetStep(
                dx: Int,
                dy: Int,
                state: RecyclerView.State,
                action: Action
            ) {
                TODO("Not yet implemented")
            }

            override fun onStop() {
                TODO("Not yet implemented")
            }

            override fun onTargetFound(
                targetView: View,
                state: RecyclerView.State,
                action: Action
            ) {
                TODO("Not yet implemented")
            }

            override fun onStart() {
                TODO("Not yet implemented")
            }
        })
    }

    class MultiTypeAdapter : RecyclerView.Adapter<MultiTypeAdapter.Type1Holder>() {

        class Type1Holder(view: View) : RecyclerView.ViewHolder(view) {
            val tv: TextView by lazy { view.findViewById<TextView>(R.id.tv) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Type1Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type1, parent, false)
            view.setOnClickListener {
                Toast.makeText(it.context, "点击 ${view.isFocusable}", Toast.LENGTH_SHORT).show()
            }
            return Type1Holder(view)
        }

        override fun getItemCount() = 10

        override fun onBindViewHolder(holder: Type1Holder, position: Int) {
            holder.tv.text = "$position"
        }

    }
}