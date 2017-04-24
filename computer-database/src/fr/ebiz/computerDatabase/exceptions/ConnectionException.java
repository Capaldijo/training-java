package fr.ebiz.computerDatabase.exceptions;

public class ConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     *  Constructor.
     * @param msg to print when cough
     */
    public ConnectionException(String msg) {
        super("[CONNECTION] " + msg);
    }
}
