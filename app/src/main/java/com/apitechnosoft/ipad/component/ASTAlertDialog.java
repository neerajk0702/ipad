package com.apitechnosoft.ipad.component;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;


/**
 * <h4>Created</h4> 1/20/2017
 *
 * @author AST Inc.
 */
public class ASTAlertDialog extends Dialog implements View.OnClickListener {

	public Button okButton, cancelButton;
	LinearLayout cancelBtnLayout, optionalBtnLayout;
	private String message = "";
	private boolean isConfirmation = false;
	private boolean isWarning = false;
	private int layoutId = R.layout.fndilog_alert_n;
	private boolean isOptionalBtnVisible;
	private Button optionalBtn;
	private String positiveBtnTxt, negativeBtnTxt;
	private TextView alertTextView;

	public ASTAlertDialog(Context context) {
		super(context);
	}

	public ASTAlertDialog(Context context, boolean isConfirmation, boolean isWarning) {
		super(context);
		this.isConfirmation = isConfirmation;
		this.isWarning = isWarning;
	}

	public ASTAlertDialog(Context context, boolean isConfirmation, boolean isWarning, boolean isOptionalBtnVisible) {
		super(context);
		this.isConfirmation = isConfirmation;
		this.isWarning = isWarning;
		this.isOptionalBtnVisible = isOptionalBtnVisible;
	}

	public void show(@StringRes int message) {
		this.show(getContext().getString(message));
	}

	public void show(String message) {
		this.message = message;
		super.show();
	}

	protected void loadHeader() {
		float dialogRadius = this.getDimension(R.dimen._10dp);
		ASTUIUtil.setBackgroundRound(this.findViewById(R.id.DialogNLayout), R.color.bg_color, new float[] { dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius });
		ImageView titleIcon = findViewById(R.id.titleIcon);
		titleIcon.setImageResource(this.getHeaderIcon());
	}

	protected void loadBody() {
		this.alertTextView = this.findViewById(R.id.txt_alert_message);
		this.alertTextView.setText(ASTUIUtil.fromHtml(message));
	}

	protected void loadFooter() {
		float dialogRadius = this.getDimension(R.dimen._10dp);
		ASTUIUtil.setBackgroundRound(this.findViewById(R.id.pop_up_footer), android.R.color.white, new float[] { 0, 0, 0, 0, dialogRadius, dialogRadius, dialogRadius, dialogRadius });
		this.findViewById(R.id.footerDivider).setVisibility(View.GONE);
		this.cancelBtnLayout = this.findViewById(R.id.btn_cancel_layout);
		this.optionalBtnLayout = this.findViewById(R.id.optionalBtn_layout);
		this.okButton = this.findViewById(R.id.submitButton);
		this.cancelButton = this.findViewById(R.id.cancelButton);
		this.optionalBtn = this.findViewById(R.id.disapprove);
		if (ASTObjectUtil.isNonEmptyStr(positiveBtnTxt)) {
			this.okButton.setText(positiveBtnTxt);
		}
		if (ASTObjectUtil.isNonEmptyStr(negativeBtnTxt)) {
			this.cancelButton.setText(negativeBtnTxt);
		}
		this.okButton.setOnClickListener(this);
		this.cancelButton.setOnClickListener(this);
		final float buttonRadius = this.getDimension(R.dimen._50dp);
		ASTUIUtil.setBackgroundRect(okButton, R.color.dark_gray, buttonRadius);
		if (this.isConfirmation || this.isOptionalBtnVisible) {
			ASTUIUtil.setBackgroundRect(okButton, R.color.greenLight, buttonRadius);
			ASTUIUtil.setBackgroundRect(cancelBtnLayout, R.color.white, buttonRadius);
			cancelBtnLayout.setVisibility(View.VISIBLE);
			cancelButton.setTextColor(ASTUIUtil.getColor(R.color.blackMedium));
		}
		if (this.isOptionalBtnVisible) {
			optionalBtnLayout.setVisibility(View.VISIBLE);
			optionalBtn.setTextColor(ASTUIUtil.getColor(R.color.blackMedium));
			optionalBtn.setOnClickListener(this);
			cancelButton.setTextColor(ASTUIUtil.getColor(android.R.color.white));
			ASTUIUtil.setBackgroundRect(cancelButton, R.color.gray, buttonRadius);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().windowAnimations = R.style.ScaleFromCenter;
		this.setContentView(this.layoutID());
		this.setCanceledOnTouchOutside(false);
		loadHeader();
		loadBody();
		loadFooter();
		Resources resources = getContext().getResources();
		int scrWidth = resources.getConfiguration().screenWidthDp;
		if (ASTUtil.getDipFromPixel(this.getContext(), scrWidth) <= 700) {
			this.getWindow().setLayout((ASTUtil.getDipFromPixel(this.getContext(), scrWidth) - 50), LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			this.getWindow().setLayout((3 * ASTUtil.getDipFromPixel(this.getContext(), scrWidth)) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}

	protected int layoutID() {
		return layoutId;
	}

	public void setPositiveBtnTxt(String positiveBtnTxt) {
		this.positiveBtnTxt = positiveBtnTxt;
	}

	public void setNegativeBtnTxt(String negativeBtnTxt) {
		this.negativeBtnTxt = negativeBtnTxt;
	}

	private @DrawableRes int getHeaderIcon() {
		if (isConfirmation)
			return R.drawable.icon_check_circle;
		else if (isWarning) {
			return R.drawable.icon_exclamation_triangle;
		}
		return R.drawable.icon_info_sign;
	}

	private @ColorRes int getHeaderColor() {
		if (isConfirmation)
			return R.color.greenLight;
		else if (isWarning) {
			return R.color.darkRed;
		}
		return R.color.orange;
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		if (isConfirmation || isOptionalBtnVisible) {
			if (v.getId() == okButton.getId()) {
				onConfirmation();
			} else if (v.getId() == cancelButton.getId()) {
				if (isConfirmation) {
					onCancelConfirmation();
				} else {
					onNegativeConfirmation();
				}
			} else if (v.getId() == optionalBtn.getId()) {
				onCancelConfirmation();
			}
		} else if (isWarning) {
			onWarningDismiss();
		} else {
			onDefault();
		}
	}

	public void onDefault() {

	}

	public void onNegativeConfirmation() {

	}

	public void onCancelConfirmation() {
	}

	public void onWarningDismiss() {
	}

	public void onConfirmation() {
	}

	@Override
	public void onBackPressed() {
	}

	public float getDimension(@DimenRes int radius) {
		return ASTUIUtil.getDimension(radius);
	}
}
