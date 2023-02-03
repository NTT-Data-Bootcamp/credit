package com.bootcamp.java.credit.domain.dto;

public enum ClientDocumentTypeEnum {
	DNI("DNI"), CARNET_EXTRANJERIA("CARNET EXTRANJERIA"), PASAPORTE("PASAPORTE"), RUC("RUC");
	private String value;
	ClientDocumentTypeEnum(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
