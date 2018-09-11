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
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.apitechnosoft.ipad.activity.ShareSingleFileActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.DownloadService;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.MyViewHolder> {

    public ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
    Typeface materialdesignicons_font;
    String UserId;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CheckBox selectCheck;
        ProgressBar loadingDialog;
        TextView deleteicon;


        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            selectCheck = view.findViewById(R.id.selectCheck);
            loadingDialog = view.findViewById(R.id.loadingDialog);
            deleteicon = view.findViewById(R.id.deleteicon);
            deleteicon.setTypeface(materialdesignicons_font);
            deleteicon.setText(Html.fromHtml("&#xf1c0;"));
        }
    }


    public PersonalAdapter(Context mContext, ArrayList<MediaData> List, int type) {
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
        SharedPreferences prefs = mContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
        holder.recenttext.setText(mediaList.get(position).getFileName());
        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
            holder.selectCheck.setVisibility(View.GONE);
            holder.deleteicon.setVisibility(View.VISIBLE);

        } else {
            holder.selectCheck.setVisibility(View.VISIBLE);
            holder.deleteicon.setVisibility(View.GONE);
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
                if (type == 1) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    // play(filePath, mediaList.get(position).getType());
                    alertForShowImage(filePath, position);
                } else if (type == 2) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowVideo(filePath, position);
                    // VideoPopup videoPopup = new VideoPopup(mContext, mediaList.get(position).getFileName(), filePath);
                    // videoPopup.show();
                    // play(filePath, mediaList.get(position).getType());
                } else if (type == 3) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    //  play(filePath, mediaList.get(position).getType());
                    alertForShowAudio(filePath, position);
                } else if (type == 4) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowDoc(filePath, mediaList.get(position).getType(), position);
                }
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

        holder.deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePersonalFolder(position);
            }
        });
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
        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        downloadicon.setTypeface(materialdesignicons_font);
        downloadicon.setText(Html.fromHtml("&#xf162;"));
        deleteicon.setTypeface(materialdesignicons_font);
        deleteicon.setText(Html.fromHtml("&#xf1c0;"));

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
        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
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
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        final View view = alert.getLayoutInflater().inflate(R.layout.show_video_view_layout, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView close = view.findViewById(R.id.close);
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
        final MediaController mediaController = new MediaController(mContext);
        final VideoView videoView = view.findViewById(R.id.videoView);
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                videoView.stopPlayback();
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
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        final View view = alert.getLayoutInflater().inflate(R.layout.show_video_view_layout, null);
        TextView updateDate = view.findViewById(R.id.updateDate);
        TextView close = view.findViewById(R.id.close);
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
        if (ASTUIUtil.isOnline(mContext)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            final String url = Contants.BASE_URL + Contants.DeleteFileApi + "username=" + UserId + "&" + "sno=" + mediaList.get(position).getSno() + "&" + "p=" + mediaList.get(position).getFilePath();
            ;
            //final String url = Contants.BASE_URL + Contants.DeleteShareFileApi + "username=" + UserId + "&" + "fsno=" + mediaList.get(position).getSno()
            // + "&" + "p=" + mediaList.get(position).getFilePath();
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
                // dotDialog.setProgress(progress);
                if (progress == 100) {
                    // dotDialog.dismiss();
                    ASTUIUtil.showToast("File Downloaded Successfully");
                }
            }
        }
    }


    private void deletePersonalFolder(final int position) {
        if (ASTUIUtil.isOnline(mContext)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            final String url = Contants.BASE_URL + Contants.DeletePersonalSectionFolder + "username=" + UserId + "&" + "fsno=" + mediaList.get(position).getSno();
            ;
            //final String url = Contants.BASE_URL + Contants.DeleteShareFileApi + "username=" + UserId + "&" + "fsno=" + mediaList.get(position).getSno()
            // + "&" + "p=" + mediaList.get(position).getFilePath();
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
}
