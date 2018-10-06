package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.database.IpadDBHelper;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ShareSingleFileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    ImageView img;
    WebView webView;
    RelativeLayout webLayout;
    VideoView videoView;
    LinearLayout videoViewLayout;
    EditText edt_comment;
    AutoCompleteTextView edt_email;
    String emailStr, commentStr;
    MediaData media;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        if (ASTUtil.isTablet(ShareSingleFileActivity.this)) {
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
        edt_email = findViewById(R.id.edt_email);
        edt_comment = findViewById(R.id.edt_comment);
        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);
        TextView iicon = findViewById(R.id.iicon);
        iicon.setTypeface(materialdesignicons_font);
        iicon.setText(Html.fromHtml("&#xf2fd;"));

        img = findViewById(R.id.img);
        webView = findViewById(R.id.web);
        webLayout = findViewById(R.id.webLayout);
        videoViewLayout = findViewById(R.id.videoViewLayout);
        String MediaData = getIntent().getStringExtra("MediaData");
        int MediaType = getIntent().getIntExtra("MediaType", 0);
        if (MediaData != null) {
            media = new Gson().fromJson(MediaData, new TypeToken<MediaData>() {
            }.getType());
            title.setText(media.getFileName());
            if (MediaType == 1) {
                videoViewLayout.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                webLayout.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                setImageShare(media);
            } else if (MediaType == 2) {
                videoViewLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webLayout.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                setDocShare(media);
            } else if (MediaType == 3 || MediaType == 4) {
                videoViewLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                webLayout.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                setVideoShare(media);
            }
        }
        IpadDBHelper ipadDBHelper = new IpadDBHelper(ShareSingleFileActivity.this);
        ArrayList<Data> dataArrayList = ipadDBHelper.getAllShareEmailData();
        if (dataArrayList != null) {
            String[] arrSearchData = new String[dataArrayList.size()];
            for (int i = 0; i < dataArrayList.size(); i++) {
                arrSearchData[i] = dataArrayList.get(i).getEmailId();
            }
            ArrayAdapter<String> adapterSiteName = new ArrayAdapter<String>(ShareSingleFileActivity.this, android.R.layout.select_dialog_item, arrSearchData);
            edt_email.setAdapter(adapterSiteName);
            edt_email.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String searchString = edt_email.getText().toString();
                }
            });
        }
    }

    private void setImageShare(MediaData media) {
        String filePath = Contants.Media_File_BASE_URL + media.getFolderlocation() + "/" + media.getFileName();
        Picasso.with(ApplicationHelper.application().getContext()).load(filePath).placeholder(R.drawable.image_icon).into(img);
    }

    private void setDocShare(MediaData media) {
        final ProgressBar loadingDialog = findViewById(R.id.loadingDialog);
        loadingDialog.setVisibility(View.VISIBLE);
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
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loadingDialog.setVisibility(View.GONE);
            }
        });
        if (filePath != null) {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + filePath);
        }
    }

    private void setVideoShare(MediaData media) {
        String filePath = Contants.Media_File_BASE_URL + media.getFolderlocation() + "/" + media.getFileName();
        final MediaController mediaController = new MediaController(ShareSingleFileActivity.this);
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

    // ----validation -----
    private boolean isValidate() {

        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        emailStr = edt_email.getText().toString();
        commentStr = edt_comment.getText().toString();

        if (emailStr.length() == 0) {
            showToast("Please enter Email Id");
            return false;
        } else if (commentStr.length() == 0) {
            showToast("Please enter Comment");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(ShareSingleFileActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void shareFile() {
        if (ASTUIUtil.isOnline(this)) {
            if (media != null) {
                final ASTProgressBar dotDialog = new ASTProgressBar(ShareSingleFileActivity.this);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(this);
                JSONObject object = new JSONObject();
                try {
                    object.put("emailId", emailStr);
                    object.put("userName", UserId);
                    object.put("commentl", commentStr);
                    object.put("sharedfilename", media.getFileName());
                    object.put("itemSno", media.getSno());
                    object.put("path", media.getFilePath());
                    object.put("folderlocation", media.getFolderlocation());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String url = Contants.BASE_URL + Contants.ShareDataapiNew;
                /* final String url = Contants.BASE_URL + Contants.ShareDataapiNew + "emailId=" + emailStr + "&" + "userName=" + UserId + "&" + "commentl=" + commentStr + "&" + "sharedfilename=" + media.getFileName() + "&" + "itemSno=" + media.getSno() + "&" + "path=" + media.getFilePath() + "&" + "folderlocation=" + media.getFolderlocation();*/
                serviceCaller.CallCommanServiceMethod(url, object, "shareFile", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                            if (data != null) {
                                if (data.isStatus()) {
                                    showToast("File shared Successfully");
                                    finish();
                                } else {
                                    Toast.makeText(ShareSingleFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ShareSingleFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            showToast(Contants.Error);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            }
        } else {
            showToast(Contants.OFFLINE_MESSAGE);
        }
    }

    public void shareSingleFile() {
        if (ASTUIUtil.isOnline(ShareSingleFileActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(ShareSingleFileActivity.this);
            progressBar.show();
            final String serviceURL = Contants.BASE_URL + Contants.ShareDataapiNew;

            JSONObject object = new JSONObject();
            try {
                object.put("emailId", emailStr);
                object.put("userName", UserId);
                object.put("commentl", commentStr);
                object.put("sharedfilename", media.getFileName());
                object.put("itemSno", media.getSno());
                object.put("path", media.getFilePath());
                object.put("folderlocation", media.getFolderlocation());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("jsonData", object.toString());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(ShareSingleFileActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                IpadDBHelper ipadDBHelper = new IpadDBHelper(ShareSingleFileActivity.this);
                                String[] mailarray = emailStr.split(",");
                                if (mailarray != null) {
                                    for (int i = 0; i < mailarray.length; i++) {
                                        Data emaildata = new Data();
                                        emaildata.setEmailId(mailarray[i]);
                                        ipadDBHelper.upsertShareEmailData(emaildata);
                                    }
                                }
                                showToast("File shared Successfully");
                                finish();
                            } else {
                                Toast.makeText(ShareSingleFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ShareSingleFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ShareSingleFileActivity.this);//off line msg....
        }

    }

 /*   public void shareSingleFile() {
        if (ASTUIUtil.isOnline(ShareSingleFileActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(ShareSingleFileActivity.this);
            progressBar.show();
            final String serviceURL = Contants.BASE_URL + Contants.ShareDataapiNew;
            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("emailId", emailStr);
            payloadList.put("userName", UserId);
            payloadList.put("commentl", commentStr);
            payloadList.put("sharedfilename", media.getFileName().toString());
            payloadList.put("itemSno", String.valueOf(media.getSno()));
            payloadList.put("path", media.getFilePath());
            payloadList.put("folderlocation", media.getFolderlocation());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(ShareSingleFileActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                showToast("File shared Successfully");
                                finish();
                            } else {
                                Toast.makeText(ShareSingleFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ShareSingleFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ShareSingleFileActivity.this);//off line msg....
        }

    }*/


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogIn:
                if (isValidate()) {
                    //shareFile();
                    shareSingleFile();
                }
                break;
        }
    }
}
