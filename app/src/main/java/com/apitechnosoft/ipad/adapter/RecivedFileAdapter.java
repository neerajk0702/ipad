package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ShareImageActivity;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecivedFileAdapter extends RecyclerView.Adapter<RecivedFileAdapter.MyViewHolder> {

    private ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
        }
    }


    public RecivedFileAdapter(Context mContext, ArrayList<MediaData> List, int type) {
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());

        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
        } else {
            if (type == 1) {
                if (mediaList.get(position).getFileExtension() != null && mediaList.get(position).getFileExtension().contains("image")) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderName() + "/" + mediaList.get(position).getFileName();
                    Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(holder.recentImg);
                }
            } else if (type == 2) {
                if (mediaList.get(position).getFileExtension() != null && mediaList.get(position).getFileExtension().contains("video")) {
                    holder.recentImg.setImageResource(R.drawable.video);
                    //  Picasso.with(ApplicationHelper.application().getContext()).load(mediaList.get(position).getFullFilePath()).into(holder.recentImg);
                }
            } else if (type == 3) {
                if (mediaList.get(position).getFileExtension() != null && mediaList.get(position).getFileExtension().contains("audio")) {
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
                } else if (type == 3) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowAudio(filePath, position);
                } else if (type == 4) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    alertForShowDoc(filePath, mediaList.get(position).getType(), position);
                }
            }
        });
        /*if (mediaList.get(position).getType().contains("image")) {
            holder.recentImg.setImageResource(R.drawable.image_placeholder);
        } else if (mediaList.get(position).getType().contains("video")) {
            holder.recentImg.setImageResource(R.drawable.video);
        } else if (mediaList.get(position).getType().contains("audio")) {
            holder.recentImg.setImageResource(R.drawable.audio_icon);
        } else if (mediaList.get(position).getType().contains("doc")) {
            holder.recentImg.setImageResource(R.drawable.doc);
        }*/
    }

    private void alertForShowDoc(String filePath, String mime, final int position) {
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
                Intent intent = new Intent(mContext, ShareImageActivity.class);
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
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public void alertForShowImage(String filePath, final int position) {
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
        updateDate.setText("Update On:" + mediaList.get(position).getEnteredDate().toString());
        title.setText(mediaList.get(position).getFileName());
        Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(img);
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
                Intent intent = new Intent(mContext, ShareImageActivity.class);
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
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public void alertForShowVideo(String filePath, final int position) {
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
                Intent intent = new Intent(mContext, ShareImageActivity.class);
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
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
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
                Intent intent = new Intent(mContext, ShareImageActivity.class);
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
            }
        });
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }


}
