package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.CustomTextureVideoView;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.activity.ShareSingleFileActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.DownloadService;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Resentdata;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class RecentFileAdapter extends RecyclerView.Adapter<RecentFileAdapter.MyViewHolder> {

    private ArrayList<Resentdata> mediaList;
    Context mContext;
    Typeface materialdesignicons_font;


    @Override
    public void onViewRecycled(MyViewHolder holder) {
        if (holder == currentVideoViewHolder) {
            currentVideoViewHolder = null;
            holder.stopVideo();
        }
        holder.videoViewnew.stopPlayback();
        super.onViewRecycled(holder);

    }

    public void onScrolled(RecyclerView recyclerView) {
        if (currentVideoViewHolder != null) {
            currentVideoViewHolder.onScrolled(recyclerView);
        }
    }

    MyViewHolder currentVideoViewHolder;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        LinearLayout itemLayout;
        ProgressBar loadingDialog;
        RelativeLayout videoViewLayout;
        android.widget.VideoView videoView;
        ProgressBar bufferingDialog;

        ImageView videoPlayImageButton, videoImageView, fullView;
        ProgressBar imageLoaderProgressBar;
        CustomTextureVideoView videoViewnew;
        String videoUrl;

        public String getVideoUrl() {
            return videoUrl;
        }

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            itemLayout = view.findViewById(R.id.itemLayout);
            loadingDialog = view.findViewById(R.id.loadingDialog);
            videoView = view.findViewById(R.id.videoView);
            videoViewLayout = view.findViewById(R.id.videoViewLayout);
            bufferingDialog = view.findViewById(R.id.bufferingDialog);

            fullView = view.findViewById(R.id.fullView);
            videoPlayImageButton = view.findViewById(R.id.video_play_img_btn);
            imageLoaderProgressBar = view.findViewById(R.id.lyt_image_loader_progress_bar);
            videoViewnew = view.findViewById(R.id.video_feed_item_video);
            videoImageView = view.findViewById(R.id.video_feed_item_video_image);

            videoViewnew.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    Log.v("Video", "onPrepared" + videoViewnew.getVideoPath());
                    int width = mp.getVideoWidth();
                    int height = mp.getVideoHeight();
                    videoViewnew.setIsPrepared(true);
                    //  UIUtils.resizeView(videoViewnew, UIUtils.getScreenWidth(ApplicationHelper.application().getActivity()), UIUtils.getScreenWidth(ApplicationHelper.application().getActivity()) * height / width);
                    if (currentVideoViewHolder == MyViewHolder.this) {
                        videoImageView.setVisibility(View.GONE);
                        imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                        videoViewnew.setVisibility(View.VISIBLE);
                        videoViewnew.seekTo(0);
                        videoViewnew.start();
                    }
                }
            });
            videoViewnew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.v("Video", "onFocusChange" + hasFocus);
                    if (!hasFocus && currentVideoViewHolder == MyViewHolder.this) {
                        //  stopVideo();
                    }

                }
            });
            videoViewnew.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    Log.v("Video", "onInfo" + what + " " + extra);

                    return false;
                }
            });
            videoViewnew.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.v("Video", "onCompletion");

                    videoImageView.setVisibility(View.VISIBLE);
                    videoPlayImageButton.setVisibility(View.VISIBLE);

                    if (videoViewnew.getVisibility() == View.VISIBLE)
                        videoViewnew.setVisibility(View.INVISIBLE);


                    imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                    currentVideoViewHolder = null;
                }
            });
            videoPlayImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentVideoViewHolder != null && currentVideoViewHolder != MyViewHolder.this) {
                        currentVideoViewHolder.videoViewnew.pause();
                        currentVideoViewHolder.videoImageView.setVisibility(View.INVISIBLE);
                        currentVideoViewHolder.videoPlayImageButton.setVisibility(View.VISIBLE);
                        currentVideoViewHolder.imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                        if (currentVideoViewHolder.videoViewnew.getVisibility() == View.VISIBLE)
                            currentVideoViewHolder.videoViewnew.setVisibility(View.INVISIBLE);


                        currentVideoViewHolder = null;
                    }
                    currentVideoViewHolder = MyViewHolder.this;

                    videoPlayImageButton.setVisibility(View.INVISIBLE);
                    imageLoaderProgressBar.setVisibility(View.VISIBLE);
                    videoViewnew.setVisibility(View.VISIBLE);
                    videoImageView.setVisibility(View.INVISIBLE);
                    if (videoViewnew.isPrepared()) {
                        imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                    } else {
                        imageLoaderProgressBar.setVisibility(View.VISIBLE);
                    }
                    if (!getVideoUrl().equals(videoViewnew.getVideoPath())) {
                        videoViewnew.setIsPrepared(false);
                        //videoView.setVideoPath(getVideoUrl());
                        videoViewnew.setVideoURI(Uri.parse(getVideoUrl()));
                        videoViewnew.requestFocus();
                        //  videoViewnew.seekTo(0);
                        // videoViewnew.start();
                    } else {
                        if (videoViewnew.isPrepared()) {
                            imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                        } else {
                            imageLoaderProgressBar.setVisibility(View.VISIBLE);
                        }
                        videoViewnew.requestFocus();
                        videoViewnew.seekTo(0);
                        videoViewnew.start();
                    }
                }
            });
        }

        public void stopVideo() {
            Log.v("Video", "stopVideo");

            //imageView is within the visible window
            videoViewnew.pause();
            if (videoViewnew.getVisibility() == View.VISIBLE) {
                videoViewnew.setVisibility(View.INVISIBLE);
            }
            videoImageView.setVisibility(View.VISIBLE);
            videoPlayImageButton.setVisibility(View.VISIBLE);
            imageLoaderProgressBar.setVisibility(View.INVISIBLE);
            currentVideoViewHolder = null;
        }

        public void onScrolled(RecyclerView recyclerView) {
            if (isViewNotVisible(videoPlayImageButton, recyclerView) || isViewNotVisible(imageLoaderProgressBar, recyclerView)) {
                //imageView is within the visible window
                stopVideo();
            }
        }

        public boolean isViewNotVisible(View view, RecyclerView recyclerView) {
            Rect scrollBounds = new Rect();
            recyclerView.getHitRect(scrollBounds);
            return view.getVisibility() == View.VISIBLE && !view.getLocalVisibleRect(scrollBounds);
        }
    }


    public RecentFileAdapter(Context mContext, ArrayList<Resentdata> List) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());
        if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("jpg") || mediaList.get(position).getExtension().contains("jpeg") || mediaList.get(position).getExtension().contains("png")) || mediaList.get(position).getExtension().contains("PNG") || mediaList.get(position).getExtension().contains("JPG") || mediaList.get(position).getExtension().contains("JPEG")) {
            setVisibilityView(holder, false);
            if (holder.loadingDialog != null) {
                holder.loadingDialog.setVisibility(View.VISIBLE);
            }
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            Picasso.with(ApplicationHelper.application().getContext()).load(filePath).resize(70, 70).into(holder.recentImg, new Callback() {
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
        } else if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("mp4") || mediaList.get(position).getExtension().contains("wmv") || mediaList.get(position).getExtension().contains("3gp") || mediaList.get(position).getExtension().contains("avi") || mediaList.get(position).getExtension().contains("flv") || mediaList.get(position).getExtension().contains("m4v") || mediaList.get(position).getExtension().contains("mkv") || mediaList.get(position).getExtension().contains("mov") || mediaList.get(position).getExtension().contains("mpeg") || mediaList.get(position).getExtension().contains("mpg") || mediaList.get(position).getExtension().contains("mts") || mediaList.get(position).getExtension().contains("webm"))) {
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            holder.videoUrl = filePath;
            setVisibilityView(holder, true);
            // holder.recentImg.setImageResource(R.drawable.video);
           /* if (mediaList.get(position).getThamblingImage() != null && !mediaList.get(position).getThamblingImage().equals("")) {
                String newpath = mediaList.get(position).getThamblingImage().replace("C:\\xampp\\tomcat\\webapps\\ROOT\\", Contants.Media_File_BASE_URL);
                Picasso.with(mContext).load(newpath).placeholder(R.drawable.video).into(holder.recentImg);
            } else {
                holder.recentImg.setImageResource(R.drawable.video);
            }*/
          /*  String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
          holder.videoView.setVideoURI(Uri.parse(filePath));
            holder.videoView.requestFocus();
            holder.videoView.seekTo(300);
            holder.videoView.pause();
          // holder.videoView.setBackgroundColor(Color.parseColor("#D9D9D9")); // Your color.
            holder.bufferingDialog.setVisibility(View.VISIBLE);
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                   // holder.videoView.setBackgroundColor(Color.TRANSPARENT);
                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                                holder.bufferingDialog.setVisibility(View.VISIBLE);
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                holder.bufferingDialog.setVisibility(View.VISIBLE);
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                holder.bufferingDialog.setVisibility(View.GONE);
                            return false;
                        }
                    });
                }
            });
            holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    holder.bufferingDialog.setVisibility(View.GONE);
                    return false;
                }
            });*/
        } else if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("mp3") || mediaList.get(position).getExtension().contains("wav")) || mediaList.get(position).getExtension().contains("m4a")) {
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            holder.videoUrl = filePath;
            setVisibilityView(holder, true);
        } else if (mediaList.get(position).getExtension().contains("doc") || mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("txt") || mediaList.get(position).getExtension().contains("docx"))) {
            setVisibilityView(holder, false);
            holder.recentImg.setImageResource(R.drawable.doc);
        } else if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("pptx") || mediaList.get(position).getExtension().contains("ppt"))) {
            setVisibilityView(holder, false);
            holder.recentImg.setImageResource(R.drawable.pptimg);
        } else if (mediaList.get(position).getExtension().contains("xls") || mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("xlsx"))) {
            setVisibilityView(holder, false);
            holder.recentImg.setImageResource(R.drawable.excelimg);
        } else if (mediaList.get(position).getExtension() != null && mediaList.get(position).getExtension().contains("pdf")) {
            setVisibilityView(holder, false);
            holder.recentImg.setImageResource(R.drawable.pdfimg);
        } else if (mediaList.get(position).getExtension() != null && mediaList.get(position).getExtension().contains("zip")) {
            setVisibilityView(holder, false);
            holder.recentImg.setImageResource(R.drawable.zipimg);
        } else if (mediaList.get(position).getExtension().contains("rar") || mediaList.get(position).getExtension().contains("RAR")) {
            setVisibilityView(holder, false);
            holder.recentImg.setImageResource(R.drawable.araimg);
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("jpg") || mediaList.get(position).getExtension().contains("jpeg") || mediaList.get(position).getExtension().contains("png")) || mediaList.get(position).getExtension().contains("PNG") || mediaList.get(position).getExtension().contains("JPG") || mediaList.get(position).getExtension().contains("JPEG")) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowImage(filePath, position);
                } else {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowDoc(filePath, mediaList.get(position).getType(), position);
                }
            }
        });

        holder.fullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.stopVideo();
                holder.videoViewnew.stopPlayback();
                if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("mp4") || mediaList.get(position).getExtension().contains("wmv") || mediaList.get(position).getExtension().contains("3gp") || mediaList.get(position).getExtension().contains("avi") || mediaList.get(position).getExtension().contains("flv") || mediaList.get(position).getExtension().contains("m4v") || mediaList.get(position).getExtension().contains("mkv") || mediaList.get(position).getExtension().contains("mov") || mediaList.get(position).getExtension().contains("mpeg") || mediaList.get(position).getExtension().contains("mpg") || mediaList.get(position).getExtension().contains("mts") || mediaList.get(position).getExtension().contains("webm"))) {
                    holder.stopVideo();
                    holder.videoViewnew.stopPlayback();
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowVideo(filePath, position);
                } else if (mediaList.get(position).getExtension() != null && (mediaList.get(position).getExtension().contains("mp3") || mediaList.get(position).getExtension().contains("wav"))) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowAudio(filePath, position);
                }
            }
        });
    }

    private void setVisibilityView(MyViewHolder holder, boolean flag) {
        if (flag) {
            holder.imageLoaderProgressBar.setVisibility(View.INVISIBLE);//INVISIBLE
            holder.videoImageView.setVisibility(View.VISIBLE);//VISIBLE
            holder.fullView.setVisibility(View.VISIBLE);

            holder.recentImg.setVisibility(View.GONE);
            holder.videoViewLayout.setVisibility(View.GONE);
        } else {
            holder.videoImageView.setVisibility(View.GONE);
            holder.videoPlayImageButton.setVisibility(View.GONE);
            holder.fullView.setVisibility(View.GONE);

            holder.recentImg.setVisibility(View.VISIBLE);
            holder.videoViewLayout.setVisibility(View.VISIBLE);
        }
    }

    private void alertForShowDoc(final String filePath, String mime, final int position) {
       /* Intent playAudioIntent = new Intent(mContext, DocOpenActivity.class);
        playAudioIntent.putExtra("FileUrl", filePath);
        mContext.startActivity(playAudioIntent);*/
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.show_doc_layout, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView close = view.findViewById(R.id.close);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        WebView webView = view.findViewById(R.id.web);
        updateDate.setText("Uploaded On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));
        final ProgressBar loadingDialog = view.findViewById(R.id.loadingDialog);
        loadingDialog.setVisibility(View.VISIBLE);
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
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loadingDialog.setVisibility(View.GONE);
            }
        });
        if (filePath != null) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + filePath);
        }


        alert.setCustomTitle(view);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShareSingleFileActivity.class);
                String media = new Gson().toJson(mediaList.get(position));
                intent.putExtra("MediaData", media);
                intent.putExtra("MediaType", 2);
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

    public void alertForShowImage(final String filePath, final int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.show_image_layout, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView close = view.findViewById(R.id.close);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        ImageView img = view.findViewById(R.id.img);
        final ProgressBar loadingDialog = view.findViewById(R.id.loadingDialog);
        updateDate.setText("Uploaded On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        loadingDialog.setVisibility(View.VISIBLE);
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShareSingleFileActivity.class);
                String media = new Gson().toJson(mediaList.get(position));
                intent.putExtra("MediaData", media);
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

    public void alertForShowVideo(final String filePath, final int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        alert.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        alert.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        final View view = alert.getLayoutInflater().inflate(R.layout.show_video_view_layout, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView close = view.findViewById(R.id.close);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        updateDate.setText("Uploaded On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));

        ProgressBar loader_progress_bar = view.findViewById(R.id.loader_progress_bar);
        CustomTextureVideoView video_feed_item_video = view.findViewById(R.id.video_feed_item_video);
        video_feed_item_video.setVideoURI(Uri.parse(filePath));
        if (video_feed_item_video.isPrepared()) {
            loader_progress_bar.setVisibility(View.INVISIBLE);
        } else {
            loader_progress_bar.setVisibility(View.VISIBLE);
        }
        // video_feed_item_video.setIsPrepared(true);
        video_feed_item_video.requestFocus();
        video_feed_item_video.seekTo(0);
        video_feed_item_video.start();

        alert.setCustomTitle(view);

        video_feed_item_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                Log.v("Video", "onPrepared" + video_feed_item_video.getVideoPath());
                int width = mp.getVideoWidth();
                int height = mp.getVideoHeight();
                loader_progress_bar.setVisibility(View.INVISIBLE);
                video_feed_item_video.setIsPrepared(true);
              /*  video_feed_item_video.requestFocus();
                video_feed_item_video.seekTo(0);
                video_feed_item_video.start();*/
            }
        });
        video_feed_item_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                loader_progress_bar.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        video_feed_item_video.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("Video", "onFocusChange" + hasFocus);

            }
        });
        video_feed_item_video.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.v("Video", "onInfo" + what + " " + extra);

                return false;
            }
        });
        video_feed_item_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.v("Video", "onCompletion");
                loader_progress_bar.setVisibility(View.INVISIBLE);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_feed_item_video.isPlaying()) {
                    video_feed_item_video.stopPlayback();
                    //  videoView.release();
                }
                alert.dismiss();
            }
        });
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShareSingleFileActivity.class);
                String media = new Gson().toJson(mediaList.get(position));
                intent.putExtra("MediaData", media);
                intent.putExtra("MediaType", 3);
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

    public void alertForShowAudio(final String filePath, final int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        alert.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        final View view = alert.getLayoutInflater().inflate(R.layout.show_video_view_layout, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView close = view.findViewById(R.id.close);
        TextView downloadicon = view.findViewById(R.id.downloadicon);
        TextView deleteicon = view.findViewById(R.id.deleteicon);
        TextView title = view.findViewById(R.id.title);
        Button sharebt = view.findViewById(R.id.sharebt);
        updateDate.setText("Uploaded On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));

        ProgressBar loader_progress_bar = view.findViewById(R.id.loader_progress_bar);
        CustomTextureVideoView video_feed_item_video = view.findViewById(R.id.video_feed_item_video);
        video_feed_item_video.setVideoURI(Uri.parse(filePath));
        if (video_feed_item_video.isPrepared()) {
            loader_progress_bar.setVisibility(View.INVISIBLE);
        } else {
            loader_progress_bar.setVisibility(View.VISIBLE);
        }
        // video_feed_item_video.setIsPrepared(true);
        video_feed_item_video.requestFocus();
        video_feed_item_video.seekTo(0);
        video_feed_item_video.start();

        alert.setCustomTitle(view);

        video_feed_item_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                Log.v("Video", "onPrepared" + video_feed_item_video.getVideoPath());
                int width = mp.getVideoWidth();
                int height = mp.getVideoHeight();
                loader_progress_bar.setVisibility(View.INVISIBLE);
                video_feed_item_video.setIsPrepared(true);
              /*  video_feed_item_video.requestFocus();
                video_feed_item_video.seekTo(0);
                video_feed_item_video.start();*/
            }
        });
        video_feed_item_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                loader_progress_bar.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        video_feed_item_video.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("Video", "onFocusChange" + hasFocus);

            }
        });
        video_feed_item_video.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.v("Video", "onInfo" + what + " " + extra);

                return false;
            }
        });
        video_feed_item_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.v("Video", "onCompletion");
                loader_progress_bar.setVisibility(View.INVISIBLE);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_feed_item_video.isPlaying()) {
                    video_feed_item_video.stopPlayback();
                    //  videoView.release();
                }
                alert.dismiss();
            }
        });
        sharebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShareSingleFileActivity.class);
                String media = new Gson().toJson(mediaList.get(position));
                intent.putExtra("MediaData", media);
                intent.putExtra("MediaType", 4);
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
            final String url = Contants.BASE_URL + Contants.DeleteFileApi + "username=" + UserId + "&" + "sno=" + mediaList.get(position).getSno() + "&" + "p=" + mediaList.get(position).getFilePath();
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

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String filePath;

        public DownloadImage(ImageView bmImage, String filePath) {
            this.bmImage = (ImageView) bmImage;
            this.filePath = filePath;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap myBitmap = null;
            MediaMetadataRetriever mMRetriever = null;
            try {
                mMRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= 14)
                    mMRetriever.setDataSource(filePath, new HashMap<String, String>());
                else
                    mMRetriever.setDataSource(filePath);
                myBitmap = mMRetriever.getFrameAtTime();
            } catch (Exception e) {
                e.printStackTrace();


            } finally {
                if (mMRetriever != null) {
                    mMRetriever.release();
                }
            }
            return myBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}

