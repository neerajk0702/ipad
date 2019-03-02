package com.apitechnosoft.ipad.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ChangeNameActivity;
import com.apitechnosoft.ipad.activity.ChnagePassword;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FNReqResCode;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.apitechnosoft.ipad.utils.ASTUIUtil.getColor;
import static com.apitechnosoft.ipad.utils.ASTUIUtil.showToast;

public class ProfileFragment extends MainFragment {

    CardView emailviewLayout;
    private ImageView profileImg;
    private TextView loginUserName, emailusername, userprofile, userMailAdd;


    private View changePasswordLayout;

    String email;
    private static File selectFile;
    String mimtype;
    TextView usertype, dataSize;
    HeaderFragment headerFragment;
    String emailStr;
    LinearLayout changeNameLayout;

    @Override
    protected int fragmentLayout() {
        return R.layout.profile;
    }

    @Override
    protected void loadView() {
        this.profileImg = this.findViewById(R.id.profileImg);
        this.loginUserName = this.findViewById(R.id.loginUserName);
        this.changePasswordLayout = this.findViewById(R.id.changePasswordLayout);
        this.emailviewLayout = this.findViewById(R.id.emailviewLayout);
        emailusername = this.findViewById(R.id.emailusername);
        usertype = this.findViewById(R.id.usertype);
        dataSize = this.findViewById(R.id.dataSize);
        changeNameLayout = this.findViewById(R.id.changeNameLayout);
    }

    @Override
    protected void setClickListeners() {
        this.profileImg.setOnClickListener(this);
        this.changePasswordLayout.setOnClickListener(this);
        this.changeNameLayout.setOnClickListener(this);
        profileImg.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {

    }

    protected void loadcartdata() {
        if (getHostActivity() == null) {
            return;
        }
        this.headerFragment = this.getHostActivity().headerFragment();
        if (headerFragment == null) {
            return;
        }
        this.headerFragment.setVisiVilityNotificationIcon(false);
    }

    @Override
    public void dataToView() {

        SharedPreferences prefs = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            emailStr = prefs.getString("UserId", "");
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profileImg) {
            ASTUIUtil.startImagePicker(getHostActivity());
        } else if (view.getId() == R.id.changePasswordLayout) {
            Intent intent = new Intent(getContext(), ChnagePassword.class);
            startActivity(intent);
        } else if (view.getId() == R.id.changeNameLayout) {
            Intent intent = new Intent(getContext(), ChangeNameActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        SharedPreferences prefs = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            email = prefs.getString("UserId", "");
        }
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            final String url = Contants.BASE_URL + Contants.UserInfoService + "email=" + email;
            serviceCaller.CallCommanServiceMethod(url, "getUserInfo", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.getUser() != null) {
                                SharedPreferences prefs = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("FirstName", data.getUser().getfName());
                                editor.putString("LastName", data.getUser().getlName());
                                editor.commit();
                                loginUserName.setText(data.getUser().getfName() + "\t" + data.getUser().getlName());
                                emailusername.setText(emailStr);
                                dataSize.setText(data.getUsedsize() + "\t" + getString(R.string.mbuseed) + data.getRemanningsiz() + "\tGB");
                                if (data.getUser().getType().equals("0")) {
                                    usertype.setText(getString(R.string.BRONZEUSER));
                                    usertype.setBackgroundColor(ASTUIUtil.getColor(R.color.browne));
                                } else if (data.getUser().getType().equals("1")) {
                                    usertype.setText(getString(R.string.SILVERUSER));
                                    usertype.setBackgroundColor(ASTUIUtil.getColor(R.color.silver3));
                                } else if (data.getUser().getType().equals("2")) {
                                    usertype.setText(getString(R.string.GOLDUSER));
                                    usertype.setBackgroundColor(ASTUIUtil.getColor(R.color.gold));
                                }

                                loadcartdata();
                                if (data.getUserprofile() != null) {
                                    String filePath = data.getUserprofile().getFilePath();
                                    if (filePath != null) {
                                        String newpath = filePath.replace("C:/xampp/tomcat/webapps/ROOT/", Contants.Media_File_BASE_URL);
                                        if (newpath != null) {
                                            Picasso.with(ApplicationHelper.application().getContext()).load(newpath).resize(65, 65).into(profileImg);
                                        }
                                    }
                                }


                            }

                        }
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


    public void getPickedFiles(ArrayList<MediaFile> files) {
        for (MediaFile deviceFile : files) {
            if (deviceFile.getFilePath() != null && deviceFile.getFilePath().exists()) {
                String imageName = deviceFile.getFileName();
                selectFile = deviceFile.getFilePath();
                // selectFile = ASTUIUtil.renameFile(deviceFile.getFileName(), imageName);
                Picasso.with(ApplicationHelper.application().getContext()).load(selectFile).into(profileImg);
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
                uploadProfilePic();
            }

        }
    }

    public void uploadProfilePic() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");

            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar progressBar = new ASTProgressBar(getContext());
                progressBar.show();
                String serviceURL = Contants.BASE_URL + Contants.UploadProfilePic;
                HashMap<String, String> payloadList = new HashMap<String, String>();
                payloadList.put("username", UserId);
                MultipartBody.Builder multipartBody = setMultipartBodyVaule();
                FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(getContext(), payloadList, multipartBody, serviceURL) {
                    @Override
                    public void receiveData(String result) {
                        ContentData data = new Gson().fromJson(result, ContentData.class);
                        if (data != null) {
                            if (data.isStatus() == true) {
                                ASTUIUtil.showToast(getString(R.string.fileuploadsuccesss));
                                reloadBackScreen();
                            } else {
                                ASTUIUtil.showToast(getString(R.string.filenotulod));
                            }
                        } else {
                            ASTUIUtil.showToast(getString(R.string.filenotulod));
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
