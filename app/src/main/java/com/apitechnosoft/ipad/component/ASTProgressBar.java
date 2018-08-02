package com.apitechnosoft.ipad.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;


/**
 * @author AST Inc.
 */
public class ASTProgressBar extends ProgressDialog {
	private TextView progressLabel;

	public ASTProgressBar(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.progress_dialog);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.setCanceledOnTouchOutside(false);
		this.progressLabel = this.findViewById(R.id.progressLabel);

	}

	@Override
	public void show() {
		super.show();
	//	ApplicationHelper.application().setAjaxTaskBusy(true);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		//FNApplicationHelper.application().setAjaxTaskBusy(false);
	}

	public void setProgressLabel(String progressLable) {
		if (this.progressLabel.getVisibility() == View.GONE) {
			this.progressLabel.setVisibility(View.VISIBLE);
		}
		this.progressLabel.setText(progressLable);
	}

	@Override
	public void onBackPressed() {

	}
}
