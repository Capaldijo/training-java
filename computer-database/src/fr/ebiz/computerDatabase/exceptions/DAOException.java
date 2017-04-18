package fr.ebiz.computerDatabase.exceptions;

public class DAOException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DAOException(String msg) {
		super("[DAO] " + msg);
	}
}
