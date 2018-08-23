package com.apitechnosoft.ipad.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.MyViewHolder> {

    private ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
    Typeface materialdesignicons_font;
    MediaController mc;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.recenttext.setText(mediaList.get(position).getFileName());

        if (mediaList.get(position).getFullFilePath() != null && !mediaList.get(position).getFullFilePath().equals("")) {
            holder.recentImg.setImageResource(R.drawable.folder);
        } else {
            if (type == 1) {
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("image")) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(holder.recentImg);
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
                    alertForShowVideo(filePath,position);
                   // VideoPopup videoPopup = new VideoPopup(mContext, mediaList.get(position).getFileName(), filePath);
                   // videoPopup.show();
                   // play(filePath, mediaList.get(position).getType());
                } else if (type == 3) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    play(filePath, mediaList.get(position).getType());
                } else if (type == 4) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    play(filePath, mediaList.get(position).getType());
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

    private void play(String filePath, String mime) {
        Intent playAudioIntent = new Intent();
        playAudioIntent.setAction(Intent.ACTION_VIEW);
        playAudioIntent.setDataAndType(Uri.parse(filePath), mime);
        mContext.startActivity(playAudioIntent);
    }

    public void alertForShowImage(String filePath, int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.show_image_layout, null);
        TextView updateDate =  view.findViewById(R.id.updateDate);
        TextView close =view.findViewById(R.id.close);
        TextView downloadicon =  view.findViewById(R.id.downloadicon);
        TextView deleteicon =  view.findViewById(R.id.deleteicon);
        TextView title =  view.findViewById(R.id.title);
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
    public void alertForShowVideo(String filePath, int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        final View view = alert.getLayoutInflater().inflate(R.layout.show_video_view_layout, null);
        TextView updateDate =  view.findViewById(R.id.updateDate);
        TextView close =view.findViewById(R.id.close);
        TextView downloadicon =  view.findViewById(R.id.downloadicon);
        TextView deleteicon =  view.findViewById(R.id.deleteicon);
        TextView title =  view.findViewById(R.id.title);
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
       // mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

        videoView.setVideoURI( Uri.parse(filePath));
        videoView.requestFocus();
       // videoView.start();
        alert.setCustomTitle(view);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        Log.i(Contants.LOG_TAG, "Duration = " +
                                                                videoView.getDuration());
                                                        videoView.start();
                                                        mediaController.show(5000);
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

 /*   public  Dialog getVideoDialog(Context context, Uri videoLocation, boolean autoplay) {
        final Dialog dialog =getBaseDialog(context,true, R.layout.show_video_view_layout);

        ((Activity)context).getWindow().setFormat(PixelFormat.TRANSLUCENT);
        final VideoView videoHolder = (VideoView) dialog.findViewById(R.id.video_view);
        videoHolder.setVideoURI(videoLocation);
        //videoHolder.setRotation(90);
        MediaController mediaController =  new MediaController(context);
        videoHolder.setMediaController(mediaController);
        mediaController.setAnchorView(videoHolder);
        videoHolder.requestFocus();
        if(autoplay) {
            videoHolder.start();
        }
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();

            }
        });
        return dialog;
    }*/

    public static Dialog getBaseDialog(Context context, boolean cancelable, int layoutResID) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setContentView(layoutResID);

        return dialog;
    }
    @Override
    public int getItemCount() {
        return mediaList.size();
    }


}
