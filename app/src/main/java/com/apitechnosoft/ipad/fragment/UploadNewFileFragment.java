package com.apitechnosoft.ipad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apitechnosoft.ipad.R;


public class UploadNewFileFragment extends MainFragment {

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_upload_new_file;
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

    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
           /* case R.id.uploadLayout:
                openUploadScreen();
                break;*/
        }
    }
}
