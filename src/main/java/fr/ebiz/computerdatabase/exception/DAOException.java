package fr.ebiz.computerdatabase.exception;

public class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     *  Constructor.
     * @param msg to print when cough
     */
    public DAOException(String msg) {
        super("[DAO] " + msg);
    }
}
