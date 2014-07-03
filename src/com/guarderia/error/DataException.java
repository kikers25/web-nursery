package com.guarderia.error;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DataException extends NestableRuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 68920194841684998L;

	public DataException(final Throwable cause)
	{
		super(cause);	
	}
	
	public DataException(final String msg, final Throwable cause)
	{
		super(msg, cause);	
	}
	
	public DataException(final String msg)
	{
		super(msg);	
	}

}
