package fr.ebiz.computerdatabase.cli;

import fr.ebiz.computerdatabase.controllers.CLIController;

public class Main {

    /**
     * Main method.
     * @param args .
     */
    public static void main(String[] args) {
        try {
            CLIController controller = new CLIController();
            controller.init();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}