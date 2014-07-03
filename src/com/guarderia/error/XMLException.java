package com.guarderia.error;

public class XMLException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5579520711688811549L;

	public XMLException() {
	}

	public XMLException(String message) {
		super(message);
	}

	public XMLException(Throwable cause) {
		super(cause);
	}

	public XMLException(String message, Throwable cause) {
		super(message, cause);
	}

}
