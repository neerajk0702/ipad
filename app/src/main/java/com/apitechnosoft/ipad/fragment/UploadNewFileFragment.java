package com.apitechnosoft.ipad.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ChnagePassword;
import com.apitechnosoft.ipad.activity.ShareSingleFileActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.component.ASTTextView;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.framework.FileUploaderHelperWithProgress;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FNReqResCode;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UploadNewFileFragment extends MainFragment {

    static ImageView selectimg;
    private static File selectFile;
    Button btnLogIn;
    ASTTextView filename;

    ImageView img;
    WebView webView;
    RelativeLayout webLayout;
    VideoView videoView;
    LinearLayout videoViewLayout;
    String mimtype;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_upload_new_file;
    }

    @Override
    protected void loadView() {
        if (ASTUtil.isTablet(getContext())) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        selectimg = this.findViewById(R.id.recentImg);
        btnLogIn = this.findViewById(R.id.btnLogIn);
        filename = this.findViewById(R.id.filename);

        img = findViewById(R.id.img);
        webView = findViewById(R.id.web);
        webLayout = findViewById(R.id.webLayout);
        videoViewLayout = findViewById(R.id.videoViewLayout);
    }

    @Override
    protected void setClickListeners() {
        selectimg.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.recentImg) {
            // ASTUIUtil.startImagePicker(getHostActivity());
            ASTUtil.startFilePicker(getHostActivity(), 60, FNFilePicker.SIZE_LIMIT - attachmentSize());
        } else if (view.getId() == R.id.btnLogIn) {
            if (isVlaidate()) {
                uploadData();
            }
        }
    }

    public boolean isVlaidate() {
        if (selectFile == null || !selectFile.exists()) {
            ASTUIUtil.shownewErrorIndicator(getContext(), "Please Select File");
            return false;
        }
        return true;
    }


    private long attachmentSize() {
        long ttlSize = 5000;

        return ttlSize;
    }


    public void getPickedFiles(ArrayList<MediaFile> files) {
        if (videoView != null) {
            videoView.stopPlayback();
        }

        for (MediaFile deviceFile : files) {
            if (deviceFile.getFilePath() != null && deviceFile.getFilePath().exists()) {
                String imageName = deviceFile.getFileName();
                selectFile = deviceFile.getFilePath();
                // selectFile = ASTUIUtil.renameFile(deviceFile.getFileName(), imageName);
                //  Picasso.with(ApplicationHelper.application().getContext()).load(selectFile).into(selectimg);
                filename.setText(deviceFile.getFileName());
                mimtype = deviceFile.getMimeType();
            }
        }
       /* if (!mimtype.contains("image")) {
            ASTUIUtil.alertForErrorMessage("This file type is not supported For Preview yet!! Please Press Upload Button.", getContext());//off line msg....
        }*/
        if (mimtype.contains("image")) {
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            setImageShare();
        } else if (mimtype.equalsIgnoreCase("application/msword") ||
                mimtype.equalsIgnoreCase("application/pdf") ||
                mimtype.equalsIgnoreCase("text/plain") ||
                mimtype.equalsIgnoreCase("application/vnd.ms-excel") ||
                mimtype.equalsIgnoreCase("application/mspowerpoint") || mimtype.equalsIgnoreCase("text/html")) {
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webLayout.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            setDocShare();
        } else if (mimtype.contains("video") || mimtype.contains("VIDEO") || mimtype.contains("audio") || mimtype.contains("AUDIO")) {
            videoViewLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            setVideoShare();
        } else {
            ASTUIUtil.alertForErrorMessage("This file type is not supported For Preview yet!! Please Press Upload Button.", getContext());//off line msg....
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
        }

    }

    public void getResult(ArrayList<MediaFile> files) {
        getPickedFiles(files);
    }


    /**
     * THIS USE an ActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void updateOnResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FNReqResCode.ATTACHMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FNFilePicker.EXTRA_SELECTED_MEDIA);
            if (files != null && files.size() > 0) {
                getResult(files);
            }

        }
    }


    public void uploadData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");


            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar progressBar = new ASTProgressBar(getContext());
                //   progressBar.show();
                String serviceURL = Contants.BASE_URL + Contants.UPLOAD_FILE;
                HashMap<String, String> payloadList = new HashMap<String, String>();
                payloadList.put("username", UserId);
                MultipartBody.Builder multipartBody = setMultipartBodyVaule();
                FileUploaderHelperWithProgress fileUploaderHelper = new FileUploaderHelperWithProgress(getContext(), payloadList, multipartBody, serviceURL) {
                    @Override
                    public void receiveData(String result) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            if (data.isStatus() == true) {
                                ASTUIUtil.showToast("File Upload Successfully");
                                reloadBackScreen();
                            } else {
                                ASTUIUtil.showToast("File Not Uploaded!");
                            }
                        } else {
                            ASTUIUtil.showToast("File Not Uploaded!");
                        }
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                };
                fileUploaderHelper.execute();
            } else {
                ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
            }
        }
    }


    //add pm install images into MultipartBody for send as multipart
    private MultipartBody.Builder setMultipartBodyVaule() {
        final MediaType MEDIA_TYPE = MediaType.parse(mimtype);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (selectFile.exists()) {
            multipartBody.addFormDataPart("file", selectFile.getName(), RequestBody.create(MEDIA_TYPE, selectFile));
        }
        return multipartBody;
    }


    private void setImageShare() {
        Picasso.with(ApplicationHelper.application().getContext()).load(selectFile).into(img);
    }

    private void setDocShare() {
        final ProgressBar loadingDialog = findViewById(R.id.loadingDialog);
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
        //webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loadingDialog.setVisibility(View.GONE);
            }
        });
        if (selectFile != null) {
            // webView.loadUrl(selectFile.toString());
            //     webView.loadDataWithBaseURL("", selectFile.toString(), "text/html", "UTF-8", "");
            //  webView.getSettings().setPluginsEnabled(true);
            File file = new File(selectFile.getPath());
            final Uri uri = Uri.fromFile(file);
            webView.loadUrl(uri.toString());
        }
    }


    private void setVideoShare() {
        final MediaController mediaController = new MediaController(getContext());
        videoView = findViewById(R.id.videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(selectFile.toString()));
        //  videoView.setVideoPath(filePath);
        videoView.requestFocus();
        videoView.setBackgroundColor(Color.parseColor("#D9D9D9")); // Your color.
        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
        ((FrameLayout) findViewById(R.id.videoViewWrapper)).addView(mediaController);
        mediaController.setVisibility(View.VISIBLE);
        final ProgressBar bufferingDialog = findViewById(R.id.bufferingDialog);
        final ImageView audiodefault = view.findViewById(R.id.audiodefault);
        if (mimtype.contains("video") || mimtype.contains("VIDEO")) {
            bufferingDialog.setVisibility(View.VISIBLE);
            audiodefault.setVisibility(View.GONE);
        } else if (mimtype.contains("audio") || mimtype.contains("AUDIO")) {
            bufferingDialog.setVisibility(View.GONE);
            audiodefault.setVisibility(View.VISIBLE);
        }


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setBackgroundColor(Color.TRANSPARENT);
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
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
