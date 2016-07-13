package com.andrels.anjsonparser.exception;

public class JSONParserFailureException extends Exception {

	private static final long serialVersionUID = -3599443772872210108L;

	public JSONParserFailureException() {
		super();
	}

	public JSONParserFailureException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public JSONParserFailureException(String detailMessage) {
		super(detailMessage);
	}

	public JSONParserFailureException(Throwable throwable) {
		super(throwable);
	}	
}
