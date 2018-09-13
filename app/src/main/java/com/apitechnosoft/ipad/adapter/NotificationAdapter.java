package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.NotificationActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Emaildata;
import com.apitechnosoft.ipad.model.Notificationlist;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<Notificationlist> notificationlists;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email, filename, comment;
        private ImageView image;
        ProgressBar loadingDialog;
        LinearLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);
            email = view.findViewById(R.id.email);
            filename = view.findViewById(R.id.filename);
            comment = view.findViewById(R.id.comment);
            image = view.findViewById(R.id.image);
            loadingDialog = view.findViewById(R.id.loadingDialog);
            mainLayout = view.findViewById(R.id.mainLayout);
        }
    }


    public NotificationAdapter(Context mContext, ArrayList<Notificationlist> arrayList) {
        this.notificationlists = arrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.email.setText(notificationlists.get(position).getPath());
        holder.comment.setText(notificationlists.get(position).getCommentl());
        holder.filename.setText(notificationlists.get(position).getFilename());
        if (notificationlists.get(position).getEmailId().contains("image")) {
            String filePath = Contants.Media_File_BASE_URL + notificationlists.get(position).getFolderlocation() + "/" + notificationlists.get(position).getFilename();
            Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    if (holder.loadingDialog != null) {
                        holder.loadingDialog.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError() {
                    if (holder.loadingDialog != null) {
                        holder.loadingDialog.setVisibility(View.GONE);
                    }
                }
            });
        } else if (notificationlists.get(position).getEmailId().contains("video")) {
            holder.image.setImageResource(R.drawable.video);
        } else if (notificationlists.get(position).getEmailId().contains("audio")) {
            holder.image.setImageResource(R.drawable.audio_icon);
        } else if (notificationlists.get(position).getEmailId().contains("text")) {
            holder.image.setImageResource(R.drawable.doc);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRemoveNotification(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationlists.size();
    }

    private void getRemoveNotification(final int pos) {
        String UserId = "";
        SharedPreferences prefs = mContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
        if (ASTUIUtil.isOnline(mContext)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            final String url = Contants.BASE_URL + Contants.DeleteSingleNotification + "username=" + UserId + "&" + "emailid=" + UserId + "&" + "sno=" + notificationlists.get(pos).getCount();
            serviceCaller.CallCommanServiceMethod(url, "getRemoveNotification", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            Log.d(Contants.LOG_TAG, "Get All RemoveNotification**" + result);
                            if (data.getNotificationstatus().equals("true")) {
                                notificationlists.remove(pos);
                                notifyDataSetChanged();
                            }

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



