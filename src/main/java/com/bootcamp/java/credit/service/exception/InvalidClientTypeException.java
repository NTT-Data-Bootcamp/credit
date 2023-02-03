package com.bootcamp.java.credit.service.exception;



public class InvalidClientTypeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidClientTypeException() {
		super("This type of client is invalid");
	}
}
