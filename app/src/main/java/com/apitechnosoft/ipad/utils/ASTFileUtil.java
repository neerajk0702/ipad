package com.apitechnosoft.ipad.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Base64InputStream;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.ASTGson;
import com.apitechnosoft.ipad.R;


import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * <h4>Created</h4> 02/02/16
 *
 * @author AST Inc.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ASTFileUtil {

	static Map<String, String> fileTypesMap;
	static String baseFileTypes = "|image|video|audio|text|";

	public static String fileTypeIcon(String mimeString) {
		return ASTStringUtil.getStringForID(fileTypeIconId(fileIconId(mimeString)));
	}

	public static Drawable fileTypeImage(String mimeString) {
		return ASTUIUtil.getDrawable(fileTypeImageId(fileIconId(mimeString)));
	}

	public static @DrawableRes
	int fileTypeImageResource(String mimeString) {
		return fileTypeImageId(fileIconId(mimeString));
	}

	private static String fileIconId(String mimeString) {
		String iconId = ASTEnum.FILE_UNKNOWN.toString();
		if (ASTObjectUtil.isNonEmptyStr(mimeString)) {
			String[] mimeStringArr = mimeString.split("/");
			if (mimeStringArr.length >= 2) {
				iconId = baseFileTypes.contains(mimeStringArr[0]) ? fileTypeString(mimeStringArr[0]) : fileTypeString(mimeStringArr[1]);
			} else {
				iconId = mimeString;
			}
		}
		return fileTypeString(iconId);
	}

	public static int fileTypeImageId(String iconId) {
		if (ASTObjectUtil.isEmptyStr(iconId)) {
			return R.drawable.unknown_file;
		}
		ASTEnum fileType = ASTEnum.fromID(iconId);
		if (fileType == null) {
			return R.drawable.unknown_file;
		}
		switch (fileType) {
			case IMAGE:
				return R.drawable.image_icon;
			case AUDIO:
				return R.drawable.audio_icon;
			case VIDEO:
				return R.drawable.video_icon;
			case WORD:
				return R.drawable.docx_icon;
			case EXCEL:
				return R.drawable.excel_icon;
			case PPT:
				return R.drawable.ppt_icon;
			case PDF:
				return R.drawable.pdf_icon;
			case TEXT:
				return R.drawable.text_icon;
			default:
				return R.drawable.unknown_file;
		}
	}

	public static int fileTypeIconId(String iconId) {
		if (ASTObjectUtil.isEmptyStr(iconId)) {
			return R.string.icon_file_unknown;
		}
		ASTEnum fileType = ASTEnum.fromID(iconId);
		if (fileType == null) {
			return R.drawable.unknown_file;
		}
		switch (fileType) {
			case IMAGE:
				return R.string.icon_file_image;
			case AUDIO:
				return R.string.icon_file_audio;
			case VIDEO:
				return R.string.icon_file_video;
			case WORD:
				return R.string.icon_file_word;
			case EXCEL:
				return R.string.icon_file_excel;
			case PPT:
				return R.string.icon_file_ppt;
			case PDF:
				return R.string.icon_file_pdf;
			case ZIP:
				return R.string.icon_file_image;
			case TEXT:
				return R.string.icon_file_text;
			default:
				return R.string.icon_file_unknown;
		}
	}

	public static String fileTypeString(String mimeString) {
		if (ASTObjectUtil.isEmptyStr(mimeString)) {
			return ASTEnum.FILE_UNKNOWN.toString();
		}
		String[] mimeStringArr = mimeString.split("/");
		if (fileTypesMap == null) {
			fileTypesMap = new HashMap<>();
			fileTypesMap.put(ASTEnum.PDF.toString(), "pdf");
			fileTypesMap.put(ASTEnum.EXCEL.toString(), "|vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.openxmlformats-officedocument.spreadsheetml.template|csv|comma-separated-values|vnd.msexcel|excel|");
			fileTypesMap.put(ASTEnum.PPT.toString(),
					"|mspowerpoint|vnd.ms-powerpoint|vnd.openxmlformats-officedocument.presentationml.presentation|vnd.openxmlformats-officedocument.presentationml.template|vnd.openxmlformats-officedocument.presentationml.slideshow|");
			fileTypesMap.put(ASTEnum.TEXT.toString(), "|rtf|x-rtf|richtext|text");
			fileTypesMap.put(ASTEnum.WORD.toString(), "|msword|vnd.openxmlformats-officedocument.wordprocessingml.document|vnd.openxmlformats-officedocument.wordprocessingml.template|");
			fileTypesMap.put(ASTEnum.ZIP.toString(), "|x-compressed|x-compress|x-rar-compressed|zip|x-tar|x-compressed-zip|");
			fileTypesMap.put(ASTEnum.IMAGE.toString(), "image");
			fileTypesMap.put(ASTEnum.AUDIO.toString(), "audio");
			fileTypesMap.put(ASTEnum.VIDEO.toString(), "video");
		}
		for (Map.Entry<String, String> entry : fileTypesMap.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(mimeStringArr[0]) || entry.getValue().contains(mimeStringArr[0])) {
				return entry.getKey();
			}
			if (mimeStringArr.length > 1 && (entry.getKey().equalsIgnoreCase(mimeStringArr[1]) ||
					entry.getValue().contains(mimeStringArr[1]))) {
				return entry.getKey();
			}
		}
		return ASTEnum.FILE_UNKNOWN.toString();
	}

	public static String fileTypeString(File file) {
		String mimeString = mimeType(file);
		return fileTypeString(mimeString);
	}

	public static String mimeType(File file) {
		String extension = file != null ? MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath()) : null;
		if (extension == null) {
			return ASTEnum.FILE_UNKNOWN.toString();
		}
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase(Locale.ENGLISH));
	}

	public static String mimeType(String fileName) {
		if (ASTObjectUtil.isEmptyStr(fileName)) {
			return ASTEnum.FILE_UNKNOWN.toString();
		}
		int index = fileName.lastIndexOf(".");
		String extension = index < 0 ? fileName : fileName.substring(index);
		String result = extension.replaceAll("[-+.^:,]", "");
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(result.toLowerCase(Locale.ENGLISH));
		return ASTObjectUtil.isNonEmptyStr(mimeType) ? mimeType : ASTEnum.FILE_UNKNOWN.toString();
	}

	public static void openFile(File file, String mimeString) {
		Uri uri;
		Intent viewIntent = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			uri = Uri.fromFile(file);
		} else {
			uri = FileProvider.getUriForFile(ApplicationHelper.application().getContext(),
                    ApplicationHelper.application().getPackageName() + ".imagepicker.provider", file);
			viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		}
		String fileType = ASTObjectUtil.isNonEmptyStr(mimeString) ? mimeString : mimeType(file);
		viewIntent.setDataAndType(uri, fileType);
		Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
        ApplicationHelper.application().getActivity().startActivity(chooserIntent);
	}

	public static File decodeAndWriteISToFile(InputStream inputStream, File localFile) {
		try {
			skipExtraInfo(inputStream);
			File tempFile = writeTempFile(inputStream);
			return decodeAndWriteOriginalFile(tempFile, localFile);
		} catch (IOException e) {
			if (localFile.exists()) {
				localFile.delete();
			}
			e.printStackTrace();
		}
		return null;
	}

	public static File writeTempFile(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}
		File tempFile = null;
		try {
			tempFile = createTempFile();
			if (tempFile == null) {
				return null;
			}
			writeFile(inputStream, tempFile);
			return tempFile;
		} catch (IOException e) {
			if (tempFile != null) {
				tempFile.delete();
			}
			e.printStackTrace();
		}
		return null;
	}

	private static File decodeAndWriteOriginalFile(File tempBase64File, File originalFile) throws IOException {
		if (tempBase64File == null || originalFile == null) {
			return null;
		}
		if (!originalFile.exists()) {
			originalFile.createNewFile();
		}
		Base64InputStream base64InputStream = new Base64InputStream(new FileInputStream(tempBase64File), Base64.DEFAULT);
		writeFile(base64InputStream, originalFile);
		tempBase64File.delete();
		return originalFile;
	}

	public static void writeFile(InputStream inputStream, File outputFile) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		writeToStream(inputStream, fileOutputStream, true);
	}

	public static void writeToStream(InputStream inputStream, OutputStream outputStream, boolean isCloseStreams) throws IOException {
		byte[] buffer = new byte[1048576]; // 1 MB Buffer
		int count;
		while ((count = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, count);
		}
		outputStream.flush();
		if (isCloseStreams) {
			outputStream.close();
			inputStream.close();
		}
	}

	public static void skipExtraInfo(InputStream inputStream) throws IOException {
		while (true) {
			if (inputStream.read() == ',') {
				break;
			}
		}
	}

	public static File createTempFile() throws IOException {
		File file = new File(Environment.getExternalStorageDirectory(), "tempBase64File.partial");
		if (file.exists()) {
			file.delete();
		}
		boolean isCreated = file.createNewFile();
		return isCreated ? file : null;
	}

	public static long getFileSize(Object fileObj) {
		if (fileObj == null) {
			return -1;
		}
		if (fileObj instanceof Bitmap) {
			return ((Bitmap) fileObj).getByteCount();
		} else if (fileObj instanceof File) {
			return ((File) fileObj).length();
		} else {
			return fileObj.toString().length();
		}
	}

	public static File safeFile(String path) {
		if (ASTObjectUtil.isEmptyStr(path)) {
			return null;
		}
		try {
			return new File(path);
		} catch (Exception ignored) {
			return null;
		}
	}

	public static String getFileContent(String fileName) {
		return streamToString(getFileFromAsset(fileName));
	}

	public static String streamToString(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			StringBuilder fileContent = new StringBuilder();
			String str;
			while ((str = bufferReader.readLine()) != null) {
				fileContent.append(str.trim());
			}
			return fileContent.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> ArrayList<T> objectListFromFile(String fileName, Class<T> entity) {
		String result = streamToString(getFileFromAsset(fileName));
		if (ASTObjectUtil.isNonEmptyStr(result)) {
			return ASTGson.store().getList(entity, result);
		}
		return new ArrayList<>();
	}

	public static <T> T objectFromFile(String fileName, Class<T> entity) {
		String result = streamToString(getFileFromAsset(fileName));
		if (ASTObjectUtil.isNonEmptyStr(result)) {
			return ASTGson.store().getObject(entity, result);
		}
		return null;
	}

	public static InputStream getFileFromAsset(String fileName) {
		try {
			if (ASTObjectUtil.isNonEmptyStr(fileName)) {
				return ApplicationHelper.application().getContext().getAssets().open(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void deleteDirectoryTree(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory()) {
			for (File child : fileOrDirectory.listFiles()) {
				deleteDirectoryTree(child);
			}
		}
		try {
			fileOrDirectory.delete();
		} catch (Exception e) {
			//FNExceptionUtil.logException(e);
		}
	}

	public static String idKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media._ID;
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media._ID;
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media._ID;
			default:
				return MediaStore.Files.FileColumns._ID;
		}
	}

	public static String dataKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media.DATA;
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.DATA;
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media.DATA;
			default:
				return MediaStore.Files.FileColumns.DATA;
		}
	}

	public static String mimeTypeKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media.MIME_TYPE;
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.MIME_TYPE;
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media.MIME_TYPE;
			default:
				return MediaStore.Files.FileColumns.MIME_TYPE;
		}
	}

	public static String titleKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media.TITLE;
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.TITLE;
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media.TITLE;
			default:
				return MediaStore.Files.FileColumns.TITLE;
		}
	}

	public static String sizeKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media.SIZE;
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.SIZE;
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media.SIZE;
			default:
				return MediaStore.Files.FileColumns.SIZE;
		}
	}

	public static String orientationKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.ORIENTATION;
			default:
				return null;
		}
	}

	public static String mediaTypeKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
			case MEDIA_TYPE_IMAGE:
			case MEDIA_TYPE_AUDIO:
				return null;
			default:
				return MediaStore.Files.FileColumns.TITLE;
		}
	}

	public static String dateAddedKey(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media.DATE_ADDED;
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.DATE_ADDED;
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media.DATE_ADDED;
			default:
				return MediaStore.Files.FileColumns.DATE_ADDED;
		}
	}

	public static Uri contentUri(int mediaType) {
		switch (mediaType) {
			case MEDIA_TYPE_IMAGE:
				return MediaStore.Images.Media.getContentUri("external");
			case MEDIA_TYPE_AUDIO:
				return MediaStore.Audio.Media.getContentUri("external");
			case MEDIA_TYPE_VIDEO:
				return MediaStore.Video.Media.getContentUri("external");
			default:
				return MediaStore.Files.getContentUri("external");
		}
	}

	/*public static void fnPrintLn(String msg) {
		String fileName =ApplicationHelper.application().appNameWithoutSpace()+ ".txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File(ApplicationHelper.application().appDir(), fileName);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(msg + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}*/
}