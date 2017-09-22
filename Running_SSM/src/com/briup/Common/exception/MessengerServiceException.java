package com.briup.Common.exception;
public class MessengerServiceException extends Exception {

	private static final long serialVersionUID = -1293956586197785052L;

	public MessengerServiceException() {
		super();
	}

	public MessengerServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessengerServiceException(String message) {
		super(message);
	}

	public MessengerServiceException(Throwable cause) {
		super(cause);
	}
}
