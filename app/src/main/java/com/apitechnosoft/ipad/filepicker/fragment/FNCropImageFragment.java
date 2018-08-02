package com.apitechnosoft.ipad.filepicker.fragment;


import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.filepicker.AndroidDeviceFile;
import com.apitechnosoft.ipad.filepicker.helper.BitmapConverterTask;
import com.apitechnosoft.ipad.filepicker.listener.CropCallback;
import com.apitechnosoft.ipad.filepicker.listener.IPageListener;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.filepicker.view.ASTCropImageView;
import com.apitechnosoft.ipad.fragment.MainFragment;

import java.util.ArrayList;

/**
 * Created 14-06-2017
 *
 * @author AST Inc.
 */
public class FNCropImageFragment extends MainFragment {

	private ASTCropImageView cropImageView;
	private MediaFile imageFile;
	private boolean isProfilePic;

	@Override
	protected void getArgs() {
		this.imageFile = this.getParcelable("imageFile");
		//this.isProfilePic = this.getArgsBoolean(FNFilePicker.EXTRA_CROP_MODE, false);

		isProfilePic=false;
	}

	@Override
	protected int fragmentLayout() {
		return R.layout.picker_crop_view;
	}

	@Override
	protected void loadView() {
		cropImageView = this.findViewById(R.id.cropImageView);
		cropImageView.setCropMode(isProfilePic ? ASTCropImageView.CropMode.CIRCLE_SQUARE : ASTCropImageView.CropMode.SQUARE);
	}

	@Override
	protected void setClickListeners() {
	}

	@Override
	protected void setAccessibility() {
	}

	@Override
	protected void dataToView() {
		ArrayList<MediaFile> mediaFiles = new ArrayList<>();
		mediaFiles.add(imageFile);
		BitmapConverterTask converterTask = new BitmapConverterTask(this.getContext()) {
			@Override
			protected void onPostExecute(ArrayList<? extends AndroidDeviceFile> fileList) {
				this.dismissProgressBar();
				if (fileList.size() > 0) {
					cropImageView.setOrientation(imageFile.getOrientation());
					cropImageView.setImageBitmap(fileList.get(0).getPhoto());
				}
			}
		};
		converterTask.execute(mediaFiles);
	}



	@Override
	public boolean onBackPressed() {
		getHostActivity().redirectToHomeMenu();
		return false;
	}

	public void rotateImage(ASTCropImageView.RotateDegrees degrees) {
		cropImageView.rotateImage(degrees);
	}

	public void startCrop(CropCallback cropCallback) {
		if (activityListener() == null) {
			return;
		}
		this.cropImageView.startCrop(imageFile, cropCallback);
	}

	private IPageListener activityListener() {
		return getHostActivity() != null ? (IPageListener) getHostActivity() : null;
	}
}
