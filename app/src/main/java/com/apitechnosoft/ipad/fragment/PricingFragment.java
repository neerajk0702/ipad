package com.apitechnosoft.ipad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;

public class PricingFragment extends MainFragment {

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_pricing;
    }
TextView gb5,gb50,gb500;
    @Override
    protected void loadView() {
        gb5=findViewById(R.id.gb5);
        gb50=findViewById(R.id.gb50);
        gb500=findViewById(R.id.gb500);
    }
    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }
    @Override
    protected void dataToView() {
        gb5.setText(Html.fromHtml("5<sup>GB</sup>"));
        gb50.setText(Html.fromHtml("50<sup>GB</sup>"));
        gb500.setText(Html.fromHtml("500<sup>GB</sup>"));
    }


}
