package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;
import java.util.HashSet;

public class MoveFileFolderAdapter extends RecyclerView.Adapter<MoveFileFolderAdapter.MyViewHolder> {

    private ArrayList<Folderdata> mediaList;
    Context mContext;
    Typeface materialdesignicons_font;
    HashSet<Integer> selectedPosition = new HashSet<>();
    public Folderdata selectFolderData;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CardView cardLayout;
        CheckBox selectCheck;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            cardLayout = view.findViewById(R.id.cardLayout);
            selectCheck = view.findViewById(R.id.selectCheck);
        }
    }


    public MoveFileFolderAdapter(Context mContext, ArrayList<Folderdata> List) {
        this.mediaList = List;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());
        holder.selectCheck.setVisibility(View.VISIBLE);
        ColorStateList  colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked} , // checked
                },
                new int[]{
                        Color.parseColor("#d33434"),
                        Color.parseColor("##42C47D"),
                }
        );

        CompoundButtonCompat.setButtonTintList(holder.selectCheck,colorStateList);

        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
        }
        holder.selectCheck.setTag(position);
        if (selectedPosition.contains(position)) {
            holder.selectCheck.setChecked(true);
        } else {
            holder.selectCheck.setChecked(false);
        }

        holder.selectCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFolderData = mediaList.get(position);
                int pos = (int) v.getTag();
                notifyData(pos);
            }
        });
    }

    public void notifyData(int pos) {
        if (selectedPosition.contains(pos)) {
            //selectedPosition.remove(pos);
            //selectedPosition.clear();
            notifyDataSetChanged();
        } else {
            selectedPosition.clear();
            selectedPosition.add(pos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

}


