package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.model.Commentdata;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;

public class ShareRecivedCommentAdapter extends RecyclerView.Adapter<ShareRecivedCommentAdapter.MyViewHolder> {

    private ArrayList<Commentdata> commentList;
    Context mContext;
    Typeface materialdesignicons_font;
    private int selectedFolderId = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email, comment;


        public MyViewHolder(View view) {
            super(view);
            comment = view.findViewById(R.id.comment);
            email = view.findViewById(R.id.email);
        }
    }


    public ShareRecivedCommentAdapter(Context mContext, ArrayList<Commentdata> emailList) {
        this.commentList = emailList;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_recived_comment_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.email.setText(commentList.get(position).getfName());
        holder.comment.setText(commentList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}



