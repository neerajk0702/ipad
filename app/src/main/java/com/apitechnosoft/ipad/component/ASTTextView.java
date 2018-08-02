package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTEnum;
import com.apitechnosoft.ipad.utils.ASTUIUtil;


/**
 * <h4>Created</h4> 16/02/17
 *
 * @author AST Inc.
 */
public class ASTTextView extends AppCompatTextView {

    public ASTTextView(Context context) {
        super(context);
        init();
    }

    public ASTTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ASTTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (this.isInEditMode()) {
            return;
        }
        if (getTypeface() != null) {
            ASTUIUtil.setFontTypeFace(this, getTypeface().getStyle());
        } else {
            this.setTypeFace(ASTEnum.FONT_REGULAR);
        }
        if (getTextColors() == null) {
            this.setTextColor(ASTUIUtil.getColor(R.color.black));
        }
    }

    public void setTypeFace(ASTEnum fontTypeFace) {
        ASTUIUtil.setFontTypeFace(this, fontTypeFace);
    }

    public void setTextDimen(@DimenRes int dimenID) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, ASTUIUtil.getDimension(dimenID));
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }
}
