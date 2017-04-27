package fr.ebiz.computerdatabase.controllers;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.CompanyDAO;
import fr.ebiz.computerdatabase.daos.ComputerDAO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.persistence.ConnectionDB;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatabase.view.Cli;
import fr.ebiz.computerdatanase.dtos.CompanyDTO;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

public class CLIController {

    final Logger logger = LoggerFactory.getLogger(CLIController.class);

    private ComputerDAO computerDAO;

    private CompanyDAO companyDAO;

    private boolean shouldKeepGoin = true;

    private Cli view;

    /**
     * Constructor CLIController.
     * @throws ConnectionException error on co to db
     */
    public CLIController() throws ConnectionException {
        computerDAO = new ComputerDAO();
        companyDAO = new CompanyDAO();
        view = new Cli();
    }

    /**
     * Init method.
     * @throws DAOException Error on getting data from db.
     * @throws MapperException Error on mapping data.
     * @throws SQLException Error on getting data.
     */
    public void init() throws DAOException, MapperException, SQLException {

        while (shouldKeepGoin) {

            switch (view.printMenu()) {
            case 1: // list companies
                listCompanies();
                break;
            case 2: // list computers
                listComputer();
                break;
            case 3: // Create a computer
                createComputer();
                break;
            case 4: // quit
                shouldKeepGoin = false;
                view.print("Bye");
                break;
            default:
                view.print("Error top menu");
            }

        } // while(shouldKeepGoin)
        try {
            ConnectionDB.getInstance().closeAll();
        } catch (NullPointerException npe) {
            logger.error("Error on closing to DB.");
        } catch (ConnectionException e) {
            logger.error(e.getMessage());
        }
    } // init

    /**
     * Get all the data from computer 10 by 10 let choose the user to previous
     * or next ou quit.
     */
    private void listCompanies() {
        try {
            int numPage = 0;
            List<CompanyDTO> list = null;
            boolean stop = false;
            while (!stop) {
                // get 10 companies in the list
                list = companyDAO.findByPage(null, numPage, Utils.PAGEABLE_NBLINE);

                // if list is empty bc the user gone to far in pages, get to
                // previous half full list
                if (list.isEmpty() && numPage > 0) {
                    logger.info("Next was selected but Company's list is Empty, getting back to last page.");
                    numPage -= Utils.PAGEABLE_NBLINE;
                    list = companyDAO.findByPage(null, numPage, Utils.PAGEABLE_NBLINE);
                }
                switch (view.printPageableList(list)) {
                case 1: // Previous Page
                    if (numPage > 0) {
                        numPage -= Utils.PAGEABLE_NBLINE;
                    } else {
                        logger.info("Previous was selected but already at first page.");
                    }
                    break;
                case 2: // Next Page
                    if (!list.isEmpty()) {
                        numPage += Utils.PAGEABLE_NBLINE;
                    }
                    break;
                case 3: // Quit
                    stop = true;
                    break;
                default:
                    view.print("Error choice listing Computer.");
                }
            }
        } catch (DAOException e) {
            view.print("Error on Page chosen");
            logger.error("Error on listing's arguments on Table Company");
        }
    }

    /**
     * Only print info about computers: List, Show details, Update or Delete.
     * @throws DAOException Error on getting data from db.
     * @throws MapperException Error on mapping data.
     * @throws SQLException Error on getting data.
     */
    private void listComputer() throws DAOException, MapperException, SQLException {
        /* ------ GET ALL COMPUTER ----- */
        boolean stop = false;
        while (!stop) {
            switch (view.printSubMenuComputers()) {
            case 1:
                // pageable list computer
                pageableListComputer();
                break;
            case 2:
                // show details of a computer
                showDetails();
                break;
            case 3:
                // update computer
                updateComputer();
                break;
            case 4:
                // delete computer
                deleteComputer();
                break;
            case 5:
                // quit
                stop = true;
                break;
            default:
                view.print("Error top menu");

            }
        }
    }

    /**
     * Create a computer, enter its name, introduce and discontinued date, and
     * its referenced company id return the computer constructed by those fields.
     * @throws DAOException Error on getting data to DB.
     */
    private void createComputer() throws DAOException {

        view.print("\n---- Create a Computer ----");

        String name = view.getStringChoice("\nEnter a name:");
        LocalDate intro = this.stringToDate("\nIntroduced date:");
        LocalDate discon = this.stringToDate("\nDiscontinued date:");
        // discontinued date can not be before introduced one
        while ((intro != null && discon != null) && discon.isBefore(intro)) {
            view.print("\nDiscontinued date can not be before introduce one.");
            discon = this.stringToDate("\nEnter the discontinued date again:");
        }
        int compIdRed = view.getIntChoice("\nEnter a company id reference (for a null company: 0): ");

        Computer computer = new Computer.Builder(name).introduced(intro).discontinued(discon)
                .companyId(compIdRed).build();

        if (computer != null) {
            if (computerDAO.insert(computer) == 1) {
                view.print("Insert done");
                logger.info("insert computer done.\n");
            }
        }
    }

    /**
     * Get all the data from computer 10 by 10 let choose the user to previous
     * or next ou quit.
     * @throws DAOException Error on getting data from DB.
     * @throws MapperException Error on mapping data.
     */
    private void pageableListComputer() throws DAOException, MapperException {
        int numPage = 0;
        List<ComputerDTO> list = null;
        boolean stop = false;
        while (!stop) {
            // get 10 computers in the list
            list = computerDAO.findByPage(null, numPage, Utils.PAGEABLE_NBLINE);
            // if list is empty bc the user gone to far in pages, get to
            // previous half full list
            if (list.isEmpty() && numPage > 0) {
                logger.info("Next was selected but Computer's list is Empty, getting back to last page.");
                numPage -= Utils.PAGEABLE_NBLINE;
                list = computerDAO.findByPage(null, numPage, Utils.PAGEABLE_NBLINE);
            }
            switch (view.printPageableList(list)) {
            case 1: // Previous Page
                if (numPage > 0) {
                    numPage -= Utils.PAGEABLE_NBLINE;
                } else {
                    logger.info("Previous was selected but already at first page.");
                }
                break;
            case 2: // Next Page
                if (!list.isEmpty()) {
                    numPage += Utils.PAGEABLE_NBLINE;
                }
                break;
            case 3: // Quit
                stop = true;
                break;
            default:
                view.print("Error choice listing Computer.");

            }
        }
    }

