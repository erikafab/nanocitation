package it.unipd.dei.nanocitation.util;

public class NoSupportedNpException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSupportedNpException(String errorMessage)
	{
		super(errorMessage);
	}
	
	public NoSupportedNpException(String errorMessage, Throwable err) 
	{
	    super(errorMessage, err);
	}
}
