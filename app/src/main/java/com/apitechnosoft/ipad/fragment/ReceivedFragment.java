package com.apitechnosoft.ipad.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.activity.NotificationActivity;
import com.apitechnosoft.ipad.activity.ShareMultipelFileActivity;
import com.apitechnosoft.ipad.adapter.NotificationAdapter;
import com.apitechnosoft.ipad.adapter.PersonalAdapter;
import com.apitechnosoft.ipad.adapter.RecentFileAdapter;
import com.apitechnosoft.ipad.adapter.RecivedFileAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.Audioist;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.model.Documentlist;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.model.Notificationlist;
import com.apitechnosoft.ipad.model.Photolist;
import com.apitechnosoft.ipad.model.Videolist;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FZProgressBar;
import com.apitechnosoft.ipad.utils.FontManager;
import com.apitechnosoft.ipad.utils.NoSSLv3Factory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;


public class ReceivedFragment extends MainFragment {
    Typeface materialdesignicons_font;
    RecyclerView recyclerView, folderrecycler_view;
    ArrayList<MediaData> mediaList;
    TextView photolayout, videolayout, audiolayout, doclayout;
    Button selectfoldet, sharefile;
    FZProgressBar main_progressBar;
    TextView seeallfile;
    boolean seeallfileFlag = true;
    HeaderFragment headerFragment;
    EditText searchedit;
    RecivedFileAdapter mAdapter;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void loadView() {
        photolayout = findViewById(R.id.photolayout);
        videolayout = findViewById(R.id.videolayout);
        audiolayout = findViewById(R.id.audiolayout);
        doclayout = findViewById(R.id.doclayout);
        selectfoldet = findViewById(R.id.selectfoldet);
        sharefile = findViewById(R.id.sharefile);
        selectfoldet.setVisibility(View.GONE);


        TextView newFolder = findViewById(R.id.newFolder);
        TextView upFolder = findViewById(R.id.upFolder);
        searchedit = findViewById(R.id.searchedit);
        // TextView filter = findViewById(R.id.filter);
        //TextView filterIcon = findViewById(R.id.filterIcon);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");

        newFolder.setTypeface(materialdesignicons_font);
        newFolder.setText(Html.fromHtml("&#xf257;"));
        newFolder.setOnClickListener(this);
        upFolder.setTypeface(materialdesignicons_font);
        upFolder.setText(Html.fromHtml("&#xf259;"));
        // filterIcon.setTypeface(materialdesignicons_font);
        //  filterIcon.setText(Html.fromHtml("&#xf04a;"));
        TextView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setTypeface(materialdesignicons_font);
        searchIcon.setText(Html.fromHtml("&#xf349;"));

        // filterspinner = (Spinner) findViewById(R.id.filterspinner);
        recyclerView = findViewById(R.id.perrecycler_view);
        recyclerView.setHasFixedSize(false);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        folderrecycler_view = findViewById(R.id.folderrecycler_view);
        folderrecycler_view.setHasFixedSize(false);
        StaggeredGridLayoutManager foldergaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
        folderrecycler_view.setLayoutManager(foldergaggeredGridLayoutManager);
        folderrecycler_view.setVisibility(View.GONE);
        main_progressBar = (FZProgressBar) view.findViewById(R.id.card_progressBar);
        //Configure the first progress bar
        main_progressBar.animation_config(0, 10);
        int[] colors1 = {Color.parseColor("#FF4B05"), Color.parseColor("#00B1F0")};
        main_progressBar.bar_config(10, 0, 0, Color.TRANSPARENT, colors1);
        seeallfile = findViewById(R.id.seeallfile);
        getAllFile();

        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(editable.toString());
                }
            }
        });
    }


    protected void loadcartdata(String count) {
        if (getHostActivity() == null) {
            return;
        }
        this.headerFragment = this.getHostActivity().headerFragment();
        if (headerFragment == null) {
            return;
        }
        this.headerFragment.setVisiVilityNotificationIcon(true);
        this.headerFragment.updateNotification(count);
    }


    @Override
    protected void setClickListeners() {
        photolayout.setOnClickListener(this);
        videolayout.setOnClickListener(this);
        audiolayout.setOnClickListener(this);
        doclayout.setOnClickListener(this);
        seeallfile.setOnClickListener(this);
        sharefile.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
    }


    @Override
    protected void dataToView() {
       /* ArrayList<Data> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Data data = new Data();
            data.setTitle("Recent" + i);
            dataList.add(data);
        }*/


        final String filter_array[] = {"Newest", "Oldest"};
        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_row, filter_array);
       /* filterspinner.setAdapter(bankAdapter);
        filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  TextView textview = view.findViewById(R.id.cust_view);
                String str = filter_array[position];
                if (str.equals("Savings Account")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.newFolder:
                alertForFolderName();
                break;
            case R.id.photolayout:
                setAdapter(1);
                setPhotoButton();
                break;
            case R.id.videolayout:
                setAdapter(2);
                seeallfile.setVisibility(View.GONE);//show only photos
                photolayout.setBackgroundResource(R.drawable.border_layout_orange);
                videolayout.setBackgroundResource(R.drawable.border_full_orange);
                audiolayout.setBackgroundResource(R.drawable.border_layout_orange);
                doclayout.setBackgroundResource(R.drawable.border_layout_orange);

                photolayout.setTextColor(Color.parseColor("#FF4B05"));
                videolayout.setTextColor(Color.parseColor("#ffffff"));
                audiolayout.setTextColor(Color.parseColor("#FF4B05"));
                doclayout.setTextColor(Color.parseColor("#FF4B05"));
                break;
            case R.id.audiolayout:
                setAdapter(3);
                seeallfile.setVisibility(View.GONE);//show only photos
                photolayout.setBackgroundResource(R.drawable.border_layout_orange);
                videolayout.setBackgroundResource(R.drawable.border_layout_orange);
                audiolayout.setBackgroundResource(R.drawable.border_full_orange);
                doclayout.setBackgroundResource(R.drawable.border_layout_orange);

                photolayout.setTextColor(Color.parseColor("#FF4B05"));
                videolayout.setTextColor(Color.parseColor("#FF4B05"));
                audiolayout.setTextColor(Color.parseColor("#ffffff"));
                doclayout.setTextColor(Color.parseColor("#FF4B05"));
                break;
            case R.id.doclayout:
                setAdapter(4);
                seeallfile.setVisibility(View.GONE);//show only photos
                photolayout.setBackgroundResource(R.drawable.border_layout_orange);
                videolayout.setBackgroundResource(R.drawable.border_layout_orange);
                audiolayout.setBackgroundResource(R.drawable.border_layout_orange);
                doclayout.setBackgroundResource(R.drawable.border_full_orange);

                photolayout.setTextColor(Color.parseColor("#FF4B05"));
                videolayout.setTextColor(Color.parseColor("#FF4B05"));
                audiolayout.setTextColor(Color.parseColor("#FF4B05"));
                doclayout.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.seeallfile:
                seeallfileFlag = false;
                seeallfile.setVisibility(View.GONE);
                setAdapter(1);
                break;
            case R.id.sharefile:
                startActivity(new Intent(getContext(), ShareMultipelFileActivity.class));
                break;
        }
    }

    private void setPhotoButton() {
        photolayout.setBackgroundResource(R.drawable.border_full_orange);
        videolayout.setBackgroundResource(R.drawable.border_layout_orange);
        audiolayout.setBackgroundResource(R.drawable.border_layout_orange);
        doclayout.setBackgroundResource(R.drawable.border_layout_orange);

        photolayout.setTextColor(Color.parseColor("#ffffff"));
        videolayout.setTextColor(Color.parseColor("#FF4B05"));
        audiolayout.setTextColor(Color.parseColor("#FF4B05"));
        doclayout.setTextColor(Color.parseColor("#FF4B05"));
    }

    public void alertForFolderName() {
        final View myview = LayoutInflater.from(getContext()).inflate(R.layout.folder_create, null);
        final EditText edt_foldername = myview.findViewById(R.id.edt_foldername);
        Button btnLogIn = myview.findViewById(R.id.btnLogIn);
        Button btncancel = myview.findViewById(R.id.btncancel);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setIcon(R.drawable.audio_icon).setCancelable(false)
                .setView(myview).create();
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_foldername.getText().toString().length() == 0) {
                    ASTUIUtil.showToast("Please enter Folder Name!");
                } else {
                    alertDialog.dismiss();
                    createFolder(edt_foldername.getText().toString());
                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void createFolder(String folderName) {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");

            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(getContext());
                final String url = Contants.BASE_URL + Contants.CreateFolder + "username=" + UserId + "&" + "foldername=" + folderName;
                serviceCaller.CallCommanServiceMethod(url, "createFolder", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                            if (data != null) {
                                if (data.isStatus()) {
                                    Toast.makeText(getContext(), "Folder Create Successfully.", Toast.LENGTH_LONG).show();
                                    getAllFile();
                                } else {
                                    Toast.makeText(getContext(), "Folder not created!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Folder not created!", Toast.LENGTH_LONG).show();
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

    private void getAllFile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");

            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
                //dotDialog.show();
                main_progressBar.setVisibility(View.VISIBLE);
                main_progressBar.animation_start(FZProgressBar.Mode.INDETERMINATE);
                ServiceCaller serviceCaller = new ServiceCaller(getContext());
                final String url = Contants.BASE_URL + Contants.RecivedFileApi + "username=" + UserId + "&" + "order=" + "desc" + "&" + "search_keyword=" + "&" + "searchdate=";
                serviceCaller.CallCommanServiceMethod(url, "Recived File", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentData data = new Gson().fromJson(result, ContentData.class);
                            if (data != null) {
                                Log.d(Contants.LOG_TAG, "Get Recived All File**" + result);
                                showFileData(data);
                                getAllNotification();
                            } else {
                                Toast.makeText(getContext(), "No Data found!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            ASTUIUtil.showToast(Contants.Error);
                        }
                       /* if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }*/
                        main_progressBar.animation_stop();
                        main_progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
            }
        }
    }

    //show file data in list
    private void showFileData(ContentData data) {
        seeallfile.setVisibility(View.VISIBLE);
        setPhotoButton();
        mediaList = new ArrayList<>();
       /* Folderdata[] folderdata = data.getFolderdata();
        if (folderdata != null && folderdata.length > 0) {
            // FolderdataList = new ArrayList<Folderdata>(Arrays.asList(folderdata));
            for (Folderdata folder : folderdata) {
                MediaData mediaData = new MediaData();
                mediaData.setSno(folder.getSno());
                mediaData.setFileName(folder.getFileName());
                mediaData.setFilePath(folder.getFilePath());
                mediaData.setFullFilePath(folder.getFullFilePath());
                mediaList.add(mediaData);
            }
        }*/
        Photolist[] photolists = data.getPhotolist();
        if (photolists != null && photolists.length > 0) {
            //photoList = new ArrayList<Photolist>(Arrays.asList(photolists));
            for (Photolist photo : photolists) {
                MediaData mediaData = new MediaData();
                mediaData.setSno(photo.getSno());
                mediaData.setFileName(photo.getFileName());
                mediaData.setFilePath(photo.getFilePath());
                mediaData.setFileExtension(photo.getFileExtension());
                mediaData.setFolderName(photo.getFolderName());
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
                mediaData.setEmailId(photo.getEmailId());
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
                mediaData.setFolderName(video.getFolderName());
                mediaData.setFolderlocation(video.getFolderlocation());
                mediaData.setFileExtension(video.getFileExtension());
                mediaData.setEmailId(video.getEmailId());
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
                mediaData.setFolderName(audio.getFolderName());
                mediaData.setFileExtension(audio.getFileExtension());
                mediaData.setEmailId(audio.getEmailId());
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
                mediaData.setLimitFilename1(documentlist.getLimitFilename1());//
                mediaData.setSize(documentlist.getSize());
                mediaData.setType(documentlist.getType());//
                mediaData.setEnteredDate(documentlist.getEnteredDate());
                mediaData.setShareSno(documentlist.getShareSno());
                mediaData.setItemSno(documentlist.getItemSno());
                mediaData.setBytes(documentlist.getBytes());
                mediaData.setKiloByte(documentlist.getKiloByte());
                mediaData.setMegaByte(documentlist.getMegaByte());
                mediaData.setGigaByte(documentlist.getGigaByte());
                mediaData.setFolderlocation(documentlist.getFolderlocation());
                mediaData.setExtension(documentlist.getExtension());
                mediaData.setFolderName(documentlist.getFolderName());
                mediaData.setFileExtension(documentlist.getFileExtension());
                mediaData.setEmailId(documentlist.getEmailId());
                mediaList.add(mediaData);
            }
        }
        setAdapter(1);
    }

    private void setAdapter(int type) {
        ArrayList<MediaData> newmediaList = new ArrayList<>();
        //add folder
        if (mediaList != null && mediaList.size() > 0) {
            for (MediaData data : mediaList) {
                if (data.getFullFilePath() != null && !data.getFullFilePath().equals("")) {
                    newmediaList.add(data);
                }
            }
            if (type == 1) {
                for (MediaData data : mediaList) {
                    if (data.getFileExtension() != null && data.getFileExtension().contains("image")) {
                        newmediaList.add(data);
                    }
                }
            } else if (type == 2) {
                for (MediaData data : mediaList) {
                    if (data.getFileExtension() != null && data.getFileExtension().contains("video")) {
                        newmediaList.add(data);
                    }
                }
            } else if (type == 3) {
                for (MediaData data : mediaList) {
                    if (data.getFileExtension() != null && data.getFileExtension().contains("audio")) {
                        newmediaList.add(data);
                    }
                }
            } else if (type == 4) {
                for (MediaData data : mediaList) {
                    if (data.getFileExtension() != null && data.getFileExtension().contains("application")) {
                        newmediaList.add(data);
                    }
                }
            }
            recyclerView.removeAllViews();
            recyclerView.removeAllViewsInLayout();
            if (seeallfileFlag) {//show only 15 file
                ArrayList<MediaData> seemediaList = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    if (i < newmediaList.size()) {
                        seemediaList.add(newmediaList.get(i));
                    }
                }
                mAdapter = new RecivedFileAdapter(getContext(), seemediaList, type);//type for image video audio doc
            } else {
                mAdapter = new RecivedFileAdapter(getContext(), newmediaList, type);//type for image video audio doc
            }
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void getAllNotification() {
        try {
            String UserId = "";
            SharedPreferences prefs = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            if (prefs != null) {
                UserId = prefs.getString("UserId", "");
            }
            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
                // dotDialog.show();

                ServiceCaller serviceCaller = new ServiceCaller(getContext());
                final String url = Contants.BASE_URL + Contants.Getallnotification + "username=" + UserId;
                serviceCaller.CallCommanServiceMethod(url, "getAllNotification", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                            if (data != null) {
                                loadcartdata(data.getNotificationcount() + "");
                            }
                        }
                    }
                });
            }
        } catch (Exception E) {

        }
    }
}
