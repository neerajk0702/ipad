package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.PersonalAdapter;
import com.apitechnosoft.ipad.adapter.ShareAllFileAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.Audioist;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Documentlist;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.model.Photolist;
import com.apitechnosoft.ipad.model.Videolist;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MultipartBody;

public class ShareMultipelFileActivity extends AppCompatActivity implements View.OnClickListener {
    String UserId, FirstName, LastName;
    private Toolbar toolbar;
    EditText edt_email, edt_comment;
    String emailStr, commentStr;
    ArrayList<MediaData> mediaList;
    ShareAllFileAdapter mAdapter;
    RecyclerView filerecycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_multipel_file);
        if (ASTUtil.isTablet(ShareMultipelFileActivity.this)) {
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
        TextView title = toolbar.findViewById(R.id.title);
        Button sendfile = findViewById(R.id.sendfile);
        sendfile.setOnClickListener(this);
        Button close = findViewById(R.id.close);
        close.setOnClickListener(this);
        edt_email = findViewById(R.id.edt_email);
        edt_comment = findViewById(R.id.edt_comment);
        TextView iicon = findViewById(R.id.iicon);
        iicon.setTypeface(materialdesignicons_font);
        iicon.setText(Html.fromHtml("&#xf2fd;"));
        filerecycler_view = findViewById(R.id.filerecycler_view);
        filerecycler_view.setHasFixedSize(false);
        StaggeredGridLayoutManager foldergaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
        filerecycler_view.setLayoutManager(foldergaggeredGridLayoutManager);
        getAllFile();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.sendfile:
                if (isValidate()) {
                    ArrayList<MediaData> MediaList = mAdapter.mediaList;
                    if (MediaList != null && MediaList.size() > 0) {
                        ArrayList<MediaData> selectMediaList = new ArrayList<>();
                        for (MediaData mediaData : MediaList) {
                            if (mediaData.isSelected()) {
                                selectMediaList.add(mediaData);
                            }
                        }
                        String SEPARATOR = ">";
                        String SEPARATORComma = ",";
                        if (selectMediaList != null && selectMediaList.size() > 0) {
                          /*  StringBuilder fileDetailBuilder = new StringBuilder();
                            for (MediaData data : selectMediaList) {
                                fileDetailBuilder.append(data.getSno());
                                fileDetailBuilder.append(SEPARATOR);

                                fileDetailBuilder.append(data.getFileName());
                                fileDetailBuilder.append(SEPARATOR);

                                fileDetailBuilder.append(data.getFilePath());
                                fileDetailBuilder.append(SEPARATOR);

                                fileDetailBuilder.append(data.getFolderlocation());
                                fileDetailBuilder.append(SEPARATORComma);
                            }
                            String itemsno = fileDetailBuilder.toString();
                            itemsno = itemsno.substring(0, itemsno.length() - SEPARATORComma.length()); //Remove last comma*/
                            JSONObject object = new JSONObject();
                            try {
                                object.put("email", emailStr);
                                object.put("userName", UserId);

                                JSONArray fileArray = new JSONArray();
                                String cdate = ASTUtil.getCurrentDate();
                                for (MediaData data : selectMediaList) {
                                    JSONObject fileobject = new JSONObject();
                                    fileobject.put("fname", FirstName);
                                    fileobject.put("lname", LastName);
                                    fileobject.put("from", cdate);
                                    fileobject.put("message", commentStr);
                                    fileobject.put("itemSno", data.getSno());
                                    fileobject.put("filename", data.getFileName());
                                    fileobject.put("path", data.getFilePath());
                                    fileobject.put("shareSno", data.getShareSno());
                                    fileobject.put("folderlocation", data.getFolderlocation());
                                    fileArray.put(fileobject);
                                }
                                object.put("fileDetail", fileArray);
                                shareFile(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            ASTUIUtil.showToast("Please Select file!");
                        }

                    }
                }
                break;
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
        Toast.makeText(ShareMultipelFileActivity.this, message, Toast.LENGTH_LONG).show();
    }

  /*  private void shareFile(JSONObject object) {
        String cdate = ASTUtil.getCurrentDate();
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ShareMultipelFileActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            //final String url = "http://192.168.1.98:8080/IpadProject/ShareDataMultiFileApi/sharemultiData";
            final String url = Contants.BASE_URL + Contants.ShareDataMultiFileApi;
            *//* final String url = Contants.BASE_URL + Contants.ShareDataMultiFileApi + "u=" + "ff" + "&" + "emailid=" + emailStr + "&" + "username=" + UserId + "&" + "fname=" + FirstName + "&" + "lname=" + LastName + "&" + "from=" + cdate + "&" + "itemsno=" + itemsno + "&" + "message=" + commentStr;*//*
            serviceCaller.CallCommanServiceMethod(url, object, "shareAllFile", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                showToast("File shared Successfully");
                                finish();
                            } else {
                                Toast.makeText(ShareMultipelFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ShareMultipelFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
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
    }*/
    public void shareFile(JSONObject object) {
        if (ASTUIUtil.isOnline(ShareMultipelFileActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(ShareMultipelFileActivity.this);
            progressBar.show();
            final String serviceURL = Contants.BASE_URL + Contants.ShareDataMultiFileApi;
            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("userData", object.toString());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(ShareMultipelFileActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                showToast("File shared Successfully");
                                finish();
                            } else {
                                Toast.makeText(ShareMultipelFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ShareMultipelFileActivity.this, "File not shared Successfully!", Toast.LENGTH_LONG).show();
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
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ShareMultipelFileActivity.this);//off line msg....
        }

    }
    private void getAllFile() {
        if (ASTUIUtil.isOnline(ShareMultipelFileActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ShareMultipelFileActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(ShareMultipelFileActivity.this);
            final String url = Contants.BASE_URL + Contants.GetFileListApi + "username=" + UserId + "&" + "order=" + "" + "&" + "search_keyword=" + "&" + "searchdate=";
            serviceCaller.CallCommanServiceMethod(url, "GetFileListApi", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            Log.d(Contants.LOG_TAG, "Get All File**" + result);
                            showFileData(data);
                        } else {
                            Toast.makeText(ShareMultipelFileActivity.this, "No Data found!", Toast.LENGTH_LONG).show();
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

    //show file data in list
    private void showFileData(ContentData data) {
        mediaList = new ArrayList<>();
        Photolist[] photolists = data.getPhotolist();
        if (photolists != null && photolists.length > 0) {
            //photoList = new ArrayList<Photolist>(Arrays.asList(photolists));
            for (Photolist photo : photolists) {
                MediaData mediaData = new MediaData();
                mediaData.setSno(photo.getSno());
                mediaData.setFileName(photo.getFileName());
                mediaData.setFilePath(photo.getFilePath());
                mediaData.setLimitFilename(photo.getLimitFilename());
                mediaData.setLimitFilename1(photo.getLimitFilename1());
                mediaData.setSize(photo.getSize());
                mediaData.setType(photo.getType());
                mediaData.setEnteredDate(photo.getEnteredDate());
                mediaData.setShareSno(photo.getShareSno());
                mediaData.setItemSno(photo.getItemSno());
                mediaData.setBytes(photo.getBytes());
                mediaData.setKiloByte(photo.getKiloByte());
                mediaData.setMegaByte(photo.getMegaByte());
                mediaData.setGigaByte(photo.getGigaByte());
                mediaData.setFolderlocation(photo.getFolderlocation());
                mediaList.add(mediaData);
            }
        }
        Videolist[] videos = data.getVideolist();
        if (videos != null && videos.length > 0) {
            //videolist = new ArrayList<Videolist>(Arrays.asList(videos));
            for (Videolist video : videos) {
                MediaData mediaData = new MediaData();
                mediaData.setSno(video.getSno());
                mediaData.setFileName(video.getFileName());
                mediaData.setFilePath(video.getFilePath());
                mediaData.setLimitFilename(video.getLimitFilename());
                mediaData.setLimitFilename1(video.getLimitFilename1());
                mediaData.setSize(video.getSize());
                mediaData.setType(video.getType());
                mediaData.setEnteredDate(video.getEnteredDate());
                mediaData.setShareSno(video.getShareSno());
                mediaData.setItemSno(video.getItemSno());
                mediaData.setBytes(video.getBytes());
                mediaData.setKiloByte(video.getKiloByte());
                mediaData.setMegaByte(video.getMegaByte());
                mediaData.setGigaByte(video.getGigaByte());
                mediaData.setFolderlocation(video.getFolderlocation());
                mediaList.add(mediaData);
            }
        }
        Audioist[] audioists = data.getAudioist();
        if (audioists != null && audioists.length > 0) {
            // audioList = new ArrayList<Audioist>(Arrays.asList(audioists));
            for (Audioist audio : audioists) {
                MediaData mediaData = new MediaData();
                mediaData.setSno(audio.getSno());
                mediaData.setFileName(audio.getFileName());
                mediaData.setFilePath(audio.getFilePath());
                mediaData.setLimitFilename(audio.getLimitFilename());
                mediaData.setLimitFilename1(audio.getLimitFilename1());
                mediaData.setSize(audio.getSize());
                mediaData.setType(audio.getType());
                mediaData.setEnteredDate(audio.getEnteredDate());
                mediaData.setShareSno(audio.getShareSno());
                mediaData.setItemSno(audio.getItemSno());
                mediaData.setBytes(audio.getBytes());
                mediaData.setKiloByte(audio.getKiloByte());
                mediaData.setMegaByte(audio.getMegaByte());
                mediaData.setGigaByte(audio.getGigaByte());
                mediaData.setFolderlocation(audio.getFolderlocation());
                mediaList.add(mediaData);
            }
        }
        Documentlist[] documentlists = data.getDocumentlist();
        if (documentlists != null && documentlists.length > 0) {
            for (Documentlist documentlist : documentlists) {
                MediaData mediaData = new MediaData();
                mediaData.setSno(documentlist.getSno());
                mediaData.setFileName(documentlist.getFileName());
                mediaData.setFilePath(documentlist.getFilePath());
                mediaData.setLimitFilename(documentlist.getLimitFilename());
                mediaData.setLimitFilename1(documentlist.getLimitFilename1());
                mediaData.setSize(documentlist.getSize());
                mediaData.setType(documentlist.getType());
                mediaData.setEnteredDate(documentlist.getEnteredDate());
                mediaData.setShareSno(documentlist.getShareSno());
                mediaData.setItemSno(documentlist.getItemSno());
                mediaData.setBytes(documentlist.getBytes());
                mediaData.setKiloByte(documentlist.getKiloByte());
                mediaData.setMegaByte(documentlist.getMegaByte());
                mediaData.setGigaByte(documentlist.getGigaByte());
                mediaData.setFolderlocation(documentlist.getFolderlocation());
                mediaData.setExtension(documentlist.getExtension());
                mediaList.add(mediaData);
            }
        }
        mAdapter = new ShareAllFileAdapter(ShareMultipelFileActivity.this, mediaList);
        filerecycler_view.setAdapter(mAdapter);
    }

    //for hid keyboard when tab outside edittext box
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
