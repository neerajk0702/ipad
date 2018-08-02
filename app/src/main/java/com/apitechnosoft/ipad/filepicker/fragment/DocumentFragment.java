package com.apitechnosoft.ipad.filepicker.fragment;


import com.apitechnosoft.ipad.filepicker.DocumentLoader;
import com.apitechnosoft.ipad.filepicker.adaptor.DocumentAdaptor;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.utils.FNReqResCode;

import java.util.ArrayList;

import static com.apitechnosoft.ipad.utils.FNObjectUtil.isEmpty;
import static java.security.AccessController.getContext;

/**
 * Created 03-07-2017
 *
 * @author AST Inc.
 */
public class DocumentFragment extends MediaFragment {

	@Override
	public void permissionGranted(int requestCode) {
		switch (requestCode) {
			case FNReqResCode.PERMISSION_REQ_WRITE_EXTERNAL_STORAGE:
				startDocLoader();
				break;
		}
	}

	private void startDocLoader() {
		DocumentLoader loader = new DocumentLoader(getContext()) {
			@Override
			protected void onPostExecute(ArrayList<MediaFile> mediaFiles) {
				super.onPostExecute(mediaFiles);
				if (isEmpty(mediaFiles)) {
					showEmptyUI();
				} else {
					showDataUI();
					adapter = new DocumentAdaptor(getContext(), mediaFiles, DocumentFragment.this);
					setAdapter();
				}
			}
		};
		loader.execute();
	}
}
