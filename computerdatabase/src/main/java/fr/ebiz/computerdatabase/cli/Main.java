package fr.ebiz.computerdatabase.cli;

import java.sql.SQLException;

import fr.ebiz.computerdatabase.controller.CLIController;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;

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
