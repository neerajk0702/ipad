package com.apitechnosoft.ipad.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.HomePagerAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;


/**
 * Created by Narayan .
 */
public class HomeFragment extends MainFragment {

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

    }



    }