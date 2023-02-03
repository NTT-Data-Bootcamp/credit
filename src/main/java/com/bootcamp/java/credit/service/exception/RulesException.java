package com.bootcamp.java.credit.service.exception;

public class RulesException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public RulesException() {
		super("The data of credit don't comply with the business rules");
	}
}
