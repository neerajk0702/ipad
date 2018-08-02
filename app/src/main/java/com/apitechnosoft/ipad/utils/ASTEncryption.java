package com.apitechnosoft.ipad.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.apitechnosoft.ipad.ApplicationHelper;

/**
 * <h4>Created</h4> 08/03/17
 *
 * @author AST Inc.
 */
public class ASTEncryption {

	protected static final String UTF8 = "utf-8";
	private static final char[] SEKRIT = new char[] { 'z', 'h', 'i', 'u', 'p', 'b', 's', 'w' };

	public static String encrypt(String value) {
		return encrypt(value, ApplicationHelper.application().getContext());
	}

	public static String encrypt(String value, Context context) {

		try {
			final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
			return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), UTF8);

		} catch (Exception e) {
			//FNExceptionUtil.logException(e);
			return null;
		}

	}

	public static String decrypt(String value) {
		return decrypt(value, ApplicationHelper.application().getContext());
	}

	public static String decrypt(String value, Context context) {
		try {
			final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
			return new String(pbeCipher.doFinal(bytes), UTF8);

		} catch (Exception e) {
			//FNExceptionUtil.logException(e);
			return null;
		}
	}
}
