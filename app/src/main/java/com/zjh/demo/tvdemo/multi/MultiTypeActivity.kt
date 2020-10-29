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
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = MultiTypeAdapter()
        rv.isFocusable = false
        rv.isFocusableInTouchMode = false
    }

    class MultiTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            const val VIEW_TYPE1 = 1
            const val VIEW_TYPE2 = 2
        }

        class Type1Holder(view: View) : RecyclerView.ViewHolder(view) {
            val tv: TextView by lazy { view.findViewById<TextView>(R.id.tv) }
        }

        class Type2Holder(view: View) : RecyclerView.ViewHolder(view) {
            val tv: TextView by lazy { view.findViewById<TextView>(R.id.tv) }
            val tv2: TextView by lazy { view.findViewById<TextView>(R.id.tv2) }
            val tv3: TextView by lazy { view.findViewById<TextView>(R.id.tv3) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType) {
                VIEW_TYPE1 -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type1, parent, false)
                    Type1Holder(view)
                }
                VIEW_TYPE2 -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type2, parent, false)
                    Type2Holder(view)
                }
                else -> throw Throwable("")
            }
        }

        override fun getItemCount() = 10

        override fun getItemViewType(position: Int): Int {
            return when(position) {
//                3, 4, 6 -> VIEW_TYPE2
                else -> VIEW_TYPE1
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when(getItemViewType(position)) {
                VIEW_TYPE1 -> {
                    val holder1 = holder as Type1Holder
                    holder1.tv.text = "$position"
                }
                VIEW_TYPE2 -> {
                    val holder2 = holder as Type2Holder
                    holder2.tv.text = "$position"
                    holder2.tv2.text = "$position"
                    holder2.tv3.text = "$position"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Vtok.instance.detach(this)
    }
}