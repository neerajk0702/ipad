package com.apitechnosoft.ipad.filepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class SquareFrameLayout extends FrameLayout {

	public SquareFrameLayout(Context context) {
		super(context);
	}

	public SquareFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
