package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.apitechnosoft.ipad.ApplicationHelper;


public class FontViewField extends AppCompatTextView {

    public FontViewField(Context context) {
        this(context, null);
    }

    public FontViewField(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontViewField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        if (this.isInEditMode()) {
            return;
        }
        try {
            setTypeface(ApplicationHelper.application().getIconTypeFace());
        }catch (Exception e){
        }
    }


    @Override
    public void setTextSize(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size);
    }

    public void setTextDimen(@DimenRes int dimenID){
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(dimenID));
    }
}
