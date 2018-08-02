package com.apitechnosoft.ipad.utils;

/**
 * <h4>Created</h4> 05/26/16
 *
 * @author AST Inc.
 */
public enum ASTResponseErrorCode {
	FN000("FN000"), FN001("FN001"), FN002("FN002"), FN003("FN003"), FN004("FN004"), FN005("FN005"), FN500("FN500"), FN501("FN501"), FN502("FN502"), FN503("FN503");
	private final String code;

	ASTResponseErrorCode(String status) {
		this.code = status;
	}

	public static ASTResponseErrorCode fromIID(String iid) {
		for (ASTResponseErrorCode typeIID : ASTResponseErrorCode.values()) {
			if (typeIID.code.equals(iid)) {
				return typeIID;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.code;
	}
}
