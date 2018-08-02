package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTUIUtil;

/**
 * @author AST Inc.
 */
public class ASTFontViewField extends AppCompatTextView {

	public ASTFontViewField(Context context) {
		this(context, null);
	}

	public ASTFontViewField(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ASTFontViewField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	private void init() {
		if (this.isInEditMode()) {
			return;
		}
		try {
			setTypeface(ApplicationHelper.application().getIconTypeFace());
		} catch (Exception e) {
		}
		if (getTextColors() == null) {
			this.setTextColor(ASTUIUtil.getColor(R.color.black));
		}
	}

	@Override
	public void setTextSize(float size) {
		super.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}

	public void setTextDimen(@DimenRes int dimenID) {
		super.setTextSize(TypedValue.COMPLEX_UNIT_PX, ASTUIUtil.getDimension(dimenID));
	}
}
