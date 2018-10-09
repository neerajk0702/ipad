package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Folderdata> mediaList;
    public ArrayList<Folderdata> masterMediaList;
    Context mContext;
    Typeface materialdesignicons_font;
    private int selectedFolderId = 0;
    boolean firstTime = false;
    String UserId;
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mediaList = masterMediaList;
                } else {

                    ArrayList<Folderdata> filteredList = new ArrayList<Folderdata>();

                    for (Folderdata data : masterMediaList) {

                        if (data.getFileName().toLowerCase().contains(charString)) {
                            filteredList.add(data);
                        }
                    }

                    mediaList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mediaList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mediaList = (ArrayList<Folderdata>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CardView cardLayout;
        TextView deleteiconfolder;

        public MyViewHolder(View view) {
            super(view);
            recenttext =view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            cardLayout = view.findViewById(R.id.cardLayout);
            deleteiconfolder = view.findViewById(R.id.deleteiconfolder);
            deleteiconfolder.setTypeface(materialdesignicons_font);
            deleteiconfolder.setText(Html.fromHtml("&#xf1c0;"));
        }
    }


    public FolderAdapter(Context mContext, ArrayList<Folderdata> List, boolean firstTime) {
        this.mediaList = List;
        this.masterMediaList=List;
        this.mContext = mContext;
        this.firstTime = firstTime;
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

        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
            holder.deleteiconfolder.setVisibility(View.VISIBLE);
        }
        if (firstTime) {
            if (selectedFolderId == mediaList.get(position).getSno()) {
                holder.cardLayout.setVisibility(View.VISIBLE);
            } else {
                holder.cardLayout.setVisibility(View.GONE);
            }
        }
        holder.recentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFolderService((mediaList.get(position).getSno()));
            }
        });

        holder.deleteiconfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePersonalFolder(position);
            }
        });

    }

    private void callFolderService(int sno) {
        Intent intent = new Intent("FolderOpen");
        intent.putExtra("OpenFolder", true);
        intent.putExtra("FolderID", sno);
        selectedFolderId = sno;
        firstTime = true;
        mContext.sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    private void deletePersonalFolder(final int position) {
        SharedPreferences prefs = mContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
        if (ASTUIUtil.isOnline(mContext)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            final String url = Contants.BASE_URL + Contants.DeletePersonalSectionFolder + "username=" + UserId + "&" + "fsno=" + mediaList.get(position).getSno();
            serviceCaller.CallCommanServiceMethod(url, "deletePersonalFile", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.showToast("Folder delete Successfully");
                                mediaList.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "Folder not delete Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mContext, "Folder not delete Successfully!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ASTUIUtil.showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
        }
    }
}

