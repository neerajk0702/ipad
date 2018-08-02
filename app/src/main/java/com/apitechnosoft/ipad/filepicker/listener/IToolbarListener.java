package com.apitechnosoft.ipad.filepicker.listener;


import com.apitechnosoft.ipad.filepicker.view.ASTCropImageView;

/**
 * Created 16-06-2017
 *
 * @author AST Inc.
 */
public interface IToolbarListener {
	void onDone();

	void onCameraClick();

	void rotateImage(ASTCropImageView.RotateDegrees degrees);

	void openCropFragment();
}
