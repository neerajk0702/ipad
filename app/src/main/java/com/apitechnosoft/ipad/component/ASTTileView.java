package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTStringUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;

/**
 * <h4>Created</h4> 7/18/2017
 *
 * @author AST Inc.
 */
public class ASTTileView extends LinearLayout {

	protected ASTTextView titleView, countView, explanationView;
	protected ASTImageView imageView;
	protected View view;
	protected CardView cardViewLayout;
	protected LinearLayout imageContainer;
	ASTTextView titleHorizontal;
	int orientation = 1;
	@ColorRes
	int circularViewBGColor = R.color.bg_color;
	private int imgHeight, imgWidth, outerPadding;
	private boolean isCircularImg;

	public ASTTileView(Context context) {
		this(context, null);
	}

	public ASTTileView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ASTTileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ASTTileView, defStyle, 0);
		if (array.hasValue(R.styleable.ASTTileView_imgHeight)) {
			this.imgHeight = (int) array.getDimension(R.styleable.ASTTileView_imgHeight, ASTUIUtil.getDimension(R.dimen._30dp));
		}
		if (array.hasValue(R.styleable.ASTTileView_imgWidth)) {
			this.imgWidth = (int) array.getDimension(R.styleable.ASTTileView_imgWidth, ASTUIUtil.getDimension(R.dimen._30dp));
		}
		if (array.hasValue(R.styleable.ASTTileView_outerPadding)) {
			this.outerPadding = (int) array.getDimension(R.styleable.ASTTileView_outerPadding, ASTUIUtil.getDimension(R.dimen._12dp));
		}
		if (array.hasValue(R.styleable.ASTTileView_isCircularImg)) {
			this.isCircularImg = array.getBoolean(R.styleable.ASTTileView_isCircularImg, false);
		}
		array.recycle();
		this.init();
	}

	public void setImageCircleColor(boolean isaddImageCircle) {
		this.circularViewBGColor = isaddImageCircle ? R.color.bg_color : android.R.color.transparent;
	}

	private void init() {
		if (this.isInEditMode()) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.view = inflater.inflate(layoutId(), this, true);
		this.titleView = view.findViewById(R.id.title);
		this.countView = view.findViewById(R.id.count);
		this.imageView = view.findViewById(R.id.image);
		this.imageContainer = view.findViewById(R.id.imageContainer);
		this.explanationView = view.findViewById(R.id.explanation);
		this.cardViewLayout = view.findViewById(R.id.cardViewLayout);
		titleHorizontal = findViewById(R.id.titleHorizontral);
		this.imageContainer.setPadding(this.outerPadding, this.outerPadding, this.outerPadding, this.outerPadding);
		setTileImageSize();
		addImageCircle();
	}

	public void setTileImageSize() {
		if (this.imgHeight > 0 && imgWidth > 0) {
			LayoutParams parms = new LayoutParams(imgWidth, imgHeight);
			this.imageView.setLayoutParams(parms);
			this.imageView.setCircular(isCircularImg);
		}
	}

	protected int layoutId() {
		return R.layout.fn_tile_view;
	}

	public void setTitle(String title) {
		ASTTextView titleView = getTileView();
		if (titleView == null) {
			return;
		}
		titleView.setText(title);
	}

	public void setTitle(@StringRes int title) {
		setTitle(ASTStringUtil.getStringForID(title));
	}

	public void setTitleColor(@ColorRes int colorId) {
		ASTTextView titleView = getTileView();
		if (titleView == null) {
			return;
		}
		int resId = ASTUIUtil.getColor(colorId);
		if (resId != 0) {
			titleView.setTextColor(resId);
		}
	}

	private boolean isVertical() {
		return orientation == LinearLayout.VERTICAL;
	}

	public void setCount(int count) {
		if (this.countView == null) {
			return;
		}
		this.countView.setVisibility(View.VISIBLE);
		String countString = String.valueOf(count);
		if (count > 99) {
			this.countView.setTextDimen(R.dimen._10dp);
			countString = 99 + "+";
		}
		this.countView.setText(countString);
	}

	public void setExplanation(String explanation) {
		if (this.explanationView == null) {
			return;
		}
		this.explanationView.setVisibility(VISIBLE);
		this.explanationView.setText(explanation);
	}

	public void setExplanation(@StringRes int explanation) {
		setExplanation(ASTStringUtil.getStringForID(explanation));
	}

	public void hideImage() {
		if (imageView != null) {
			this.imageView.setVisibility(View.GONE);
		}
	}

	private boolean isImageViewInit() {
		if (this.imageView == null) {
			return false;
		}
		this.imageView.setVisibility(View.VISIBLE);
		return true;
	}

	public void setImageUrl(String imgUrl) {
		if (isImageViewInit()) {
			imageView.setURL(imgUrl, R.drawable.noimage);
		}
	}

	public void setImageDrawable(Drawable icon) {
		if (isImageViewInit()) {
			this.imageView.setImageDrawable(icon);
		}
	}

	public void setImageResource(@DrawableRes int icon) {
		if (isImageViewInit()) {
			this.imageView.setImageResource(icon);
			this.addImageCircle();
		}
	}

	public void hideCountField() {
		if (countView == null) {
			return;
		}
		this.countView.setVisibility(View.GONE);
	}

	public void hideTitleView() {
		if (titleView != null) {
			titleView.setVisibility(GONE);
		}
	}

	public void setExplanationColor(@ColorRes int colorId) {
		if (this.explanationView == null) {
			return;
		}
		int resId = ASTUIUtil.getColor(getContext(), colorId);
		if (resId != 0) {
			this.explanationView.setTextColor(resId);
		}
	}

	public void addImageCircle() {
		imageContainer.post(new Runnable() {
			@Override
			public void run() {
				int radius = imageContainer.getWidth() > imageContainer.getHeight() ? imageContainer.getWidth() / 2 : imageContainer.getHeight() / 2;
				int paddingLeft = imageContainer.getWidth() > imageContainer.getHeight() ? outerPadding : (imageContainer.getHeight() - imageContainer.getWidth()) + outerPadding;
				int paddingTop = imageContainer.getHeight() > imageContainer.getWidth() ? outerPadding : imageContainer.getWidth() - imageContainer.getHeight() + outerPadding;
				imageContainer.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
				ASTUIUtil.setBackgroundRect(imageContainer, circularViewBGColor, radius + outerPadding);
			}
		});
	}

	public void changeDirection(int orientation) {
		this.orientation = orientation;
		switch (orientation) {
			case LinearLayout.HORIZONTAL:
				titleHorizontal.setVisibility(View.VISIBLE);
				titleView.setVisibility(View.GONE);
				break;
			case LinearLayout.VERTICAL:
				titleHorizontal.setVisibility(View.GONE);
				titleView.setVisibility(View.VISIBLE);
				break;
		}
	}

	private ASTTextView getTileView() {
		switch (orientation) {
			case LinearLayout.HORIZONTAL:
				return titleHorizontal;
			case LinearLayout.VERTICAL:
				return titleView;
		}
		return null;
	}

	public void setCardViewBg(int colorId) {
		if (this.cardViewLayout != null) {
			cardViewLayout.setCardBackgroundColor(ASTUIUtil.getColor(colorId));
		}
	}

	public void setTitleBgColor(@ColorRes int colorId) {
		ASTTextView titleView = getTileView();
		if (titleView == null) {
			return;
		}
		int resId = ASTUIUtil.getColor(colorId);
		if (resId != 0) {
			titleView.setBackgroundColor(resId);
		}
	}

	public void setTitleTextFontSize(@DimenRes int textSize) {
		ASTTextView titleView = getTileView();
		if (titleView == null) {
			return;
		}
		titleView.setTextDimen(textSize);
	}
}
