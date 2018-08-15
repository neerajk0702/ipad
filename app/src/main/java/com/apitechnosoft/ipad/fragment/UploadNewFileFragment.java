package com.apitechnosoft.ipad.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTImageView;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Constant;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FNReqResCode;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UploadNewFileFragment extends MainFragment {

    static ImageView selectimg;
    private static File selectFile;
    Button btnLogIn;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_upload_new_file;
    }

    @Override
    protected void loadView() {
        selectimg = this.findViewById(R.id.recentImg);
        btnLogIn = this.findViewById(R.id.btnLogIn);
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
            if (isVlaidate())
                uploadData();
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
        for (MediaFile deviceFile : files) {
            if (deviceFile.getFilePath() != null && deviceFile.getFilePath().exists()) {
                String imageName = deviceFile.getFileName();
                selectFile = ASTUIUtil.renameFile(deviceFile.getFileName(), imageName);
                Picasso.with(ApplicationHelper.application().getContext()).load(selectFile).into(selectimg);

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
            getResult(files);

        }
    }


    public void uploadData() {
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar progressBar = new ASTProgressBar(getContext());
            progressBar.show();
            String serviceURL = Constant.BASE_URL + Constant.UPLOAD_FILE;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", "89neerajsingh@gmail.com");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("file", jsonObject.toString());
            MultipartBody.Builder multipartBody = setMultipartBodyVaule();
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(getContext(), payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    ContentData data = new Gson().fromJson(result, ContentData.class);
                    if (data != null) {
                        if (data.isStatus() == true) {
                            ASTUIUtil.showToast("Image SuceesFuly Load");
                            reloadBackScreen();
                        } else {
                            ASTUIUtil.alertForErrorMessage(Contants.Error, getContext());
                        }
                    } else {
                        ASTUIUtil.showToast("Not Upload Image");
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


    //add pm install images into MultipartBody for send as multipart
    private MultipartBody.Builder setMultipartBodyVaule() {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (selectFile.exists()) {
            multipartBody.addFormDataPart(selectFile.getName(), selectFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, selectFile));
        }
        return multipartBody;
    }

}
