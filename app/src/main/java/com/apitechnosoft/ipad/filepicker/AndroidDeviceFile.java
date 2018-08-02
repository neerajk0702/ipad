package com.apitechnosoft.ipad.filepicker;

import android.graphics.Bitmap;
import android.net.Uri;

import com.apitechnosoft.ipad.filepicker.helper.ASTImageUtil;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;

import java.io.File;

/**
 * Created 27-06-2017
 *
 * @author AST Inc.
 */
public class AndroidDeviceFile extends FNEntityObject {

	protected transient int id;
	protected transient String path;
	protected transient Uri photoUri;
	protected transient File filePath;
	protected transient int orientation;
	protected transient String mimeType = "image/png";
	protected transient String encodedString;
	protected transient Bitmap photo;
	protected transient boolean isContactPhoto;

	public AndroidDeviceFile() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String photoPath) {
		this.path = photoPath;
	}

	public Uri getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(Uri photoUri) {
		this.photoUri = photoUri;
	}

	public int getOrientation() {
		return orientation == 0 ? ASTImageUtil.getExifRotation(getFilePath()) : orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getEncodedString() {
		return fileDataTypeInfo() + encodedString;
	}

	public void setEncodedString(String encodedString) {
		this.encodedString = encodedString;
	}

	public String fileDataTypeInfo() {
		return "data:" + mimeType + ";base64,";
	}

	public File getFilePath() {
		if (filePath == null) {
			filePath = new File(getPath());
		}
		return filePath;
	}

	public String videoFilePath() {
		return FNFilePicker.SCHEME_VIDEO + ":" + getPath();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MediaFile))
			return false;

		AndroidDeviceFile baseFile = (AndroidDeviceFile) o;

		return getId() == baseFile.getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public boolean isContactPhoto() {
		return isContactPhoto;
	}

	public void setContactPhoto(boolean contactPhoto) {
		isContactPhoto = contactPhoto;
	}
}
