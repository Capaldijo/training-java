package fr.ebiz.computerDatabase.exceptions;

public class ServiceException extends Exception {
    
	private static final long serialVersionUID = 1L;

	public ServiceException(String msg) {
		super("[SERVICE] " + msg);
	}
}
