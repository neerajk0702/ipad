package com.apitechnosoft.ipad.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.WindowManager;
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
import com.apitechnosoft.ipad.activity.CustomTextureVideoView;
import com.apitechnosoft.ipad.activity.ShareSingleFileActivity;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.DownloadService;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.apitechnosoft.ipad.utils.UIUtils;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static java.security.AccessController.getContext;

public class ShareAllFileAdapter extends RecyclerView.Adapter<ShareAllFileAdapter.MyViewHolder> {

    public ArrayList<MediaData> mediaList;
    Context mContext;
    int type;
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
        CheckBox selectCheck;
        RelativeLayout videoViewLayout;
        // VideoView videoView;
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
            selectCheck = view.findViewById(R.id.selectCheck);
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
            String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
            holder.videoUrl = filePath;
            holder.imageLoaderProgressBar.setVisibility(View.INVISIBLE);//INVISIBLE
            holder.videoImageView.setVisibility(View.VISIBLE);//VISIBLE
            holder.fullView.setVisibility(View.VISIBLE);


            holder.recentImg.setImageResource(R.drawable.video);
            holder.recentImg.setVisibility(View.GONE);
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
        } else {
            holder.videoImageView.setVisibility(View.GONE);
            holder.videoPlayImageButton.setVisibility(View.GONE);
            holder.fullView.setVisibility(View.GONE);
        }
        if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("audio")) {
            holder.recentImg.setImageResource(R.drawable.audio_icon);
            holder.videoImageView.setVisibility(View.GONE);
            holder.videoPlayImageButton.setVisibility(View.GONE);
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
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("audio")) {
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
                    // alertForShowVideo(filePath, position);
                }
            }
        });
        holder.fullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaList.get(position).getType() != null && mediaList.get(position).getType().contains("video")) {
                    holder.stopVideo();
                    holder.videoViewnew.stopPlayback();
                    String filePath = Contants.Media_File_BASE_URL + mediaList.get(position).getFolderlocation() + "/" + mediaList.get(position).getFileName();
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
        alert.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        final View view = alert.getLayoutInflater().inflate(R.layout.video_view, null);
        Button close = view.findViewById(R.id.close);
        TextView title = view.findViewById(R.id.title);
        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVisibility(View.GONE);

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

        final MediaController controller = new MediaController(mContext);
        video_feed_item_video.setMediaController(controller);
        controller.setMediaPlayer(video_feed_item_video);
        controller.setAnchorView(video_feed_item_video);
        ((ViewGroup) controller.getParent()).removeView(controller);
        ((FrameLayout) view.findViewById(R.id.videoViewWrapper)).addView(controller);

        video_feed_item_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                Log.v("Video", "onPrepared" + video_feed_item_video.getVideoPath());
                int width = mp.getVideoWidth();
                int height = mp.getVideoHeight();
                loader_progress_bar.setVisibility(View.INVISIBLE);
                video_feed_item_video.setIsPrepared(true);
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                loader_progress_bar.setVisibility(View.INVISIBLE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                loader_progress_bar.setVisibility(View.VISIBLE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                loader_progress_bar.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        }
                        return false;
                    }
                });
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
        title.setText(mediaList.get(position).getFileName());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video_feed_item_video.isPlaying()) {
                    video_feed_item_video.stopPlayback();
                    //  videoView.release();
                }
                alert.dismiss();
            }
        });
        alert.show();
    }
}
