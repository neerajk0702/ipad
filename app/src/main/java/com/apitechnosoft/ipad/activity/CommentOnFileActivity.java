package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.ShareRecivedCommentAdapter;
import com.apitechnosoft.ipad.adapter.SharedRecivedEmailAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.Commentdata;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Emaildata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class CommentOnFileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    ImageView img;
    WebView webView;
    RelativeLayout webLayout;
    VideoView videoView;
    LinearLayout videoViewLayout;
    EditText edt_comment;
    String commentStr;
    String UserId;
    String ItemSno, FilePath, FileName, ShareSno, Sno;
    ArrayList<Commentdata> commentList;
    String ShowfilePath;
    ProgressBar loadingDialog;
    RelativeLayout imgLayout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_on_file);
        if (ASTUtil.isTablet(CommentOnFileActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {

        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
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
        edt_comment = findViewById(R.id.edt_comment);
        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);
        loadingDialog = findViewById(R.id.loadingDialog);
        img = findViewById(R.id.img);
        imgLayout = findViewById(R.id.imgLayout);
        webView = findViewById(R.id.web);
        webLayout=findViewById(R.id.webLayout);
        videoViewLayout = findViewById(R.id.videoViewLayout);
        recyclerView = findViewById(R.id.emailrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentOnFileActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ItemSno = getIntent().getStringExtra("ItemSno");
        FilePath = getIntent().getStringExtra("FilePath");
        FileName = getIntent().getStringExtra("FileName");
        ShareSno = getIntent().getStringExtra("ShareSno");
        int MediaType = getIntent().getIntExtra("MediaType", 0);
        ShowfilePath = getIntent().getStringExtra("ShowfilePath");
        Sno = getIntent().getStringExtra("Sno");
        title.setText(FileName);

        if (MediaType == 1) {
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            imgLayout.setVisibility(View.VISIBLE);
            setImageShare();
        } else if (MediaType == 2) {
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webLayout.setVisibility(View.VISIBLE);
            imgLayout.setVisibility(View.GONE);
            setDocShare();
        } else if (MediaType == 3 || MediaType == 4) {
            videoViewLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            imgLayout.setVisibility(View.GONE);
            setVideoShare();
        }

        getAllSharedFileComments();
    }

    private void setImageShare() {
        loadingDialog.setVisibility(View.VISIBLE);
        Picasso.with(ApplicationHelper.application().getContext()).load(ShowfilePath).into(img, new Callback() {
            @Override
            public void onSuccess() {
                loadingDialog.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                loadingDialog.setVisibility(View.GONE);
            }
        });
    }

    private void setDocShare() {
        final ProgressBar webloadingDialog = findViewById(R.id.webloadingDialog);
        webloadingDialog.setVisibility(View.VISIBLE);
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
                webloadingDialog.setVisibility(View.GONE);
            }
        });
        if (ShowfilePath != null) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + ShowfilePath);
        }
    }

    private void setVideoShare() {
        final MediaController mediaController = new MediaController(CommentOnFileActivity.this);
        videoView = findViewById(R.id.videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(ShowfilePath));
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

    // ----validation -----
    private boolean isValidate() {

        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        commentStr = edt_comment.getText().toString();
        if (commentStr.length() == 0) {
            showToast("Please enter Comment");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(CommentOnFileActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void sendComment() {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(CommentOnFileActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.SaveCommentApi + "comment=" + commentStr + "&" + "itemsno=" + ItemSno + "&" + "path=" + FilePath + "&" + "filename=" + FileName + "&" + "sharesno=" + ShareSno + "&" + "username=" + UserId + "&" + "emailid=" + UserId;

            serviceCaller.CallCommanServiceMethod(url, "sendComment", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                showToast("Comment send Successfully");
                                edt_comment.setText("");
                                getAllSharedFileComments();
                            } else {
                                Toast.makeText(CommentOnFileActivity.this, "Comment not send Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CommentOnFileActivity.this, "Comment not send Successfully", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            showToast(Contants.OFFLINE_MESSAGE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogIn:
                if (isValidate()) {
                    sendComment();
                }
                break;
        }
    }

    //get all shared comments
    private void getAllSharedFileComments() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");

            if (ASTUIUtil.isOnline(CommentOnFileActivity.this)) {
                final ASTProgressBar dotDialog = new ASTProgressBar(CommentOnFileActivity.this);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(CommentOnFileActivity.this);
                final String url = Contants.BASE_URL + Contants.GetShareFileCommentandEmail + "username=" + UserId + "&" + "itemsno=" + ItemSno + "&" + "sharesno=" + ShareSno + "&" + "sno=" + Sno + "&" + "search_keyword=";
                serviceCaller.CallCommanServiceMethod(url, "getAllSharedFileComments", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        Log.d(Contants.LOG_TAG, "Get All File**" + result);
                        if (isComplete) {
                            ContentData data = new Gson().fromJson(result, ContentData.class);
                            if (data != null) {
                                if (data.getCommentdata() != null) {
                                    commentList = new ArrayList<Commentdata>(Arrays.asList(data.getCommentdata()));
                                    if (commentList != null && commentList.size() > 0) {
                                        ShareRecivedCommentAdapter recivedEmailAdapter = new ShareRecivedCommentAdapter(CommentOnFileActivity.this, commentList);
                                        recyclerView.setAdapter(recivedEmailAdapter);
                                    }
                                } else {
                                    Toast.makeText(CommentOnFileActivity.this, "Shared Email No Data found!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(CommentOnFileActivity.this, "Shared Email No Data found!", Toast.LENGTH_LONG).show();
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
}
