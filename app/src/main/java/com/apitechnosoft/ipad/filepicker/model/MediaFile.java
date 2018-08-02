package com.apitechnosoft.ipad.filepicker.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.webkit.MimeTypeMap;


import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.filepicker.AndroidDeviceFile;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.component.ASTFileUtil;
import com.apitechnosoft.ipad.utils.FNObjectUtil;

import java.io.File;
import java.io.FileInputStream;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created 06-06-2017
 *
 * @author AST Inc.
 */
public class MediaFile extends AndroidDeviceFile {

	public static final Parcelable.Creator<MediaFile> CREATOR = new Parcelable.Creator<MediaFile>() {
		@Override
		public MediaFile createFromParcel(Parcel in) {
			return new MediaFile(in);
		}

		@Override
		public MediaFile[] newArray(int size) {
			return new MediaFile[size];
		}
	};
	private String name;
	private long size;
	private int mediaType;
	private boolean isfromCamera;
	private boolean isCropped;
	private String fileName;
	private String compressFilePath;
	private FileInputStream inputStream;

	public MediaFile() {
	}

	public MediaFile(int id, String name, String path) {
		this(id, name, path, false);
	}

	public MediaFile(int id, String name, String path, boolean isSelected) {
		this.setId(id);
		this.name = name;
		this.path = path;
		this.isSelected = isSelected;
	}

	protected MediaFile(Parcel in) {
		this.setId(in.readInt());
		this.name = in.readString();
		this.path = in.readString();
		this.isSelected = in.readByte() != 0;
		mimeType = in.readString();
		size = in.readLong();
		mediaType = in.readInt();
		orientation = in.readInt();
		isfromCamera = in.readByte() != 0;
		isCropped = in.readByte() != 0;
	}

	public static MediaFile deviceFile(Cursor cursor, int position, int mediaType) {
		cursor.moveToPosition(position);
		int fileId = cursor.getInt(cursor.getColumnIndexOrThrow(ASTFileUtil.idKey(mediaType)));
		String path = cursor.getString(cursor.getColumnIndexOrThrow(ASTFileUtil.dataKey(mediaType)));
		String fileName = cursor.getString(cursor.getColumnIndexOrThrow(ASTFileUtil.titleKey(mediaType)));

		MediaFile fileObj = new MediaFile(fileId, fileName, path, false);
		fileObj.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(ASTFileUtil.sizeKey(mediaType))));
		fileObj.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(ASTFileUtil.mimeTypeKey(mediaType))));

		String mediaTypeKey = ASTFileUtil.mediaTypeKey(mediaType);
		fileObj.setMediaType(FNObjectUtil.isNonEmptyStr(mediaTypeKey) ? cursor.getInt(cursor.getColumnIndexOrThrow(mediaTypeKey)) : mediaType);
		String orientationKey = ASTFileUtil.orientationKey(mediaType);
		if (FNObjectUtil.isNonEmptyStr(orientationKey)) {
			fileObj.setOrientation(cursor.getInt(cursor.getColumnIndexOrThrow(orientationKey)));
		}
		return fileObj;
	}

	public static MediaFile deviceFile(Cursor cursor, int mediaType) {
		return deviceFile(cursor, cursor.getPosition(), mediaType);
	}

	public boolean isPhoto() {
		return mediaType == MEDIA_TYPE_IMAGE;
	}

	public boolean isVideo() {
		return mediaType == MEDIA_TYPE_VIDEO;
	}

	public boolean isAudio() {
		return mediaType == MEDIA_TYPE_AUDIO;
	}

	public boolean isDocument() {
		return mediaType == FNFilePicker.MEDIA_TYPE_DOCUMENT;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getTitle() {
		return new File(this.path).getName();
	}

	public @DrawableRes int getResource() {
		switch (mediaType) {
			case MEDIA_TYPE_AUDIO:
				return R.drawable.audio_icon;
			case MEDIA_TYPE_VIDEO:
				return R.drawable.video_icon;
			case MEDIA_TYPE_IMAGE:
				return R.drawable.image_icon;
			default:
				return ASTFileUtil.fileTypeImageResource(getMimeType());
		}
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(getId());
		parcel.writeString(name);
		parcel.writeString(path);
		parcel.writeByte((this.isSelected != null && this.isSelected) ? (byte) 1 : (byte) 0);
		parcel.writeString(mimeType);
		parcel.writeLong(size);
		parcel.writeInt(mediaType);
		parcel.writeInt(orientation);
		parcel.writeByte(this.isfromCamera ? (byte) 1 : (byte) 0);
		parcel.writeByte(this.isCropped ? (byte) 1 : (byte) 0);
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public boolean isfromCamera() {
		return isfromCamera;
	}

	public void setIsfromCamera(boolean isfromCamera) {
		this.isfromCamera = isfromCamera;
	}

	public boolean isCropped() {
		return isCropped;
	}

	public void setCropped(boolean cropped) {
		isCropped = cropped;
	}

	public String getFileName() {
		if (getFilePath() == null) {
			return null;
		}
		if (isEmptyStr(fileName)) {
			String extension = MimeTypeMap.getFileExtensionFromUrl(this.getFilePath().getAbsolutePath());
			fileName = this.getFilePath().getName();
			if (this.isNonEmptyStr(extension)) {
				String fullExt = "." + extension;
				return fileName.replaceAll(fullExt, "");
			}
		}
		return fileName;
	}

	public String getCompressFilePath() {
		return compressFilePath;
	}

	public void setCompressFilePath(String compressFilePath) {
		this.compressFilePath = compressFilePath;
	}
}
