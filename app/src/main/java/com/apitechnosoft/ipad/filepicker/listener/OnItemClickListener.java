package com.apitechnosoft.ipad.filepicker.listener;

import android.view.View;

import com.apitechnosoft.ipad.FNObject;
import com.apitechnosoft.ipad.filepicker.AndroidDeviceFile;


/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public interface OnItemClickListener {
	void onFileClick(View view, FNObject object);

	boolean isSelected(AndroidDeviceFile file);
}
