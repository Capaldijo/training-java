package fr.ebiz.computerdatabase.mapper;

public class MapperException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param msg to print when cough
     */
    public MapperException(String msg) {
        super("[MAPPER] " + msg);
    }
}
