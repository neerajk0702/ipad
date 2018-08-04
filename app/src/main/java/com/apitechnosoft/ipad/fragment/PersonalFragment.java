package com.apitechnosoft.ipad.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.RecentFileAdapter;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;


public class PersonalFragment extends MainFragment {
    Typeface materialdesignicons_font;
    RecyclerView recent_recycler_view;
    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void loadView() {
        TextView uploadIcon=findViewById(R.id.uploadIcon);
        TextView searchIcon=findViewById(R.id.searchIcon);

        TextView newFolder=findViewById(R.id.newFolder);
        TextView upFolder=findViewById(R.id.upFolder);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");
        uploadIcon.setTypeface(materialdesignicons_font);
        uploadIcon.setText(Html.fromHtml("&#xf167;"));
        searchIcon.setTypeface(materialdesignicons_font);
        searchIcon.setText(Html.fromHtml("&#xf349;"));

        newFolder.setTypeface(materialdesignicons_font);
        newFolder.setText(Html.fromHtml("&#xf257;"));
        upFolder.setTypeface(materialdesignicons_font);
        upFolder.setText(Html.fromHtml("&#xf259;"));

         recent_recycler_view = (RecyclerView) findViewById(R.id.recent_recycler_view);
        setLinearLayoutManager(recent_recycler_view);
        recent_recycler_view.setNestedScrollingEnabled(false);
        recent_recycler_view.setHasFixedSize(false);

    }
    private void setLinearLayoutManager(RecyclerView recyclerView) {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }


    @Override
    protected void dataToView() {
        ArrayList<Data> dataList=new ArrayList<>();
        for(int i=0;i<10;i++){
            Data data=new Data();
            data.setTitle("Recent"+i);
            dataList.add(data);
        }
        RecentFileAdapter mAdapter = new RecentFileAdapter(getContext(),dataList);
        recent_recycler_view.setAdapter(mAdapter);
    }


}
