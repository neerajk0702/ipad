package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FNObjectUtil;

/**
 * <h4>Created</h4> 01/28/16
 *
 * @author AST Inc.
 */
public class ASTFrameIcon extends FrameLayout {

	private ASTFontViewField frame;
	private ASTFontViewField icon;

	public ASTFrameIcon(Context context) {
		this(context, null);
	}

	public ASTFrameIcon(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ASTFrameIcon(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		if (this.isInEditMode()) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.frame_icon_view, this, true);
		frame = view.findViewById(R.id.iconFrame);
		icon = view.findViewById(R.id.icon);
		loadAttributes(attrs);
	}

	private void loadAttributes(AttributeSet attrs) {
		if (attrs == null) {
			return;
		}
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ASTFrameIcon);
		float frameSize = a.getDimension(R.styleable.ASTFrameIcon_frameSize, 0);
		float iconSize = a.getDimension(R.styleable.ASTFrameIcon_iconSize, 0);
		String frameIcon = a.getString(R.styleable.ASTFrameIcon_frameIconText);
		String icon = a.getString(R.styleable.ASTFrameIcon_iconText);
		int frameColor = a.getColor(R.styleable.ASTFrameIcon_frameColor, 0);
		int iconColor = a.getColor(R.styleable.ASTFrameIcon_iconColor, 0);
		setFrameSize(frameSize);
		setIconSize(iconSize);
		setFrameIcon(frameIcon);
		setIcon(icon);
		setFrameColor(frameColor);
		setIconColor(iconColor);
		a.recycle();
	}

	public void setFrameSize(float size) {
		if (size <= 0) {
			return;
		}
		this.frame.setTextSize(size);
	}

	public void setIconSize(float size) {
		if (size <= 0) {
			return;
		}
		this.icon.setTextSize(size);
	}

	public void setFrameDimen(@DimenRes int resId) {
		this.frame.setTextDimen(resId);
	}

	public void setIconDimen(@DimenRes int resId) {
		this.icon.setTextDimen(resId);
	}

	public void setIcon(int iconId) {
		if (iconId < 0) {
			return;
		}
		this.icon.setText(this.getResources().getString(iconId));
	}

	public void setIcon(String iconStr) {
		if (FNObjectUtil.isEmptyStr(iconStr)) {
			return;
		}
		this.icon.setText(iconStr);
	}

	public void setFrameIcon(int iconId) {
		if (iconId < 0) {
			return;
		}
		this.frame.setText(this.getResources().getString(iconId));
	}

	public void setFrameIcon(String iconStr) {
		if (FNObjectUtil.isEmptyStr(iconStr)) {
			return;
		}
		this.frame.setText(iconStr);
	}

	public void setFrameColor(int colorId) {
		if (colorId > 0) {
			frame.setTextColor(ASTUIUtil.getColor(getContext(), colorId));
		} else {
			frame.setTextColor(colorId);
		}
	}

	public void setIconColor(int colorId) {
		if (colorId > 0) {
			icon.setTextColor(ASTUIUtil.getColor(getContext(), colorId));
		} else {
			icon.setTextColor(colorId);
		}
	}

	public void startIconAnimation() {
		ASTUIUtil.startAnimation(icon);
	}

	public void cancelIconAnimation() {
		ASTUIUtil.cancelAnimation(icon);
	}

}
