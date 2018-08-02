package com.apitechnosoft.ipad.component;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.ASTStringUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;

/**
 * @author AST Inc.
 */
public class ASTNotificationDialog extends ASTAlertDialog {

	public TextureView messageView2;
	public TextureView messageView1;
	public float dialogRadius;

	public ASTNotificationDialog(Context context) {
		super(context, true, false);
	}

	protected int layoutID() {
		return R.layout.network_error_dialog;
	}

	@Override
	protected void loadHeader() {
		this.dialogRadius = this.getDimension(R.dimen._10dp);
		ASTUIUtil.setBackgroundRound(this.findViewById(R.id.DialogLayout), R.color.bg_color, new float[] { dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius, dialogRadius });
	}

	@Override
	protected void loadBody() {
		this.messageView1 = this.findViewById(R.id.messageView1);
		this.messageView2 = this.findViewById(R.id.messageView2);
		//this.messageView1.setTypeFace(ASTEnum.FONT_SEMIBOLD);
		//this.messageView2.setTypeFace(ASTEnum.FONT_SEMIBOLD);
		ImageView myImageView = this.findViewById(R.id.imageView1);
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.blink_anim); //fade it in, and fade it out.
		myImageView.startAnimation(myFadeInAnimation);
	}

	@Override
	protected void loadFooter() {
		ASTUIUtil.setBackgroundRound(this.findViewById(R.id.pop_up_footer), android.R.color.white, new float[] { 0, 0, 0, 0, dialogRadius, dialogRadius, dialogRadius, dialogRadius });
		this.setPositiveBtnTxt(ASTStringUtil.getStringForID(R.string.openSett));
		this.setNegativeBtnTxt(ASTStringUtil.getStringForID(R.string.close));
		super.loadFooter();
		float buttonRadius = this.getDimension(R.dimen._50dp);
		ASTUIUtil.setBackgroundRect(okButton, R.color.greenDark, buttonRadius);
		this.findViewById(R.id.footerDivider).setVisibility(View.GONE);
	}

	@Override
	public void onConfirmation() {
		this.getContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
	}

}
