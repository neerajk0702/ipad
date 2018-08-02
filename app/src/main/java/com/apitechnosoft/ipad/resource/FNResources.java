package com.apitechnosoft.ipad.resource;


import com.apitechnosoft.ipad.ApplicationHelper;

/**
 * @author AST Inc.
 */
public class FNResources {

	public static int getId(String resourceName, String defType) {
		return ApplicationHelper.application().getResourceId(resourceName, defType);
	}

	public static final class anim {
		public static int get(String resName) {
			return getId(resName, "anim");
		}
	}

	public static final class attr {
		public static int get(String resName) {
			return getId(resName, "attr");
		}
	}

	public static final class color {
		public static int get(String resName) {
			return getId(resName, "color");
		}
	}

	public static final class dimen {
		public static int get(String resName) {
			return getId(resName, "dimen");
		}
	}

	public static final class drawable {
		public static int get(String resName) {
			return getId(resName, "drawable");
		}
	}

	public static final class id {
		public static int get(String resName) {
			return getId(resName, "id");
		}
	}

	public static final class integer {
		public static int get(String resName) {
			return getId(resName, "integer");
		}
	}

	public static final class layout {
		public static int get(String resName) {
			return getId(resName, "layout");
		}
	}

	public static final class menu {
		public static int get(String resName) {
			return getId(resName, "menu");
		}
	}

	public static final class string {
		public static int get(String resName) {
			return getId(resName, "string");
		}
	}

	public static final class style {
		public static int get(String resName) {
			return getId(resName, "style");
		}
	}

	public static final class styleable {
		public static int get(String resName) {
			return getId(resName, "styleable");
		}
	}
}
