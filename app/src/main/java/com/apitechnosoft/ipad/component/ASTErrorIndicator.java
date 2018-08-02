package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTUtil;

/**
 * @author AST Inc.
 */
public class ASTErrorIndicator {

    PopupWindow popupWindow;
    View parentView = null;
    Context context = null;
    LayoutInflater inflater = null;
    TextView textView;
    WindowManager windowManager = null;
    boolean isShowing;

    public ASTErrorIndicator(Context context) {
        this.context = context;
        this.popupWindow = new PopupWindow(context);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.setRootViewId(R.layout.error_indicator);
    }

    public void setRootViewId(int id) {
        this.parentView = this.inflater.inflate(id, null);
        this.textView = this.parentView.findViewById(R.id.error_msg);
        this.setContentView(this.parentView);
    }

    public void setContentView(View root) {
        this.parentView = root;
        this.popupWindow.setContentView(this.parentView);
    }

    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setContentView(inflator.inflate(layoutResID, null));
    }

    public void show(View anchor, String text) {
        if (this.parentView == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }
        this.isShowing = true;
        this.setText(text);
        int scrWidth = this.context.getApplicationContext().getResources().getConfiguration().screenWidthDp;
        this.popupWindow.setWidth(ASTUtil.getDipFromPixel(this.context, scrWidth));
        this.popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(false);
        this.popupWindow.setOutsideTouchable(false);
        this.popupWindow.setContentView(this.parentView);
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
        this.popupWindow.setAnimationStyle(R.style.errorIndicatorStyle);
        this.popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (anchorRect.left), anchorRect.bottom);
        ASTFontViewField closeButton = parentView.findViewById(R.id.closeErrorIndicator);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ASTErrorIndicator.this.popupWindow.dismiss();
                ASTErrorIndicator.this.isShowing = false;
            }
        }, 3000);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        closeButton.setVisibility(View.VISIBLE);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setText(@StringRes int textID) {
        this.textView.setText(textID);
    }

    public boolean isShowing() {
        return this.isShowing;
    }
}