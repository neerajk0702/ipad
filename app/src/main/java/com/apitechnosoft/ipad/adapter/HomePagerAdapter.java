package com.apitechnosoft.ipad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apitechnosoft.ipad.fragment.PersonalFragment;
import com.apitechnosoft.ipad.fragment.ReceivedFragment;
import com.apitechnosoft.ipad.fragment.SharedFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HomePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PersonalFragment tab1 = new PersonalFragment();
                return tab1;
            case 1:
                SharedFragment tab2 = new SharedFragment();
                return tab2;
            case 2:
                ReceivedFragment tab3 = new ReceivedFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

