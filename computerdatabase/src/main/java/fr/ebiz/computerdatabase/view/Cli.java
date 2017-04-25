package fr.ebiz.computerdatabase.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Cli {

    private Scanner sc;

    /**
     * Constructor Cli.
     */
    public Cli() {
        sc = new Scanner(System.in);
    }

    /**
     * Top menu in which we choose between list companies list computers.
     * Create a computer or quit and return the user's choice.
     * @return User's choice.
     */
    public int printMenu() {
        while (true) {
            this.print("\n---- Menu Principal ----");
            this.print("1) List companies\n" + "2) List computers\n" + "3) Create Computer\n" + "4) Quit");
            switch (sc.next()) {
            case "1": // List Companies
                return 1;
            case "2": // List computers
                return 2;
            case "3": // Create computer
                return 3;
            case "4": // Quit
                sc.close();
                return 4;
            default:
                this.print("Please, choose between 1 to 4.");
            }
        }
    }

    /**
     * SubMenu for Computers. User can choose between list them show details of
     * one computer chosen update a computer chosen, delete a computer and
     * return a int depending the user's choice.
     * @return User's choice.
     */
    public int printSubMenuComputers() {
        while (true) {
            this.print("\n---- SubMenu Computer Listing ----");
            this.print("1) List\n" + "2) Show computer details\n" + "3) Update computer\n" + "4) Delete computer\n"
                    + "5) Quit");
            switch (sc.next()) {
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                this.print("Returning to top menu");
                return 5;
            default:
                this.print("Please, choose a correct answer.");
            }
        }
    }

    /**
     * Print all the List's element And ask the user what he wants to do.
     * (Previous, Next, Quit)
     * @param list
     *      List of computer or company
     * @return the user choice
     */
    public int printPageableList(List<?> list) {
        for (Object elmt : list) {
            System.out.println(elmt.toString() + "\n\n--------------------------\n");
        }
        return getIntChoice("1) Previous   | 2) Next    | 3) Quit");
    }

    /**
     * Ask the user to choose the ID of the computer he wants to show details.
     * @return the user's choice
     */
    public int printShowDetailsAction() {
        return this.getIntChoice("\nChoose a computer id to show details: ");
    }

    /**
     * Ask the user to choose the ID of the computer he wants to delete.
     * @return the user's choice
     */
    public int printDeleteComputerAction() {
        return this.getIntChoice("\nChoose a computer id to delete: ");
    }

    /**
     * Only print message when called by other classes because only View
     * supposed to print messages.
     * @param msg
     *      msg to print to user
     */
    public void print(String msg) {
        System.out.println(msg);
    }

    /**
     * Ask the user to enter a answer of type int It keeps asking him if not
     * type int.
     * The string parameter is here only here to be print nothing special
     * @param msg
     *      message to print
     * @return the user's answer
     */
    public int getIntChoice(String msg) {
        this.print(msg);
        boolean correct = false;
        int response = 0;
        while (!correct) {
            try {
                sc = new Scanner(System.in);
                response = sc.nextInt();
                correct = true;
            } catch (InputMismatchException ime) {
                this.print("Please, choose a correct answer.");
            }
        }
        return response;
    }

    /**
     * Ask the user to enter a answer of type String.
     * The string parameter is here only here to be print nothing special.
     * @param msg
     *      msg to print to the user
     * @return the user's answer
     */
    public String getStringChoice(String msg) {
        this.print(msg);
        sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
