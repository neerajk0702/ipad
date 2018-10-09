package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class ShareAllFileAdapter extends RecyclerView.Adapter<ShareAllFileAdapter.MyViewHolder> {

    public ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CheckBox selectCheck;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            selectCheck = view.findViewById(R.id.selectCheck);
        }
    }


    public ShareAllFileAdapter(Context mContext, ArrayList<MediaData> List) {
        this.mediaList = List;
        this.mContext = mContext;
        this.type = type;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_row, parent, false);

        return new MyViewHolder(itemView);
    }
    public void setCheckBoxColor(CheckBox checkBox, int checkedColor, int uncheckedColor) {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {checkedColor, uncheckedColor};
        CompoundButtonCompat.setButtonTintList(checkBox, new
                ColorStateList(states, colors));
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());
        setCheckBoxColor(holder.selectCheck, ASTUIUtil.getColor(R.color.green_color),ASTUIUtil.getColor(R.color.selectfolder));


        holder.selectCheck.setVisibility(View.VISIBLE);
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("image")) {
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            Picasso.with(ApplicationHelper.application().getContext()).load(filePath).placeholder(R.drawable.image_icon).into(holder.recentImg);
        }
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("video")) {
            holder.recentImg.setImageResource(R.drawable.video);
        }
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("audio")) {
            holder.recentImg.setImageResource(R.drawable.audio_icon);
        }
        if (mediaList.get(position).getExtension() != null) {
            if (mediaList.get(position).getExtension().contains("docx") || mediaList.get(position).getExtension().contains("txt")) {
                holder.recentImg.setImageResource(R.drawable.doc);
            } else if (mediaList.get(position).getExtension().contains("pdf")) {
                holder.recentImg.setImageResource(R.drawable.pdfimg);
            } else if (mediaList.get(position).getExtension().contains("html")) {
                holder.recentImg.setImageResource(R.drawable.htmlimg);
            } else if (mediaList.get(position).getExtension().contains("zip")) {
                holder.recentImg.setImageResource(R.drawable.zipimg);
            } else if (mediaList.get(position).getExtension().contains("xlsx")) {
                holder.recentImg.setImageResource(R.drawable.excelimg);
            } else if (mediaList.get(position).getExtension().contains("pptx") || mediaList.get(position).getExtension().contains("ppt")) {
                holder.recentImg.setImageResource(R.drawable.pptimg);
            }
        }

        if (mediaList.get(position).isSelected()) {
            holder.selectCheck.setChecked(true);
        } else {
            holder.selectCheck.setChecked(false);
        }
        holder.selectCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mediaList.get(position).setSelected(true);
                } else {
                    mediaList.get(position).setSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

}
