package com.apitechnosoft.ipad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ChnagePassword;
import com.apitechnosoft.ipad.adapter.HomePagerAdapter;
import com.apitechnosoft.ipad.adapter.RecentFileAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.Audioist;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.model.Folderdata;
import com.apitechnosoft.ipad.model.MediaData;
import com.apitechnosoft.ipad.model.Photolist;
import com.apitechnosoft.ipad.model.Resentdata;
import com.apitechnosoft.ipad.model.Videolist;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.apitechnosoft.ipad.ApplicationHelper.application;


/**
 * Created by Narayan .
 */
public class HomeFragment extends MainFragment implements View.OnClickListener {
    Typeface materialdesignicons_font;
    RecyclerView recent_recycler_view;
    LinearLayout uploadLayout;
    private TabLayout tabLayout;
    ArrayList<Resentdata> mediaList;
    ViewPager viewPager;
    HomePagerAdapter adapter;
    boolean isTab;

    @Override
    protected int fragmentLayout() {
        return R.layout.home_fragment_layout;
    }

    @Override
    protected void loadView() {
        if (ASTUtil.isTablet(getContext())) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTab = true;
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isTab = false;
        }

        uploadLayout = findViewById(R.id.uploadLayout);
        TextView uploadIcon = findViewById(R.id.uploadIcon);

        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");
        uploadIcon.setTypeface(materialdesignicons_font);
        uploadIcon.setText(Html.fromHtml("&#xf167;"));
        recent_recycler_view = findViewById(R.id.recent_recycler_view);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Shared"));
        tabLayout.addTab(tabLayout.newTab().setText("Received"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        getRecentFile();


    }

    @Override
    protected void setClickListeners() {
        uploadLayout.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {
        adapter = new HomePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setLinearLayoutManager(recent_recycler_view);
        recent_recycler_view.setNestedScrollingEnabled(false);
        recent_recycler_view.setHasFixedSize(false);


    }

    private void setLinearLayoutManager(RecyclerView recyclerView) {
        if (isTab) {
            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        } else {
            RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(LayoutManager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.uploadLayout:
                openUploadScreen();
                break;
        }
    }

    protected void openUploadScreen() {
        Bundle bundle = new Bundle();
        bundle.putString("headerTxt", "Home");
        bundle.putInt("MENU_ID", 0);
        getHostActivity().updateFragment(new UploadNewFileFragment(), bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        dataToView();
    }

    private void getRecentFile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String UserId = prefs.getString("UserId", "");
            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
                // dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(getContext());
                final String url = Contants.BASE_URL + Contants.RecentFileApi + "username=" + UserId + "&" + "order=" + "desc" + "&" + "search_keyword=" + "&" + "searchdate=";
                serviceCaller.CallCommanServiceMethod(url, "RecentFile Api", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentData data = new Gson().fromJson(result, ContentData.class);
                            if (data != null) {
                                showFileData(data);
                            } else {
                                Toast.makeText(getContext(), "No Data found!", Toast.LENGTH_LONG).show();
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

    //show file data in list
    private void showFileData(ContentData data) {
        mediaList = new ArrayList<>();
        Resentdata[] resentdata = data.getResentdata();
        if (resentdata != null && resentdata.length > 0) {
            for (Resentdata resentdata1 : resentdata) {
                Resentdata resentdata2 = new Resentdata();
                resentdata2.setSno(resentdata1.getSno());
                resentdata2.setFileName(resentdata1.getFileName());
                resentdata2.setFilePath(resentdata1.getFilePath());
                resentdata2.setLimitFilename(resentdata1.getLimitFilename());
                resentdata2.setLimitFilename1(resentdata1.getLimitFilename1());
                resentdata2.setSize(resentdata1.getSize());
                resentdata2.setType(resentdata1.getType());
                resentdata2.setEnteredDate(resentdata1.getEnteredDate());
                resentdata2.setShareSno(resentdata1.getShareSno());
                resentdata2.setItemSno(resentdata1.getItemSno());
                resentdata2.setBytes(resentdata1.getBytes());
                resentdata2.setKiloByte(resentdata1.getKiloByte());
                resentdata2.setMegaByte(resentdata1.getMegaByte());
                resentdata2.setGigaByte(resentdata1.getGigaByte());
                resentdata2.setFolderlocation(resentdata1.getFolderlocation());
                resentdata2.setExtension(resentdata1.getExtension());
                mediaList.add(resentdata2);

            }
        }
        if (mediaList != null && mediaList.size() > 0) {
            //Collections.reverse(mediaList);
            RecentFileAdapter mAdapter = new RecentFileAdapter(getContext(), mediaList);
            recent_recycler_view.setAdapter(mAdapter);
        }
    }


}