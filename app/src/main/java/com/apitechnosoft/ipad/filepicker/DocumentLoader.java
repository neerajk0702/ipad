package com.apitechnosoft.ipad.filepicker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.utils.ASTEnum;
import com.apitechnosoft.ipad.utils.ASTFileUtil;
import com.apitechnosoft.ipad.utils.FNObjectUtil;

import java.util.ArrayList;

import static android.provider.MediaStore.Files.FileColumns.DATA;
import static android.provider.MediaStore.Files.FileColumns.DATE_ADDED;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static android.provider.MediaStore.Files.FileColumns.MIME_TYPE;
import static android.provider.MediaStore.Files.FileColumns.SIZE;
import static android.provider.MediaStore.Files.FileColumns.TITLE;
import static android.provider.MediaStore.Files.FileColumns._ID;

/**
 * Created 03-07-2017
 *
 * @author AST Inc.
 */
public class DocumentLoader extends AsyncTask<Void, Void, ArrayList<MediaFile>> {

	final String[] DOC_PROJECTION = {
			_ID, DATA, MIME_TYPE, SIZE, DATE_ADDED, TITLE, MEDIA_TYPE
	};

	private final Context context;
	private ASTProgressBar _progressBar;

	public DocumentLoader(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		showProgressDialog();
	}

	@Override
	protected ArrayList<MediaFile> doInBackground(Void... params) {
		ArrayList<MediaFile> documents = new ArrayList<>();
		final String[] projection = DOC_PROJECTION;
		String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
				+ MediaStore.Files.FileColumns.MEDIA_TYPE_NONE + " AND " +
				MediaStore.Files.FileColumns.MIME_TYPE + " IS NOT NULL AND " +
				MediaStore.Files.FileColumns.SIZE + ">0";
		final Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
				projection,
				selection,
				null,
				MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

		if (cursor != null) {
			documents = getDocumentFromCursor(cursor);
			cursor.close();
		}
		return documents;
	}

	@Override
	protected void onPostExecute(ArrayList<MediaFile> mediaFiles) {
		super.onPostExecute(mediaFiles);
		dismissProgressBar();
	}

	private ArrayList<MediaFile> getDocumentFromCursor(Cursor cursor) {
		ArrayList<MediaFile> documents = new ArrayList<>();
		while (cursor.moveToNext()) {
			int fileId = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
			String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
			String fileName = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
			String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MIME_TYPE));
			int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MEDIA_TYPE));
			if (mediaType == MEDIA_TYPE_VIDEO || mediaType == MEDIA_TYPE_AUDIO ||
					mediaType == MEDIA_TYPE_IMAGE || FNObjectUtil.isEmptyStr(mimeType)) {
				continue;
			}
			String type = ASTFileUtil.fileTypeString(mimeType);
			if (ASTEnum.TEXT.isEqual(type) || ASTEnum.PDF.isEqual(type) || ASTEnum.PPT.isEqual(type) ||
					ASTEnum.WORD.isEqual(type) || ASTEnum.EXCEL.isEqual(type) || ASTEnum.ZIP.isEqual(type)) {
				MediaFile fileObj = new MediaFile(fileId, fileName, path, false);
				fileObj.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(SIZE)));
				fileObj.setMimeType(mimeType);
				fileObj.setMediaType(FNFilePicker.MEDIA_TYPE_DOCUMENT);
				documents.add(fileObj);
			}
		}
		return documents;
	}

	protected void showProgressDialog() {
		this._progressBar = new ASTProgressBar(this.context);
		this._progressBar.show();
	}

	protected void dismissProgressBar() {
		try {
			if (this._progressBar != null) {
				this._progressBar.dismiss();
			}
		} catch (Exception e) {
		}
	}
}
