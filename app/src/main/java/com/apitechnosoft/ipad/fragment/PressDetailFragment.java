package com.apitechnosoft.ipad.fragment;

import android.view.View;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;

public class PressDetailFragment extends MainFragment {
    TextView detail;
    int detailvalue;

    @Override
    protected int fragmentLayout() {
        return R.layout.press_detail_fragment;
    }

    @Override
    protected void loadView() {
        detail = findViewById(R.id.detail);
    }

    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {
        if(detailvalue==1) {
            detail.setText(getString(R.string.novdetail));
        }else if(detailvalue==2){
           detail.setText(getString(R.string.octdetail));
        }else if(detailvalue==3){
            detail.setText(getString(R.string.decdetail));
        }else if(detailvalue==4){
           detail.setText(getString(R.string.oct2detail));
        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        detailvalue = getArguments().getInt("Key", 0);
    }
}

