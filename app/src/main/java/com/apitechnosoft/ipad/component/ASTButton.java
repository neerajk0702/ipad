package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.apitechnosoft.ipad.utils.ASTEnum;
import com.apitechnosoft.ipad.utils.ASTUIUtil;


/**
 * <h4>Created</h4> 16/02/17
 *
 * @author AST Inc.
 */
public class ASTButton extends AppCompatButton {

    public ASTButton(Context context) {
        super(context);
        init();
    }

    public ASTButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ASTButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    public void setTypeFace(ASTEnum fontTypeFace) {
        ASTUIUtil.setFontTypeFace(this, fontTypeFace);
    }
}
