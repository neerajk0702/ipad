package com.apitechnosoft.ipad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.TermsConditionActivity;


public class PressFragment extends MainFragment {
    TextView nov, oct, dec, oct2;

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_press;
    }

    @Override
    protected void loadView() {
        nov = findViewById(R.id.nov);
        oct = findViewById(R.id.oct);
        dec = findViewById(R.id.dec);
        oct2 = findViewById(R.id.oct2);
    }

    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {
        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(1);
            }
        });
        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(2);
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(3);
            }
        });
        oct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(4);
            }
        });
    }

    private void opneDetail(int key) {
        Bundle bundle = new Bundle();
        bundle.putString("headerTxt", "Press Detail");
        bundle.putInt("Key",key);
        ApplicationHelper.application().getActivity().updateFragment(new PressDetailFragment(), bundle);
    }
}
