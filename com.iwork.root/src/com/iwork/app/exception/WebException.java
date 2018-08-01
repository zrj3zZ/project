package com.iwork.app.exception;

/**
 * 系统级异常
 * 
 * @author 
 * 
 */
public class WebException extends RuntimeException {

	private static final long serialVersionUID = 5645227164781802573L;

	public WebException(String msg) {
		super(msg);
	}

	public WebException(Throwable t) {
		super(t);
		setStackTrace(t.getStackTrace());
	}

	public WebException(String msg, Throwable t) {
		super(msg, t);
		setStackTrace(t.getStackTrace());
	}

}
