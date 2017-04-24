package fr.ebiz.computerDatabase.main;

import java.sql.SQLException;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.service.Service;

public class Main {

    /**
     * Main method.
     * @param args .
     */
    public static void main(String[] args) {
        Service service;
        try {
            service = new Service();
            service.init();
        } catch (ConnectionException | DAOException | MapperException | SQLException e) {
        }
    }

}
