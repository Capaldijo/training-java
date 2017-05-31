package fr.ebiz.computerdatabase.cli;

import fr.ebiz.computerdatabase.controller.CLIController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    /**
     * Main method.
     * @param args .
     */
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("../../../");
            CLIController controller = (CLIController) context.getBean("cLIController");
            controller.init();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}
