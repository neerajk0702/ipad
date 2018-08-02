package com.apitechnosoft.ipad.filepicker.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.exception.FNExceptionUtil;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.filepicker.FNFilePickerConfig;
import com.apitechnosoft.ipad.filepicker.fragment.FNCropImageFragment;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.utils.ASTStringUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_FOLDER_MODE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_IMAGE_DIRECTORY;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_LIMIT;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_MEDIA_TYPE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_MODE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_RETURN_AFTER_FIRST;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_SELECTED_MEDIA;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_SHOW_CAMERA;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_SIZE_LIMIT;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.MAX_LIMIT;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.MODE_MULTIPLE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.SIZE_LIMIT;

/**
 * Created 15-06-2017
 *
 * @author AST Inc.
 */
public class ASTImageUtil {

	public static final float SINGLE_MODE_MAX_HEIGHT = 800.0f;
	public static final float SINGLE_MODE_MAX_WIDTH = 800.0f;
	public static final float MULTI_MODE_MAX_HEIGHT = 600.0f;
	public static final float MULTI_MODE_MAX_WIDTH = 600.0f;
	public static final float PHOTO_MAX_HEIGHT = 100.0f;
	public static final float PHOTO_MAX_WIDTH = 100.0f;

	public static Bitmap getScaledBitmap(Bitmap bitmap, int outWidth, int outHeight) {
		int currentWidth = bitmap.getWidth();
		int currentHeight = bitmap.getHeight();
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.postScale(
				(float) outWidth / (float) currentWidth,
				(float) outHeight / (float) currentHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, currentWidth, currentHeight, scaleMatrix, true);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}
		return inSampleSize;
	}

	public static File createImageFile(String directory) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), directory);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("ASTImageUtil", "Oops! Failed create " + directory + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		String imageFileName = "IMG_" + timeStamp;

		File imageFile = null;
		try {
			imageFile = File.createTempFile(imageFileName, ".jpg", mediaStorageDir);
		} catch (IOException e) {
			Log.d("ASTImageUtil", "Oops! Failed create " + imageFileName + " file");
		}
		return imageFile;
	}

	public static Uri createImageUri(String directory) {
		File imageFile = createImageFile(directory);
		if (imageFile != null) {
			Context appContext = ApplicationHelper.application().getApplicationContext();
			String providerName = String.format(Locale.ENGLISH, "%s%s", appContext.getPackageName(), ".imagepicker.provider");
			return FileProvider.getUriForFile(appContext, providerName, imageFile);
		}
		return null;
	}

	public static String getNameFromFilePath(String path) {
		if (path.contains(File.separator)) {
			return path.substring(path.lastIndexOf(File.separator) + 1);
		}
		return path;
	}

	public static void grantAppPermission(Context context, Intent intent, Uri fileUri) {
		List<ResolveInfo> resolvedIntentActivities = context.getPackageManager()
				.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
			String packageName = resolvedIntentInfo.activityInfo.packageName;
			context.grantUriPermission(packageName, fileUri,
					Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
	}

	public static void revokeAppPermission(Context context, Uri fileUri) {
		context.revokeUriPermission(fileUri,
				Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
	}

	public static Bitmap compressImage(Context context, Uri imageUri) {
		return compressImage(context, imageUri, PHOTO_MAX_WIDTH, PHOTO_MAX_HEIGHT);
	}

	public static Bitmap compressImage(Context context, Uri imageUri, float width, float height) {
		return compressImage(getRealPathFromURI(context, imageUri), 0, width, height);
	}

	public static Bitmap compressImage(String imagePath, int orientation) {
		return compressImage(imagePath, orientation, MULTI_MODE_MAX_WIDTH, MULTI_MODE_MAX_HEIGHT);
	}

	public static Bitmap compressImage(String imagePath, int imgOrientation, float maxWidth, float maxHeight) {
		Bitmap scaledBitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		float imgRatio = (float) actualWidth / (float) actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			bmp = BitmapFactory.decodeFile(imagePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		} catch (Exception exception) {
			FNExceptionUtil.logException(exception);
		}
		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
		Matrix matrix = new Matrix();
		matrix.postRotate(getRotateDegreeFromOrientation(imgOrientation));
		scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		scaledBitmap.compress(Bitmap.CompressFormat.PNG, 85, out);
		return scaledBitmap;
	}

	public static int getRotateDegreeFromOrientation(int orientation) {
		int degree = 0;
		switch (orientation) {
			case ExifInterface.ORIENTATION_UNDEFINED:
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				break;
		}
		return degree;
	}

	public static int getExifRotation(File file) {
		if (file == null)
			return 0;
		try {
			ExifInterface exif = new ExifInterface(file.getAbsolutePath());
			return getRotateDegreeFromOrientation(
					exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_UNDEFINED));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getRealPathFromURI(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String realPath = cursor.getString(column_index);
			cursor.close();
			return realPath;
		}
		return uri.getPath();
	}

	public static int imageSize(Context context, int gridCol) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		int widthPixels = metrics.widthPixels;
		return widthPixels / gridCol;
	}

	public static void openCropFragment(MainActivity activity, MediaFile file) {
		Bundle bundle = new Bundle();
		bundle.putParcelable("imageFile", file);
		bundle.putBoolean(FNFilePicker.EXTRA_SHOW_ROTATE, true);
		bundle.putBoolean(FNFilePicker.EXTRA_SHOW_DONE, true);
		bundle.putString(FNFilePicker.EXTRA_DONE_TEXT, ASTStringUtil.getStringForID(R.string.save));
		activity.updateFragment(new FNCropImageFragment(), bundle, true, false);
	}

	public static String saveToFile(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
		OutputStream outputStream = null;
		Context appContext = ApplicationHelper.application().getApplicationContext();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		String imageFileName = "IMG_" + timeStamp;
		Uri uri = Uri.fromFile(new File(appContext.getCacheDir(), imageFileName));
		final String currentImagePath = ASTImageUtil.getRealPathFromURI(appContext, uri);
		try {
			outputStream = appContext.getContentResolver()
					.openOutputStream(uri);
			if (outputStream != null) {
				bitmap.compress(format, quality, outputStream);
				return currentImagePath;
			}
		} catch (IOException e) {
		//	FNLogUtil.sendToLogger("An error occurred while saving the image: " + uri);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Throwable ignored) {
			}
		}
		return null;
	}

	public static FNFilePickerConfig makeConfigFromIntent(Context context, Intent intent) {
		FNFilePickerConfig config = new FNFilePickerConfig(context);
		config.setMode(intent.getIntExtra(EXTRA_MODE, MODE_MULTIPLE));
		config.setLimit(intent.getIntExtra(EXTRA_LIMIT, MAX_LIMIT));
		config.setSizeLimit(intent.getLongExtra(EXTRA_SIZE_LIMIT, SIZE_LIMIT));
		config.setShowCamera(intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true));
		config.setSelectedFiles(intent.<MediaFile> getParcelableArrayListExtra(EXTRA_SELECTED_MEDIA));
		config.setFolderMode(intent.getBooleanExtra(EXTRA_FOLDER_MODE, true));
		config.setImageDirectory(intent.getStringExtra(EXTRA_IMAGE_DIRECTORY));
		config.setReturnAfterFirst(intent.getBooleanExtra(EXTRA_RETURN_AFTER_FIRST, false));
		config.setMediaType(intent.getIntExtra(EXTRA_MEDIA_TYPE, MEDIA_TYPE_IMAGE));
		return config;
	}
}
