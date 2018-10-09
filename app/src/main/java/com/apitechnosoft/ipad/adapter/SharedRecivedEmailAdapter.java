package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.model.Commentdata;
import com.apitechnosoft.ipad.model.Emaildata;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;

public class SharedRecivedEmailAdapter extends RecyclerView.Adapter<SharedRecivedEmailAdapter.MyViewHolder> {

    private ArrayList<Emaildata> emailList;
    Context mContext;
    Typeface materialdesignicons_font;
    private int selectedFolderId = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cust_view;


        public MyViewHolder(View view) {
            super(view);
            cust_view = view.findViewById(R.id.cust_view);
        }
    }


    public SharedRecivedEmailAdapter(Context mContext, ArrayList<Emaildata> emailList) {
        this.emailList = emailList;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.cust_view.setText(emailList.get(position).getEmailId());

    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

}


