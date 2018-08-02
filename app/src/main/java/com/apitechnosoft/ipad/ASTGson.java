package com.apitechnosoft.ipad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.apitechnosoft.ipad.exception.FNExceptionUtil;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author AST Inc.
 */
public class ASTGson {

	static ASTGson instance;
	Gson gson;
	Gson gsonExcAnnotation;

	private ASTGson() {
		this.init();
	}

	public static ASTGson store() {
		if (instance == null) {
			instance = new ASTGson();
		}
		return instance;
	}

	private void init() {
		this.gson = new GsonBuilder().create();
	}

	public String toJson(Object obj) {
		return this.gson.toJson(obj);
	}

	public <T> T getObject(Class<T> entity, Object response) {
		if (ASTObjectUtil.isEmpty(response)) {
			return null;
		}
		try {
			String jsonString = response instanceof Map ? this.toJson(response) : ASTJSonHelper.toJSON(response).toString();
			return ASTObjectUtil.isNonEmptyStr(jsonString) ? this.gson.fromJson(jsonString, entity) : null;
		} catch (Exception e) {
			FNExceptionUtil.logException(e, ApplicationHelper.application().getContext(), true);
			return null;
		}
	}

	public <T> ArrayList<T> getList(Class<T> entity, Object response) {
		if (response == null) {
			return new ArrayList<>();
		}
		ArrayList<T> returnList = new ArrayList<>();
		try {
			String jsonsString = response instanceof Iterable ? this.toJson(response) : ASTJSonHelper.toJSON(response).toString();
			ArrayList<Object> list = this.gson.fromJson(jsonsString, new TypeToken<ArrayList<T>>() {
			}.getType());
			for (Object obj : list) {
				returnList.add(this.getObject(entity, obj));
			}
		} catch (Exception e) {
		FNExceptionUtil.logException(e, ApplicationHelper.application().getContext(), true);
		}
		return returnList;
	}

	public <T> ArrayList<T> getList(Object response) {
		if (response == null) {
			return new ArrayList<>();
		}
		try {
			return this.gson.fromJson(ASTJSonHelper.toJSON(response).toString(), new TypeToken<ArrayList<T>>() {
			}.getType());
		} catch (Exception e) {
			FNExceptionUtil.logException(e, ApplicationHelper.application().getContext(), true);
			return null;
		}
	}

	// future R&D
	public <T> Object[] getObjectArray(String response) {
		try {
			return this.gson.fromJson(response, new TypeToken<T[]>() {
			}.getType());
		} catch (Exception e) {
			FNExceptionUtil.logException(e, ApplicationHelper.application().getContext(), true);
			return null;
		}

	}
}
