package fr.ebiz.computerdatabase.cli;

import fr.ebiz.computerdatabase.controller.CLIController;
import fr.ebiz.computerdatabase.service.ICompanyService;
import fr.ebiz.computerdatabase.service.IComputerService;
import org.springframework.beans.factory.annotation.Autowired;

public class Main {

    @Autowired
    static IComputerService computerService;

    @Autowired
    static ICompanyService companyService;
    /**
     * Main method.
     * @param args .
     */
    public static void main(String[] args) {
        try {
            CLIController controller = new CLIController(computerService, companyService);
            controller.init();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}
