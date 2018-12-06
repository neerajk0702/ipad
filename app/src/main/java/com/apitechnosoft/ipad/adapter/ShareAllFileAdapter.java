package com.apitechnosoft.ipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ShareSingleFileActivity;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.DownloadService;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static java.security.AccessController.getContext;

public class ShareAllFileAdapter extends RecyclerView.Adapter<ShareAllFileAdapter.MyViewHolder> {

    public ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recenttext;
        ImageView recentImg;
        CheckBox selectCheck;
        RelativeLayout videoViewLayout;
        VideoView videoView;
        ProgressBar bufferingDialog;

        public MyViewHolder(View view) {
            super(view);
            recenttext = (TextView) view.findViewById(R.id.recenttext);
            recentImg = view.findViewById(R.id.recentImg);
            selectCheck = view.findViewById(R.id.selectCheck);
            videoViewLayout = view.findViewById(R.id.videoViewLayout);
            bufferingDialog = view.findViewById(R.id.bufferingDialog);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_row, parent, false);//recent_row

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
        holder.setIsRecyclable(false);
        holder.recenttext.setText(mediaList.get(position).getFileName());
        setCheckBoxColor(holder.selectCheck, ASTUIUtil.getColor(R.color.green_color), ASTUIUtil.getColor(R.color.selectfolder));

        holder.selectCheck.setVisibility(View.VISIBLE);
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("image")) {
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            Picasso.with(mContext).load(filePath).resize(70, 70).placeholder(R.drawable.image_icon).into(holder.recentImg);
        }
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("video")) {
            holder.recentImg.setImageResource(R.drawable.video);
            holder.recentImg.setVisibility(View.VISIBLE);
            holder.videoViewLayout.setVisibility(View.GONE);
            // holder.recentImg.setImageResource(R.drawable.video);
         /*   if (mediaList.get(position).getThamblingImage() != null && !mediaList.get(position).getThamblingImage().equals("")) {
                String newpath = mediaList.get(position).getThamblingImage().replace("C:\\xampp\\tomcat\\webapps\\ROOT\\", Contants.Media_File_BASE_URL);
                Picasso.with(mContext).load(newpath).placeholder(R.drawable.video).into(holder.recentImg);
            } else {
                holder.recentImg.setImageResource(R.drawable.video);
            }
*/
         /*   String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();

            holder.videoView.setVideoURI(Uri.parse(filePath));
            holder.videoView.requestFocus();
            holder.videoView.seekTo(300);
            holder.videoView.pause();
            holder.videoView.requestFocus();
            holder.videoView.start();
            holder.videoView.setBackgroundColor(Color.parseColor("#D9D9D9")); // Your color.*/
          /*  holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    holder.videoView.setBackgroundColor(Color.TRANSPARENT);
                }
            });*/
           /* holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    holder.videoView.setBackgroundColor(Color.TRANSPARENT);
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
            /*String filePath = Contants.Media_File_BASE_URL +
                    mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            //  holder.recentImg.setVisibility(View.GONE);
            try {
                holder.videoViewLayout.setVisibility(View.GONE);
                holder.recentImg.setVisibility(View.VISIBLE);
                // Bitmap bitmap= ASTUtil.retriveVideoFrameFromURL(filePath);

                new DownloadImage(holder.recentImg, filePath).execute(filePath);
                //  holder.recentImg.setImageBitmap(bitmap);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }*/
        }
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("audio")) {
            holder.recentImg.setImageResource(R.drawable.audio_icon);
        }
        if (mediaList.get(position).getExtension() != null) {
            if (mediaList.get(position).getExtension().contains("doc") || mediaList.get(position).getExtension().contains("docx") || mediaList.get(position).getExtension().contains("txt")) {
                holder.recentImg.setImageResource(R.drawable.doc);
            } else if (mediaList.get(position).getExtension().contains("pdf")) {
                holder.recentImg.setImageResource(R.drawable.pdfimg);
            } else if (mediaList.get(position).getExtension().contains("html")) {
                holder.recentImg.setImageResource(R.drawable.htmlimg);
            } else if (mediaList.get(position).getExtension().contains("zip")) {
                holder.recentImg.setImageResource(R.drawable.zipimg);
            } else if (mediaList.get(position).getExtension().contains("xls") || mediaList.get(position).getExtension().contains("xlsx")) {
                holder.recentImg.setImageResource(R.drawable.excelimg);
            } else if (mediaList.get(position).getExtension().contains("pptx") || mediaList.get(position).getExtension().contains("ppt")) {
                holder.recentImg.setImageResource(R.drawable.pptimg);
            } else if (mediaList.get(position).getExtension().contains("rar") || mediaList.get(position).getExtension().contains("RAR")) {
                holder.recentImg.setImageResource(R.drawable.araimg);
            }
        }

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


        holder.recentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("video")) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    //  alertForShowVideo(filePath);
                    alertForShowVideo(filePath, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }


    public Bitmap getThumblineImage(String videoPath) {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MINI_KIND);
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
            Bitmap finalbitmap = null;
            MediaMetadataRetriever mMRetriever = null;
            try {
                int timeInSeconds = 1;
                mMRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= 14)
                    mMRetriever.setDataSource(filePath, new HashMap<String, String>());
                else
                    mMRetriever.setDataSource(filePath);
                myBitmap = mMRetriever.getFrameAtTime(timeInSeconds * 1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                finalbitmap = ThumbnailUtils.extractThumbnail(myBitmap, 500, 500);
                // myBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth() / 2, myBitmap.getHeight() / 2, true);
                // myBitmap.compress(Bitmap.CompressFormat.PNG, 80, streamThumbnail);
                myBitmap.recycle(); //ensure the image is freed;
            } catch (Exception e) {
                e.printStackTrace();


            } finally {
                if (mMRetriever != null) {
                    mMRetriever.release();
                }
            }
            return finalbitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void alertForShowVideo(final String filePath, final int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        final View view = alert.getLayoutInflater().inflate(R.layout.video_view, null);
        Button close = view.findViewById(R.id.close);
        TextView title = view.findViewById(R.id.title);
        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(filePath));
        videoView.requestFocus();
        //  videoView.seekTo(300);
        //   videoView.pause();
        videoView.requestFocus();
        videoView.start();
        alert.setCustomTitle(view);

        title.setText(mediaList.get(position).getFileName());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.stopPlayback();
                    videoView.release();
                }
                alert.dismiss();
            }
        });
        alert.show();
    }
}
