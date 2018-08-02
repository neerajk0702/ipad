package com.apitechnosoft.ipad.filepicker.listener;

import com.apitechnosoft.ipad.filepicker.model.MediaFile;

/**
 * Created 16-06-2017
 *
 * @author AST Inc.
 */
public interface CropCallback {
	void onSuccess(MediaFile file);

	void onError();
}
