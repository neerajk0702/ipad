package com.apitechnosoft.ipad.fragment;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.HomePagerAdapter;
import com.apitechnosoft.ipad.adapter.RecentFileAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.utils.FontManager;

import java.util.ArrayList;


/**
 * Created by Narayan .
 */
public class HomeFragment extends MainFragment {
    Typeface materialdesignicons_font;
    RecyclerView recent_recycler_view;

    @Override
    protected int fragmentLayout() {
        return R.layout.home_fragment_layout;
    }

    @Override
    protected void loadView() {
    }

    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {
        TextView uploadIcon = findViewById(R.id.uploadIcon);
        TextView searchIcon = findViewById(R.id.searchIcon);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");
        uploadIcon.setTypeface(materialdesignicons_font);
        uploadIcon.setText(Html.fromHtml("&#xf167;"));
        searchIcon.setTypeface(materialdesignicons_font);
        searchIcon.setText(Html.fromHtml("&#xf349;"));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Shared"));
        tabLayout.addTab(tabLayout.newTab().setText("Received"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final HomePagerAdapter adapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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

        recent_recycler_view = (RecyclerView) findViewById(R.id.recent_recycler_view);
        setLinearLayoutManager(recent_recycler_view);
        recent_recycler_view.setNestedScrollingEnabled(false);
        recent_recycler_view.setHasFixedSize(false);
        ArrayList<Data> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Data data = new Data();
            data.setTitle("Recent" + i);
            dataList.add(data);
        }
        RecentFileAdapter mAdapter = new RecentFileAdapter(getContext(), dataList);
        recent_recycler_view.setAdapter(mAdapter);

    }

    private void setLinearLayoutManager(RecyclerView recyclerView) {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


}