package com.apitechnosoft.ipad.filepicker;

import com.apitechnosoft.ipad.FNObject;

import java.util.List;
import java.util.Map;

/**
 * @author AST Inc.
 */
public class FNEntityObject extends FNObject {

	public long primaryKey;
	public Boolean isDeleted;
	protected Boolean isSelected;

	public Map<String, Object> objectMapForKeys(List<String> keys) {
		Map<String, Object> valueMap = super.objectMapForKeys(keys);
		if (this.primaryKey > 0) {
			valueMap.put("primaryKey", this.primaryKey);
		}
		return valueMap;
	}

	@Override
	public Map<String, Object> objectMap() {
		Map<String, Object> returnMap = super.objectMap();
		if (this.primaryKey == 0 && returnMap.containsKey("primaryKey")) {
			returnMap.remove("primaryKey");
		}
		return returnMap;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String entityTextDescription() {
		return null;
	}

}
