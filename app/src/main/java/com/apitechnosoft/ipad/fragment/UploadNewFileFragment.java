package com.apitechnosoft.ipad.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ChnagePassword;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.component.ASTTextView;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FNReqResCode;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;
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

    String mimtype;

    public void getPickedFiles(ArrayList<MediaFile> files) {
        for (MediaFile deviceFile : files) {
            if (deviceFile.getFilePath() != null && deviceFile.getFilePath().exists()) {
                String imageName = deviceFile.getFileName();
                selectFile = deviceFile.getFilePath();
                // selectFile = ASTUIUtil.renameFile(deviceFile.getFileName(), imageName);
                Picasso.with(ApplicationHelper.application().getContext()).load(selectFile).into(selectimg);
                filename.setText(deviceFile.getFileName());
                mimtype = deviceFile.getMimeType();
            }
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
                progressBar.show();
                String serviceURL = Contants.BASE_URL + Contants.UPLOAD_FILE;
                HashMap<String, String> payloadList = new HashMap<String, String>();
                payloadList.put("username", UserId);
                MultipartBody.Builder multipartBody = setMultipartBodyVaule();
                FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(getContext(), payloadList, multipartBody, serviceURL) {
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

}
