package com.apitechnosoft.ipad.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.framework.FileUploaderHelperWithProgress;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.model.Lastuploadpic;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.utils.ASTFileUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FileUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadFromOutsideActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    ImageView img;
    WebView webView;
    RelativeLayout webLayout;
    VideoView videoView;
    LinearLayout videoViewLayout;
    EditText edt_comment;
    AutoCompleteTextView edt_email;
    String emailStr, commentStr;
    String UserId;
    File selectFile;
    String mimtype;
    TextView title;
    private String fileName;
    private String filePath;
    private String limitFilename;
    private int sno;
    private int size;
    private String enteredDate;
    private int shareSno;
    private int itemSno;
    private int bytes;
    private String extension;
    private String folderlocation;
    ASTProgressBar shareProgressBar;
    String FirstName, LastName;
    ImageView audiodefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_from_outside);
        if (ASTUtil.isTablet(UploadFromOutsideActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        getData();

    }

    private void init() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
            FirstName = prefs.getString("FirstName", "");
            LastName = prefs.getString("LastName", "");
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
        title = toolbar.findViewById(R.id.title);
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
        audiodefault = findViewById(R.id.audiodefault);

    }

    private void setType(File selectFile, Uri imageUri) {
        if (mimtype.contains("image")) {
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            setImageShare(selectFile);
        } else if (mimtype.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                mimtype.equalsIgnoreCase("application/pdf") ||
                mimtype.equalsIgnoreCase("text/plain") ||
                mimtype.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                mimtype.equalsIgnoreCase("application/vnd.ms-powerpoint")) {
            videoViewLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webLayout.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            // setDocShare(selectFile, imageUri);
        } else if (mimtype.contains("video") || mimtype.contains("VIDEO") || mimtype.contains("audio") || mimtype.contains("AUDIO")) {
            videoViewLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            webLayout.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            setVideoShare(selectFile, imageUri);
        }
        IpadDBHelper ipadDBHelper = new IpadDBHelper(UploadFromOutsideActivity.this);
        ArrayList<Data> dataArrayList = ipadDBHelper.getAllShareEmailData();
        if (dataArrayList != null) {
            String[] arrSearchData = new String[dataArrayList.size()];
            for (int i = 0; i < dataArrayList.size(); i++) {
                arrSearchData[i] = dataArrayList.get(i).getEmailId();
            }
            ArrayAdapter<String> adapterSiteName = new ArrayAdapter<String>(UploadFromOutsideActivity.this, android.R.layout.select_dialog_item, arrSearchData);
            edt_email.setAdapter(adapterSiteName);
            edt_email.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String searchString = edt_email.getText().toString();
                }
            });
        }

    }

    private void getData() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendImage(intent, type);


        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
           /* if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }*/
        } else {
            // Handle other intents, such as being started from the home screen
        }

    }


    void handleSendImage(Intent intent, String type) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (imageUri != null) {
            try {
                // File filesc = new File(imageUri.getPath());

                File file = FileUtil.from(UploadFromOutsideActivity.this, imageUri);
                Log.d("file", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
                selectFile = file;
                mimtype = type;
                title.setText(selectFile.getName());
                setType(selectFile, imageUri);
                if (file != null && file.exists()) {
                    uploadData();
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public void uploadData() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");
            if (ASTUIUtil.isOnline(this)) {
                final ASTProgressBar progressBar = new ASTProgressBar(this);
                //progressBar.show();
                String serviceURL = Contants.BASE_URL + Contants.UPLOAD_OURSITEFILE;
                HashMap<String, String> payloadList = new HashMap<String, String>();
                payloadList.put("username", UserId);
                MultipartBody.Builder multipartBody = setMultipartBodyVaule();
                FileUploaderHelperWithProgress fileUploaderHelper = new FileUploaderHelperWithProgress(this, payloadList, multipartBody, serviceURL) {
                    @Override
                    public void receiveData(String result) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            if (data.isStatus() == true) {
                                for (Lastuploadpic lastuploadpic : data.getLastuploadpic()) {
                                    sno = lastuploadpic.getSno();
                                    fileName = lastuploadpic.getFileName();
                                    filePath = lastuploadpic.getFilePath();
                                    limitFilename = lastuploadpic.getLimitFilename();
                                    sno = lastuploadpic.getSno();
                                    size = lastuploadpic.getSize();
                                    //type = lastuploadpic.getType();
                                    enteredDate = lastuploadpic.getEnteredDate();
                                    shareSno = lastuploadpic.getShareSno();
                                    itemSno = lastuploadpic.getItemSno();
                                    bytes = lastuploadpic.getBytes();
                                    extension = lastuploadpic.getExtension();
                                    folderlocation = lastuploadpic.getFolderlocation();
                                    if (mimtype.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                                            mimtype.equalsIgnoreCase("application/pdf") ||
                                            mimtype.equalsIgnoreCase("text/plain") ||
                                            mimtype.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                                            mimtype.equalsIgnoreCase("application/vnd.ms-powerpoint")) {
                                        setDocShare();
                                    }
                                }

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
                ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
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


    private void setImageShare(File selectFile) {
        Picasso.with(ApplicationHelper.application().getContext()).load(selectFile).placeholder(R.drawable.image_icon).into(img);
    }

    private void setDocShare() {
        final ProgressBar loadingDialog = findViewById(R.id.loadingDialog);
        loadingDialog.setVisibility(View.VISIBLE);
        //  String filePath = Contants.Media_File_BASE_URL + media.getFolderlocation() + "/" + media.getFileName();
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
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loadingDialog.setVisibility(View.GONE);
            }
        });
        String filePath = Contants.Media_File_BASE_URL + folderlocation + "/" + fileName;
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + filePath);
        //webView.loadUrl(selectFile.getAbsolutePath());
        // webView.loadData(selectFile.getAbsolutePath(), mimtype, "utf8");
        //webView.loadData(selectFile.getAbsolutePath(), "pdf", "utf8");
    }

    private void setVideoShare(File selectFile, Uri imageUri) {
        final MediaController mediaController = new MediaController(UploadFromOutsideActivity.this);
        videoView = findViewById(R.id.videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(imageUri);
        //  videoView.setVideoPath(filePath);
        videoView.requestFocus();
        ((ViewGroup) mediaController.getParent()).removeView(mediaController);
        ((FrameLayout) findViewById(R.id.videoViewWrapper)).addView(mediaController);
        mediaController.setVisibility(View.VISIBLE);
        final ProgressBar bufferingDialog = findViewById(R.id.bufferingDialog);

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
        Toast.makeText(UploadFromOutsideActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void shareSingleFile() {
        if (ASTUIUtil.isOnline(UploadFromOutsideActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(UploadFromOutsideActivity.this);
            progressBar.show();
            final String serviceURL = Contants.BASE_URL + Contants.ShareDataapiNew;

            JSONObject object = new JSONObject();
            try {
                object.put("emailId", emailStr);
                object.put("userName", UserId);
                object.put("commentl", commentStr);
                object.put("fName", FirstName);
                object.put("lName", LastName);
                object.put("sharedfilename", fileName);
                object.put("itemSno", sno);
                object.put("path", filePath);
                object.put("folderlocation", folderlocation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("jsonData", object.toString());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(UploadFromOutsideActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                IpadDBHelper ipadDBHelper = new IpadDBHelper(UploadFromOutsideActivity.this);
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
                                Toast.makeText(UploadFromOutsideActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(UploadFromOutsideActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
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
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, UploadFromOutsideActivity.this);//off line msg....
        }

    }

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
