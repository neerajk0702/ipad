package com.apitechnosoft.ipad.utils;

/**
 * <h4>Created</h4> 01/18/16
 *
 * @author AST Inc.
 */
public interface ASTRespUtilInterface {

	boolean isCustomMsgExists();

	String msgForAjaxId();

	boolean warningAsError(String actionId);

	void addToMap(String key, Object value);

	boolean showMessageOnError(String actionId);
}
