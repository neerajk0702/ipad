package com.apitechnosoft.ipad.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.NotificationActivity;
import com.apitechnosoft.ipad.activity.OrganizerActivity;
import com.apitechnosoft.ipad.component.FontViewField;
import com.apitechnosoft.ipad.resource.FNResources;
import com.apitechnosoft.ipad.utils.ASTStringUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;

import static com.apitechnosoft.ipad.utils.ASTObjectUtil.isEmptyStr;


public class HeaderFragment extends MainFragment {

    protected String headerTxt;
    protected TextView title, Organizer, notification, noticount;
    protected FontViewField sliderBtn, backButton;
    private boolean showBackButton;
    private boolean showMenuButton;

    @Override
    protected void getArgs() {
        this.showMenuButton = getArguments().getBoolean("showMenuButton", true);
        headerTxt = this.getArguments().getString("headerTxt", "");
    }

    @Override
    protected int fragmentLayout() {
        return R.layout.header;
    }

    @Override
    protected void loadView() {
        this.sliderBtn = this.findViewById(R.id.sliderBtn);
        this.backButton = this.findViewById(R.id.backBtn);
        this.title = this.findViewById(R.id.title);
        this.Organizer = this.findViewById(R.id.Organizer);
        this.notification = this.findViewById(R.id.notificationicon);
        this.noticount = this.findViewById(R.id.noticount);
        getHostActivity().setDrawerState(showMenuButton);

    }

    @Override
    protected void setClickListeners() {
        this.sliderBtn.setOnClickListener(this);
        this.backButton.setOnClickListener(this);
        this.Organizer.setOnClickListener(this);
        this.notification.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
        this.sliderBtn.setVisibility(showMenuButton ? View.VISIBLE : View.GONE);
        this.backButton.setVisibility(showMenuButton ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void dataToView() {
        this.title.setText(headerTxt);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sliderBtn || v.getId() == R.id.backBtn) {
            if (this.getHostActivity() != null) {
                if (v.getId() == R.id.backBtn) {
                    this.getHostActivity().onBackPressed();
                } else {
                    this.getHostActivity().showSideNavigationPanel();
                }
            }
        } else if (v.getId() == R.id.Organizer) {
            startActivity(new Intent(getContext(), OrganizerActivity.class));
        } else if (v.getId() == R.id.notificationicon) {
            startActivity(new Intent(getContext(), NotificationActivity.class));
        }
    }

    public void updateTitle() {
        if (isEmptyStr(this.headerTxt)) {
            return;
        }
        if (FNResources.string.get(this.headerTxt) != 0) {
            ASTUtil.setTextFromResourceName(ASTStringUtil.getStringForID(FNResources.string.get(this.headerTxt)), this.title);
        }
    }

    public void updateNotification(String noti) {
        if (noticount != null) {
            noticount.setText(noti);
        }

    }
}
