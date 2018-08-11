package com.apitechnosoft.ipad.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.RecentFileAdapter;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;

public class SharedFragment extends MainFragment {
    Typeface materialdesignicons_font;
    RecyclerView recyclerView;
    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_shared;
    }

    @Override
    protected void loadView() {
        TextView newFolder = findViewById(R.id.newFolder);
        TextView upFolder = findViewById(R.id.upFolder);
      //  TextView filter = findViewById(R.id.filter);
       // TextView filterIcon = findViewById(R.id.filterIcon);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");

        newFolder.setTypeface(materialdesignicons_font);
        newFolder.setText(Html.fromHtml("&#xf257;"));
        upFolder.setTypeface(materialdesignicons_font);
        upFolder.setText(Html.fromHtml("&#xf259;"));
        //filterIcon.setTypeface(materialdesignicons_font);
        //filterIcon.setText(Html.fromHtml("&#xf04a;"));
        TextView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setTypeface(materialdesignicons_font);
        searchIcon.setText(Html.fromHtml("&#xf349;"));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        // filterspinner = (Spinner) findViewById(R.id.filterspinner);
    }

    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }


    @Override
    protected void dataToView() {
        ArrayList<Data> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Data data = new Data();
            data.setTitle("Recent" + i);
            dataList.add(data);
        }
        RecentFileAdapter mAdapter = new RecentFileAdapter(getContext(), dataList,false);
        recyclerView.setAdapter(mAdapter);

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

}
