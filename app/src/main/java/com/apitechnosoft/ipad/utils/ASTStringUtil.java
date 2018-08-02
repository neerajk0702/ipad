package com.apitechnosoft.ipad.utils;


import com.apitechnosoft.ipad.ApplicationHelper;

/**
 * @author AST Inc.
 */
public class ASTStringUtil {

	public static String getStringForID(int id) {
		return ApplicationHelper.application().getResources().getString(id);
	}

	public static String getStringForID(int id, Object... formatArgs) {
		return ApplicationHelper.application().getResources().getString(id, formatArgs);
	}

	public static String getStringForName(String name) {
		return getStringForID(getIdForName(name));
	}

	public static int getIdForName(String name) {
		return ApplicationHelper.application().getResourceId(name, "string");
	}

	public static int getIdForName(String name, String packageName) {
		return ApplicationHelper.application().getResourceId(name, "string", packageName);
	}
}
