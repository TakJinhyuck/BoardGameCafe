package com.my.exception;

public class DuplicatedException extends AddException {
	public DuplicatedException() {}
	
	public DuplicatedException(String message) {
		super(message);
	}
}