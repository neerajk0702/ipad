package com.apitechnosoft.ipad.filepicker.listener;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

/**
 * Created 09-06-2017
 *
 * @author AST Inc.
 */
public class VideoRequestHandler extends RequestHandler {

	@Override
	public boolean canHandleRequest(Request data) {
		String scheme = data.uri.getScheme();
		return (FNFilePicker.SCHEME_VIDEO.equals(scheme));
	}

	@Override
	public Result load(Request data, int arg1) throws IOException {
		Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
		return new Result(bm, Picasso.LoadedFrom.DISK);
	}
}
