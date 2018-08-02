package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.RequestHandler;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.filepicker.PicassoTrustAll;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.CircleTransform;
import com.apitechnosoft.ipad.utils.FNObjectUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;

/**
 * Created 27-06-2017
 *
 * @author AST Inc.
 */
public class ASTImageView extends AppCompatImageView {

	private Uri uri;
	private String url;
	private File file;
	private Drawable defaultImg;

	private boolean isCircular = false;
	private RequestHandler requestHandler;

	private int imgWidth;
	private int imgHeight;

	public ASTImageView(Context context) {
		this(context, null);
	}

	public ASTImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ASTImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
	}

	private void init(AttributeSet attrs, int defStyleAttr) {
		if (isInEditMode()) {
			return;
		}
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ASTImageView, defStyleAttr, 0);
			isCircular = a.getBoolean(R.styleable.ASTImageView_isCircular, false);
			a.recycle();
		}
		this.file = null;
		this.uri = null;
		this.url = null;
		this.defaultImg = ASTUIUtil.getDrawable(R.drawable.noimage);
	}

	public void setURI(@Nullable Uri uri, @DrawableRes int defaultImg) {
		this.setURI(uri, ASTUIUtil.getDrawable(defaultImg));
	}

	/**
	 * try to use setURI
	 *
	 * @param uri
	 */
	@Deprecated
	@Override
	public void setImageURI(@Nullable Uri uri) {
		super.setImageURI(uri);
	}

	public void setURI(@Nullable Uri uri, Drawable defaultImg) {
		this.uri = uri;
		this.defaultImg = defaultImg;
		loadImage();
	}

	public void setURL(@Nullable String url, @DrawableRes int defaultImg) {
		this.setURL(url, ASTUIUtil.getDrawable(defaultImg));
	}

	public void setURL(@Nullable String url, Drawable defaultImg) {
		this.url = StringEscapeUtils.unescapeHtml3(url);
		this.defaultImg = defaultImg;
		loadImage();
	}

	public void setImageFile(@Nullable File file, @DrawableRes int defaultImg) {
		this.setImageFile(file, ASTUIUtil.getDrawable(defaultImg));
	}

	public void setImageFile(@Nullable File file, Drawable defaultImg) {
		this.file = file;
		this.defaultImg = defaultImg;
		loadImage();
	}

	private void loadImage() {
		this.evaluateSize();
		Picasso picasso = requestHandler == null ? ((FNObjectUtil.isNonEmptyStr(url) && url.startsWith("https")) ? PicassoTrustAll.instance(getContext()) : Picasso.with(getContext()))
				: new Picasso.Builder(getContext().getApplicationContext()).addRequestHandler(requestHandler).build();
		RequestCreator rc = null;
		if (FNObjectUtil.isNonEmptyStr(url)) {
			rc = picasso.load(url);
		} else if (uri != null) {
			rc = picasso.load(uri);
		} else if (file != null) {
			rc = picasso.load(file);
		}
		if (rc != null) {
			if (this.imgWidth > 0 && this.imgHeight > 0) {
				rc.resize(this.imgWidth, this.imgHeight)
						.centerCrop();
			}
			if (isCircular) {
				rc.transform(new CircleTransform());
			}
			rc.placeholder(defaultImg)
					.error(defaultImg)
					.into(ASTImageView.this);
		} else {
			this.setImageDrawable(defaultImg);
		}
	}

	public void setCircular(boolean circular) {
		isCircular = circular;
	}

	public void setSize(int imgWidth, int imgHeight) {
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
	}

	public void evaluateSize() {
		if ((this.getLayoutParams().width == LinearLayout.LayoutParams.WRAP_CONTENT || this.getLayoutParams().width == LinearLayout.LayoutParams.MATCH_PARENT) &&
				(this.getLayoutParams().height == LinearLayout.LayoutParams.WRAP_CONTENT || this.getLayoutParams().height == LinearLayout.LayoutParams.MATCH_PARENT)) {
			return;
		}
		if (this.imgWidth == 0) {
			this.imgWidth = this.getLayoutParams().width > 0 ? this.getLayoutParams().width : ASTUIUtil.getDimensionInt(R.dimen._50dp);
		}
		if (this.imgHeight == 0) {
			this.imgHeight = this.getLayoutParams().height > 0 ? this.getLayoutParams().height : ASTUIUtil.getDimensionInt(R.dimen._50dp);
		}
	}

	public void setRequestHandler(RequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}
}
