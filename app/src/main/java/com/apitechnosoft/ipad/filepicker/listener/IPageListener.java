package com.apitechnosoft.ipad.filepicker.listener;


import com.apitechnosoft.ipad.filepicker.FNFilePickerConfig;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;

/**
 * Created 07-06-2017
 *
 * @author AST Inc.
 */
public interface IPageListener {
	boolean isValidToAddFile();

	FNFilePickerConfig config();

	void openCropFragment(MediaFile file);

	void updateHeader();
}
