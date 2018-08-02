package com.apitechnosoft.ipad.filepicker;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.apitechnosoft.ipad.FNObject;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;

import java.util.ArrayList;

/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class FNFilePickerConfig extends FNObject {

	public static final Parcelable.Creator<FNFilePickerConfig> CREATOR = new Parcelable.Creator<FNFilePickerConfig>() {
		@Override
		public FNFilePickerConfig createFromParcel(Parcel source) {
			return new FNFilePickerConfig(source);
		}

		@Override
		public FNFilePickerConfig[] newArray(int size) {
			return new FNFilePickerConfig[size];
		}
	};

	private ArrayList<MediaFile> selectedFiles;
	private String imageDirectory;
	private int mode;
	private int limit;
	private long sizeLimit;
	private boolean folderMode;
	private boolean showCamera;
	private boolean returnAfterFirst;
	private int mediaType;

	public FNFilePickerConfig(Context context) {
		this.mode = FNFilePicker.MODE_MULTIPLE;
		this.limit = FNFilePicker.MAX_LIMIT;
		this.sizeLimit = FNFilePicker.SIZE_LIMIT;
		this.showCamera = true;
		this.selectedFiles = new ArrayList<>();
		this.folderMode = false;
		this.imageDirectory = context.getString(R.string.text_camera);
		this.returnAfterFirst = false;
	}

	protected FNFilePickerConfig(Parcel in) {
		this.selectedFiles = in.createTypedArrayList(MediaFile.CREATOR);
		this.imageDirectory = in.readString();
		this.mode = in.readInt();
		this.limit = in.readInt();
		this.sizeLimit = in.readLong();
		this.folderMode = in.readByte() != 0;
		this.showCamera = in.readByte() != 0;
		this.returnAfterFirst = in.readByte() != 0;
		this.mediaType = in.readInt();
	}

	public boolean isReturnAfterFirst() {
		return returnAfterFirst;
	}

	public void setReturnAfterFirst(boolean returnAfterFirst) {
		this.returnAfterFirst = returnAfterFirst;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(long sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public boolean isShowCamera() {
		return showCamera;
	}

	public void setShowCamera(boolean showCamera) {
		this.showCamera = showCamera;
	}

	public ArrayList<MediaFile> getSelectedFiles() {
		if (selectedFiles == null) {
			selectedFiles = new ArrayList<>();
		}
		return selectedFiles;
	}

	public void setSelectedFiles(ArrayList<MediaFile> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

	public int getSelectedPosition(MediaFile file) {
		for (int i = 0; i < getSelectedFiles().size(); i++) {
			if (getSelectedFiles().get(i).getPath().equals(file.getPath())) {
				return i;
			}
		}
		return -1;
	}

	public void removeSelectedPosition(int position) {
		this.getSelectedFiles().remove(position);
	}

	public boolean isFolderMode() {
		return folderMode;
	}

	public void setFolderMode(boolean folderMode) {
		this.folderMode = folderMode;
	}

	public String getImageDirectory() {
		return imageDirectory;
	}

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.selectedFiles);
		dest.writeString(this.imageDirectory);
		dest.writeInt(this.mode);
		dest.writeInt(this.limit);
		dest.writeLong(this.sizeLimit);
		dest.writeByte(this.folderMode ? (byte) 1 : (byte) 0);
		dest.writeByte(this.showCamera ? (byte) 1 : (byte) 0);
		dest.writeByte(this.returnAfterFirst ? (byte) 1 : (byte) 0);
		dest.writeInt(this.mediaType);
	}

	private long totalFileSize() {
		long ttlSize = 0;
		for (MediaFile file : getSelectedFiles()) {
			ttlSize += file.getSize();
		}
		return ttlSize;
	}

	public boolean isOverLimit() {
		return getSelectedFiles().size() >= getLimit();
	}

	public boolean isOverLimitSize() {
		return totalFileSize() > getSizeLimit();
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
}
