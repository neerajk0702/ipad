package com.apitechnosoft.ipad.activity;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

public class ShareImageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageView img;
    WebView webView;
    VideoView videoView;
    LinearLayout videoViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = toolbar.findViewById(R.id.title);
        img = findViewById(R.id.img);
        webView = findViewById(R.id.web);
        videoViewLayout = findViewById(R.id.videoViewLayout);
        String MediaData = getIntent().getStringExtra("MediaData");
        int MediaType = getIntent().getIntExtra("MediaType", 0);
        if (MediaData != null) {
            MediaData media = new Gson().fromJson(MediaData, new TypeToken<MediaData>() {
            }.getType());
            title.setText(media.getFileName());
            if (MediaType == 1) {
                videoViewLayout.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                setImageShare(media);
            } else if (MediaType == 2) {
                videoViewLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                setDocShare(media);
            } else if (MediaType == 3 || MediaType == 4) {
                videoViewLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                setVideoShare(media);
            }
        }

    }

    private void setImageShare(MediaData media) {
        String filePath = Contants.Media_File_BASE_URL + media.getFolderlocation() + "/" + media.getFileName();
        Picasso.with(ApplicationHelper.application().getContext()).load(filePath).into(img);
    }

    private void setDocShare(MediaData media) {
        String filePath = Contants.Media_File_BASE_URL + media.getFolderlocation() + "/" + media.getFileName();
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
    }

    private void setVideoShare(MediaData media) {
        String filePath = Contants.Media_File_BASE_URL + media.getFolderlocation() + "/" + media.getFileName();
        final MediaController mediaController = new MediaController(ShareImageActivity.this);
        videoView = findViewById(R.id.videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(filePath));
        //  videoView.setVideoPath(filePath);
        videoView.requestFocus();
        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
        ((FrameLayout) findViewById(R.id.videoViewWrapper)).addView(mediaController);
        mediaController.setVisibility(View.VISIBLE);
        final ProgressBar bufferingDialog = findViewById(R.id.bufferingDialog);
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
                        ((FrameLayout) findViewById(R.id.videoViewWrapper)).addView(mediaController);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
