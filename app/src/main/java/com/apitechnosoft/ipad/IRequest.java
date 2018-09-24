package com.apitechnosoft.ipad;

import java.util.Map;

/**
 * Created 02-11-2017
 *
 * @author Altametrics Inc.
 */
public interface IRequest {

	boolean isShowMessage();

	void setShowMessage(boolean showMessage);

	boolean isShowErrorMessage();

	void setShowErrorMessage(boolean showErrorMessage);

	boolean isHttps();

	String requestMethod();

	boolean doOutput();

	String actionUrl();

	String requestJson();

	String actionID();

	Map<String, Object> requestMap();

	boolean isGetRequest();

	boolean isVerifyHostName();

	boolean isExceptionRequest();

	String getExpMgmtAPIKey();

	String getSessionId();

	boolean isFNResponse();

	String restApiKey();
}
