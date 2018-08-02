package com.apitechnosoft.ipad.helper;

import android.view.View;

/**
 * <h4>Created</h4> 07/29/16
 *
 * @author AST Inc.
 */
public interface IObjectHelper {

	boolean isEmpty(Object o);

	boolean isEmptyStr(String text);

	boolean isNonEmptyStr(String text);

	boolean isEmptyView(View v);

	String getTextFromView(View v);
}
