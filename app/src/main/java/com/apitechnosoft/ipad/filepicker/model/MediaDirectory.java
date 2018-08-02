package com.apitechnosoft.ipad.filepicker.model;


import com.apitechnosoft.ipad.FNObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class MediaDirectory extends FNObject {

	private String name;
	private String directory;
	private ArrayList<MediaFile> files;

	public MediaDirectory(String dirBucket, String name) {
		this.directory = dirBucket;
		this.name = name;
		files = new ArrayList<>();
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public ArrayList<MediaFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<MediaFile> files) {
		this.files = files;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File photoCoverPath() {
		if (!isEmpty(files)) {
			return files.get(0).getFilePath();
		}
		return null;
	}

	public String videoCoverPath() {
		if (!isEmpty(files)) {
			return files.get(0).videoFilePath();
		}
		return null;
	}

	public boolean isVideoDir() {
		if (!isEmpty(files))
			return files.get(0).isVideo();
		return false;
	}
}
