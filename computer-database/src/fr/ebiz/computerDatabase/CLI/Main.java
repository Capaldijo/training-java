package fr.ebiz.computerDatabase.CLI;

import java.sql.SQLException;

import fr.ebiz.computerDatabase.controller.CLIController;
import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;

public class Main {

    /**
     * Main method.
     * @param args .
     */
    public static void main(String[] args) {
        CLIController controller;
        try {
            controller = new CLIController();
            controller.init();
        } catch (ConnectionException | DAOException | MapperException | SQLException e) {
        }
    }

}