    /**
     * Let the user choose the computer id he wants to get details of and print
     * it.
     * @throws DAOException Error on getting data from DB.
     * @throws MapperException Error on mapping data.
     */
    private void showDetails() throws DAOException, MapperException {

        Long id = (long) view.printShowDetailsAction();

        Computer computer = null;
        Company company = null;
        /* ------ GET COMPUTER BY ID ----- */
        computer = computerDAO.find(id);
        if (computer != null) {
            /* ------ GET COMPANY BY ID ----- */
            company = companyDAO.find(computer.getCompanyId());
            view.print(computer + ", which reference company [" + company + "]");
        }
    }

    /**
     * Update a computer, ask the user which computer he wants to update with
     * which fields.
     * @throws DAOException Error on getting data from DB.
     * @throws MapperException Error on mapping data.
     */
    private void updateComputer() throws DAOException, MapperException {

        Long idComputer = (long) view.getIntChoice("\nChoose a computer id to update: ");

        // find the computer chosen
        Computer computer = computerDAO.find(idComputer);

        // ask the user what to edit
        view.print("Here is the computer's info you want to update:");
        view.print(computer.toString());
        String choice = null;

        String name = null;
        LocalDate intro = null;
        LocalDate discon = null;

        do {
            choice = view.getStringChoice("\nDo you want to change the name ?");
            if (choice.toLowerCase().equals("yes")) {
                name = view.getStringChoice("\nEnter a new name:");
                computer.setName(name);
            }
        } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));

        do { // check in the db if introduced date is before discontinued
            // one
            choice = null;
            do {
                choice = view.getStringChoice("\nDo you want to change the introduced date ?");
                if (choice.toLowerCase().equals("yes")) {
                    intro = this.stringToDate("\nEnter a new introduced date:");
                    computer.setIntroduced(intro);
                }
            } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));

            choice = null;
            do {
                choice = view.getStringChoice("\nDo you want to change the discontinued date ?");
                if (choice.toLowerCase().equals("yes")) {
                    discon = this.stringToDate("\nEnter a new discontinued date:");
                    // if user updated the date
                    if (intro != null) {
                        if (discon != null && discon.isBefore(intro)) {
                            view.print("Discontinued can not be before introduced one");
                            choice = null;
                        }
                        // else we compare the stored date of the computer
                        // in db to the new one
                    } else if (computer.getIntroduced() != null && discon.isBefore(computer.getIntroduced())) {
                        view.print("Discontinued can not be before introduced one");
                        choice = null;
                    } else {
                        computer.setDiscontinued(discon);
                    }
                }
            } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
        } while ((intro != null && discon != null) && intro.isAfter(discon));

        choice = null;
        do {
            choice = view.getStringChoice("\nDo you want to change the company ref id ?");
            if (choice.toLowerCase().equals("yes")) {
                int compIdRef = view.getIntChoice("\nEnter a new company ref id (for a null company: 0):");
                computer.setCompanyId(compIdRef);
            }
        } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));

        if (computer != null) {
            if (computerDAO.update(computer) == 1) {
                view.print("Update success");
                logger.info("Update computer done.\n");
            } else {
                view.print("Update error");
                logger.info("Update computer error.\n");
            }
        }
    }

    /**
     * Delete the computer that the user chose.
     */
    private void deleteComputer() {

        // ask the user to chose an id
        String id = view.printDeleteComputerAction();
        try {
            // if delete success print success
            if (computerDAO.delete(id) == 1) {
                view.print("delete done.");
                logger.info("delete computer done.\n");
            } else {
                view.print("No computer to delete.");
                logger.info("No computer to delete.\n");
            }
        } catch (SQLException | ConnectionException e) {
            view.print("\nError on deleting computer");
            logger.error("Error on delete computer");
        }
    }

    /**
     * Method that ask the user the year, month, day, hour and minute in order
     * to build a LocalDateTime object for the computer.
     * The string parameter is here only here to be print nothing special
     * @param msg print to the user
     * @return the LocalDateTime object built by the user's given answers
     */
    public LocalDate stringToDate(String msg) {
        view.print(msg);
        LocalDate time = null;
        while (time == null) {
            int year = view.getIntChoice("Enter the year (for a null date: 0):");
            if (year == 0) {
                return null;
            }

            while (year < 1970 || year > 2020) {
                view.print("\nYear is too old or too far in the future.");
                year = view.getIntChoice("Enter the year again:");
            }

            int month = view.getIntChoice("\nEnter the month (between 1 to 12):");
            while (month > 12 || month < 1) {
                view.print("\nIncorrect answer. Please choose between 1 to 12.");
                month = view.getIntChoice("Enter the month again:");
            }

            int day = view.getIntChoice("\nEnter the day (between 1 to 31):");
            while (day > 31 || day < 1) {
                view.print("\nIncorrect answer. Please choose between 1 to 31.");
                day = view.getIntChoice("Enter the day again:");
            }

            try {
                time = LocalDate.of(year, month, day);
            } catch (DateTimeException dte) {
                view.print("\nInvalid Date given. Please try again.");
            }
        }
        return time;
    }
}
