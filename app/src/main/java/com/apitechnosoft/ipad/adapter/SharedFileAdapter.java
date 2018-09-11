package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.CommentOnFileActivity;
import com.apitechnosoft.ipad.activity.ShareSingleFileActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.DownloadService;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Emaildata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class SharedFileAdapter extends RecyclerView.Adapter<SharedFileAdapter.MyViewHolder> {

    public ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CheckBox selectCheck;
        ProgressBar loadingDialog;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            selectCheck = view.findViewById(R.id.selectCheck);
            loadingDialog = view.findViewById(R.id.loadingDialog);
        }
    }


    public SharedFileAdapter(Context mContext, ArrayList<MediaData> List, int type) {
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

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.recenttext.setText(mediaList.get(position).getFileName());

        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
            holder.selectCheck.setVisibility(View.GONE);
        } else {
            holder.selectCheck.setVisibility(View.VISIBLE);
            if (type == 1) {
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("image")) {
                    if (holder.loadingDialog != null) {
                        holder.loadingDialog.setVisibility(View.VISIBLE);
                    }
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(holder.recentImg, new Callback() {
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
                }
            } else if (type == 2) {
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("video")) {
                    holder.recentImg.setImageResource(R.drawable.video);
                    //  Picasso.with(ApplicationHelper.application().getContext()).load(mediaList.get(position).getFullFilePath()).into(holder.recentImg);
                }
            } else if (type == 3) {
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("audio")) {
                    holder.recentImg.setImageResource(R.drawable.audio_icon);
                    //  Picasso.with(ApplicationHelper.application().getContext()).load(mediaList.get(position).getFullFilePath()).into(holder.recentImg);

                }
            } else if (type == 4) {
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
            }
        }
        holder.recentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllSharedFileComments(position);
            }
        });
        if (mediaList.get(position).isSelected()) {
            holder.selectCheck.setChecked(true);
        } else {
            holder.selectCheck.setChecked(false);
        }
        holder.selectCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    private void alertForShowDoc(final int position, ArrayList<Emaildata> emailList) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.shared_doc_item, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        WebView webView = view.findViewById(R.id.web);
        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));

        RecyclerView recyclerView = view.findViewById(R.id.emailrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (emailList != null && emailList.size() > 0) {
            SharedRecivedEmailAdapter recivedEmailAdapter = new SharedRecivedEmailAdapter(mContext, emailList);
            recyclerView.setAdapter(recivedEmailAdapter);
        }
        final String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        webView.getSettings().setAllowFileAccess(true);
        if (filePath != null) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + filePath);
        }


        alert.setCustomTitle(view);
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentOnFileActivity.class);
                intent.putExtra("ItemSno", mediaList.get(position).getItemSno() + "");
                intent.putExtra("FilePath", mediaList.get(position).getFilePath());
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("ShareSno", mediaList.get(position).getShareSno() + "");
                intent.putExtra("Sno", mediaList.get(position).getSno() + "");
                intent.putExtra("MediaType", 2);
                intent.putExtra("ShowfilePath", filePath);
                mContext.startActivity(intent);
                alert.dismiss();
            }
        });
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                deletePersonalFile(position);
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("url", filePath);
                intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                mContext.startService(intent);
            }
        });
        alert.show();
    }

    public void alertForShowImage(final int position, ArrayList<Emaildata> emailList) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.shared_image_item, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        ImageView img = view.findViewById(R.id.img);
        RecyclerView recyclerView = view.findViewById(R.id.emailrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (emailList != null && emailList.size() > 0) {
            SharedRecivedEmailAdapter recivedEmailAdapter = new SharedRecivedEmailAdapter(mContext, emailList);
            recyclerView.setAdapter(recivedEmailAdapter);
        }

        final ProgressBar loadingDialog = view.findViewById(R.id.loadingDialog);
        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        loadingDialog.setVisibility(View.VISIBLE);
        final String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
        Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(img, new Callback() {
            @Override
            public void onSuccess() {
                loadingDialog.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                loadingDialog.setVisibility(View.GONE);
            }
        });
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));

        alert.setCustomTitle(view);
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentOnFileActivity.class);
                intent.putExtra("ItemSno", mediaList.get(position).getItemSno() + "");
                intent.putExtra("FilePath", mediaList.get(position).getFilePath());
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("ShareSno", mediaList.get(position).getShareSno() + "");
                intent.putExtra("Sno", mediaList.get(position).getSno() + "");
                intent.putExtra("ShowfilePath", filePath);
                intent.putExtra("MediaType", 1);

                mContext.startActivity(intent);
                alert.dismiss();
            }
        });
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                deletePersonalFile(position);
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("url", filePath);
                intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                mContext.startService(intent);
            }
        });
        alert.show();
    }

    public void alertForShowVideo(final int position, ArrayList<Emaildata> emailList) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        final View view = alert.getLayoutInflater().inflate(R.layout.shared_video_item, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        RecyclerView recyclerView = view.findViewById(R.id.emailrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (emailList != null && emailList.size() > 0) {
            SharedRecivedEmailAdapter recivedEmailAdapter = new SharedRecivedEmailAdapter(mContext, emailList);
            recyclerView.setAdapter(recivedEmailAdapter);
        }

        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));
        final MediaController mediaController = new MediaController(mContext);
        final VideoView videoView = view.findViewById(R.id.videoView);

        final String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(filePath));
        //  videoView.setVideoPath(filePath);
        videoView.requestFocus();
        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
        ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(mediaController);
        mediaController.setVisibility(View.VISIBLE);
        // videoView.start();
        alert.setCustomTitle(view);
        final ProgressBar bufferingDialog = view.findViewById(R.id.bufferingDialog);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.requestFocus();
                videoView.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp,
                                                   int width, int height) {

                        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
                        ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(mediaController);
                        mediaController.setVisibility(View.VISIBLE);
                    }
                });

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                bufferingDialog.setVisibility(View.GONE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                bufferingDialog.setVisibility(View.VISIBLE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                bufferingDialog.setVisibility(View.GONE);
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                bufferingDialog.setVisibility(View.GONE);
                return false;
            }
        });
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentOnFileActivity.class);
                intent.putExtra("ItemSno", mediaList.get(position).getItemSno() + "");
                intent.putExtra("FilePath", mediaList.get(position).getFilePath());
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("ShareSno", mediaList.get(position).getShareSno() + "");
                intent.putExtra("Sno", mediaList.get(position).getSno() + "");
                intent.putExtra("MediaType", 3);
                intent.putExtra("ShowfilePath", filePath);
                mContext.startActivity(intent);
                alert.dismiss();
            }
        });
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                deletePersonalFile(position);
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("url", filePath);
                intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                mContext.startService(intent);
            }
        });
        alert.show();
    }

    public void alertForShowAudio(final int position, ArrayList<Emaildata> emailList) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        final View view = alert.getLayoutInflater().inflate(R.layout.shared_video_item, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));
        RecyclerView recyclerView = view.findViewById(R.id.emailrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (emailList != null && emailList.size() > 0) {
            SharedRecivedEmailAdapter recivedEmailAdapter = new SharedRecivedEmailAdapter(mContext, emailList);
            recyclerView.setAdapter(recivedEmailAdapter);
        }

        final String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
        final MediaController mediaController = new MediaController(mContext);
        final VideoView videoView = view.findViewById(R.id.videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(filePath));
        // videoView.requestFocus();
        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
        ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(mediaController);
        mediaController.setVisibility(View.VISIBLE);
        // videoView.start();
        alert.setCustomTitle(view);
        final ProgressBar bufferingDialog = view.findViewById(R.id.bufferingDialog);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.requestFocus();
                videoView.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp,
                                                   int width, int height) {

                        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
                        ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(mediaController);
                        mediaController.setVisibility(View.VISIBLE);
                    }
                });
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                bufferingDialog.setVisibility(View.GONE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                bufferingDialog.setVisibility(View.VISIBLE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                bufferingDialog.setVisibility(View.GONE);
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                bufferingDialog.setVisibility(View.GONE);
                return false;
            }
        });
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentOnFileActivity.class);
                intent.putExtra("ItemSno", mediaList.get(position).getItemSno() + "");
                intent.putExtra("FilePath", mediaList.get(position).getFilePath());
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("ShareSno", mediaList.get(position).getShareSno() + "");
                intent.putExtra("Sno", mediaList.get(position).getSno() + "");
                intent.putExtra("MediaType", 4);
                intent.putExtra("ShowfilePath", filePath);
                mContext.startActivity(intent);
                alert.dismiss();
            }
        });
        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                deletePersonalFile(position);
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.putExtra("FileName", mediaList.get(position).getFileName());
                intent.putExtra("url", filePath);
                intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                mContext.startService(intent);
            }
        });
        alert.show();
    }

  /*  public void audioPlayer(String path) {
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(path);
            mp.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mp.start();
    }*/

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    private void deletePersonalFile(final int position) {
        String UserId = "";
        SharedPreferences prefs = mContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
        if (ASTUIUtil.isOnline(mContext)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            //final String url = Contants.BASE_URL + Contants.DeleteShareFileApi + "username=" + UserId + "&" + "fsno=" + mediaList.get(position).getSno() + "&" + "p=" + mediaList.get(position).getFilePath();
            final String url = Contants.BASE_URL + Contants.DeleteShareFileApi + "username=" + UserId + "&" + "fsno=" + mediaList.get(position).getItemSno();

            serviceCaller.CallCommanServiceMethod(url, "deletePersonalFile", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.showToast("File delete Successfully");
                                mediaList.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "File not delete Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mContext, "File not delete Successfully!", Toast.LENGTH_LONG).show();
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

    //get all shared comments
    private void getAllSharedFileComments(final int position) {
        SharedPreferences prefs = mContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");

            if (ASTUIUtil.isOnline(mContext)) {
                final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(mContext);
                final String url = Contants.BASE_URL + Contants.GetShareFileCommentandEmail + "username=" + UserId + "&" + "itemsno=" + mediaList.get(position).getItemSno() + "&" + "sharesno=" + mediaList.get(position).getShareSno() + "&" + "sno=" + mediaList.get(position).getSno() + "&" + "search_keyword=";
                serviceCaller.CallCommanServiceMethod(url, "getAllSharedFileComments", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        Log.d(Contants.LOG_TAG, "Get All File**" + result);
                        ArrayList<Emaildata> emailList = null;
                        if (isComplete) {
                            ContentData data = new Gson().fromJson(result, ContentData.class);
                            if (data != null) {
                                if (data.getEmaildata() != null) {
                                    emailList = new ArrayList<Emaildata>(Arrays.asList(data.getEmaildata()));
                                } else {
                                    Toast.makeText(mContext, "Shared Email No Data found!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Shared Email No Data found!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            ASTUIUtil.showToast(Contants.Error);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        openPopup(position, emailList);
                    }
                });
            } else {
                ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
            }
        }
    }

    private void openPopup(int position, ArrayList<Emaildata> emailList) {
        if (type == 1) {
            alertForShowImage(position, emailList);
        } else if (type == 2) {
            alertForShowVideo(position, emailList);
        } else if (type == 3) {
            alertForShowAudio(position, emailList);
        } else if (type == 4) {
            alertForShowDoc(position, emailList);
        }
    }

    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                if (progress == 100) {
                    ASTUIUtil.showToast("File Downloaded Successfully");
                }
            }
        }
    }
}

