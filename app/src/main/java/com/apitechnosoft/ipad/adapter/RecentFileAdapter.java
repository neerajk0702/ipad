package com.apitechnosoft.ipad.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.model.Data;

import java.util.ArrayList;

public class RecentFileAdapter extends RecyclerView.Adapter<RecentFileAdapter.MyViewHolder> {

    private ArrayList<Data> dataList;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg =  view.findViewById(R.id.recentImg);
        }
    }


    public RecentFileAdapter(Context mContext, ArrayList<Data> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.recenttext.setText(dataList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}

