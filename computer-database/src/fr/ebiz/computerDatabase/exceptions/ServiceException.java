package fr.ebiz.computerDatabase.exceptions;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param msg to print when cough
     */
    public ServiceException(String msg) {
        super("[SERVICE] " + msg);
    }
}
