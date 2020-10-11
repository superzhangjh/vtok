package com.zjh.demo.tvdemo.vlayout;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.zjh.demo.tvdemo.OnItemClickListener;
import com.zjh.demo.tvdemo.R;

public class LinearLayoutAdapter extends DelegateAdapter.Adapter<LinearLayoutAdapter.ViewHolder> {

    private int count;
    private OnItemClickListener onItemClickListener;

    public LinearLayoutAdapter(int count, OnItemClickListener onItemClickListener) {
        this.count = count;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper = new LinearLayoutHelper(10);
        helper.setItemCount(count);
        return helper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_vlayout_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.<TextView>findViewById(R.id.tv).setText("" + i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("点击测试", "LinearLayoutAdapter：" + i);
                onItemClickListener.onItemClick(view, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
