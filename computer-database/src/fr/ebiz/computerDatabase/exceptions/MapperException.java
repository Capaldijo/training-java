package fr.ebiz.computerDatabase.exceptions;

public class MapperException extends Exception {

    private static final long serialVersionUID = 1L;

    public MapperException(String msg) {
        super("[MAPPER] " + msg);
    }
}
