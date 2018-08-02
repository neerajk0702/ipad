package com.apitechnosoft.ipad.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FNReqResCode;

import java.util.ArrayList;

import static com.apitechnosoft.ipad.ApplicationHelper.application;


public abstract class MainFragment extends Fragment implements View.OnClickListener {

    protected View view;
    long previousTime;
    protected boolean willPopBackOnResume;
    protected boolean willReloadBackScreen;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.hideSoftKeyboard();
        if (this.getArguments() != null) {
            this.getArgs();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(this.fragmentLayout(), container, false);
        loadPageView();
        return this.view;
    }

    public void loadPageView() {
        if (this.view == null) {
            return;
        }
        this.loadView();
        this.setClickListeners();
        this.dataToView();
        this.setAccessibility();
    }

    public String getTextFromView(View v) {
        return ASTObjectUtil.getTextFromView(v);
    }

    protected void getArgs() {

    }

    public MainActivity getHostActivity() {
        return (MainActivity) getActivity();
    }

    public void hideSoftKeyboard() {
        ASTUIUtil.hideSoftKeyboard(this.getActivity());
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return (T) this.view.findViewById(id);
    }

    public void permissionGranted(int requestCode) {
    }


    public void permissionDenied(int requestCode) {
    }

    /**
     * Load you fragment layout here
     *
     * @return
     */

    @LayoutRes
    protected abstract int fragmentLayout();

    /**
     * load all view in here
     */
    protected abstract void loadView();

    /**
     * Set all page level click listeners here.
     */
    protected abstract void setClickListeners();

    /**
     * Set all Visibility and other things for user UI according to access detail or your logic.
     */
    protected abstract void setAccessibility();

    /**
     * Load all server data and logic data to view here.
     */
    protected abstract void dataToView();

    @Override
    public void onClick(View view) {

    }

    protected void moveTaskToBack() {
        if (this.getActivity() == null) {
            return;
        }
        if (2000 + previousTime > (previousTime = System.currentTimeMillis())) {
            this.getActivity().moveTaskToBack(true);
        } else {
            Snackbar.make(view, "Press Again", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void updateOnResult(int requestCode, int resultCode, Intent data) {
    }

   /* public boolean onBackPressed() {
        return true;
    }*/

    public boolean onBackPressed() {
        if (this.willReloadBackScreen) {
            return this.reloadBackScreen();
        }
        return true;
    }

    public boolean reloadBackScreen() {
        return this.reloadBackScreen(null);
    }

    public boolean reloadBackScreen(Intent data) {
        return this.reloadBackScreen(data, FNReqResCode.RES_PAGE_COMMUNICATOR);
    }


    public boolean reloadBackScreen(Intent data, Integer activityResult) {
        if (this.getTargetFragment() != null) {
            this.getTargetFragment().onActivityResult(FNReqResCode.REQ_PAGE_COMMUNICATOR, activityResult, data);
            this.popBackStack();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (willPopBackOnResume)
            getFragmentManager().popBackStack();
        willPopBackOnResume = false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void popBackStack() {
        if (!application().isAppInForground()) {
            willPopBackOnResume = true;
            return;
        }
        if (getFragmentManager() == null || getFragmentManager().getBackStackEntryCount() == 1) {
            return;
        }
        getFragmentManager().popBackStack();
    }

    protected String getArgsString(String key) {
        return this.getArguments().getString(key);
    }

    protected boolean getArgsBoolean(String key) {
        return getArgsBoolean(key, false);
    }

    protected boolean getArgsBoolean(String key, boolean defaultValue) {
        return this.getArguments().getBoolean(key, defaultValue);
    }

    protected int getArgsInt(String key) {
        return this.getArguments().getInt(key);
    }

    protected long getArgsLong(String key) {
        return this.getArguments().getLong(key);
    }

    protected <T extends Parcelable> T getParcelable(String key) {
        return this.getArguments().getParcelable(key);
    }

    protected <T extends Parcelable> ArrayList<T> getParcelableArrayList(String key) {
        return this.getArguments().getParcelableArrayList(key);
    }

    protected Parcelable[] getParcelableArray(String key) {
        return this.getArguments().getParcelableArray(key);
    }


    protected void showErrorIndicator(@StringRes int messageResId, Object... args) {
        ASTUIUtil.showErrorIndicator(getActivity(), getString(messageResId, args), getHostActivity().headerView());
    }

}
