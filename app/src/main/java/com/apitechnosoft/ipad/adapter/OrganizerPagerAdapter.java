package com.apitechnosoft.ipad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apitechnosoft.ipad.fragment.BirthdayFragment;
import com.apitechnosoft.ipad.fragment.EventFragment;
import com.apitechnosoft.ipad.fragment.PersonalFragment;
import com.apitechnosoft.ipad.fragment.ReceivedFragment;
import com.apitechnosoft.ipad.fragment.SharedFragment;

public class OrganizerPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public OrganizerPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EventFragment tab1 = new EventFragment();
                return tab1;
            case 1:
                BirthdayFragment tab2 = new BirthdayFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


