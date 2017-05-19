package fr.ebiz.computerdatabase.controllers;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import fr.ebiz.computerdatabase.interfaces.ICompanyService;
import fr.ebiz.computerdatabase.interfaces.IComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.views.Cli;
import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CLIController {

    private static final Logger LOG = LoggerFactory.getLogger(CLIController.class);

    private final IComputerService computerService;

    private final ICompanyService companyService;

    private boolean shouldKeepGoin = true;

    private Cli view;

    private DateTimeFormatter formatter;

    /**
     * Constructor CLIController.
     * @param computerService .
     * @param companyService .
     * @throws ConnectionException error on co to db
     */
    @Autowired
    public CLIController(IComputerService computerService, ICompanyService companyService) {
        formatter = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
        this.computerService = computerService;
        this.companyService = companyService;
        view = new Cli();
    }

    /**
     * Init method.
     * @throws DAOException Error on getting data from db.
     * @throws MapperException Error on mapping data.
     * @throws SQLException Error on getting data.
     */
    public void init() {

        while (shouldKeepGoin) {

            switch (view.printMenu()) {
            case 1: // list companies
                companiesMenu();
                break;
            case 2: // list computers
                computerMenu();
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
    } // init

    /**
     * Get all the data from computer 10 by 10 let choose the user to previous
     * or next ou quit.
     */
    private void companiesMenu() {
        boolean stop = false;
        while (!stop) {
            switch (view.printSubMenuCompanies()) {
            case 1:
                // pageable list computer
                pageableListCompanies();
                break;
            case 2:
                // delete computer
                deleteCompany();
                break;
            case 3:
                // quit
                stop = true;
                break;
            default:
                view.print("Error top menu");
            }
        }
    }

    /**
     * Only print info about computers: List, Show details, Update or Delete.
     * @throws DAOException Error on getting data from db.
     * @throws MapperException Error on mapping data.
     * @throws SQLException Error on getting data.
     */
    private void computerMenu() {
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
    private void createComputer() {

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

        ComputerDTO computer = new ComputerDTO.Builder(name)
                .introduced(intro.format(formatter))
                .discontinued(discon.format(formatter))
                .companyId(String.valueOf(compIdRed)).build();

        if (computer != null) {
            if (computerService.add(computer) == 1) {
                view.print("Insert done");
                LOG.info("insert computer done.\n");
            }
        }
    }

    /**
     * Get all the data from computer 10 by 10 let choose the user to previous
     * or next ou quit.
     */
    private void pageableListComputer() {
        int numPage = 0;
        List<ComputerDTO> list = null;
        boolean stop = false;
        while (!stop) {
            // get 10 computers in the list
            PaginationFilters filter = new PaginationFilters.Builder().build();
            list = computerService.getByPage(String.valueOf(numPage), String.valueOf(Utils.PAGEABLE_NBLINE), filter);
            // if list is empty bc the user gone to far in pages, get to
            // previous half full list
            if (list.isEmpty() && numPage > 0) {
                LOG.info("Next was selected but Computer's list is Empty, getting back to last page.");
                numPage -= Utils.PAGEABLE_NBLINE;
                list = computerService.getByPage(String.valueOf(numPage), String.valueOf(Utils.PAGEABLE_NBLINE), filter);
            }
            switch (view.printPageableList(list)) {
            case 1: // Previous Page
                if (numPage > 0) {
                    numPage -= Utils.PAGEABLE_NBLINE;
                } else {
                    LOG.info("Previous was selected but already at first page.");
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
     * Get all the data from company 10 by 10 let choose the user to previous
     * or next ou quit.
     */
    public void pageableListCompanies() {
        try {
            int numPage = 0;
            List<CompanyDTO> list = null;
            boolean stop = false;
            while (!stop) {
                // get 10 companies in the list
                list = companyService.getByPage(String.valueOf(numPage), String.valueOf(Utils.PAGEABLE_NBLINE));

                // if list is empty bc the user gone to far in pages, get to
                // previous half full list
                if (list.isEmpty() && numPage > 0) {
                    LOG.info("Next was selected but Company's list is Empty, getting back to last page.");
                    numPage -= Utils.PAGEABLE_NBLINE;
                    list = companyService.getByPage(String.valueOf(numPage), String.valueOf(Utils.PAGEABLE_NBLINE));
                }
                switch (view.printPageableList(list)) {
                case 1: // Previous Page
                    if (numPage > 0) {
                        numPage -= Utils.PAGEABLE_NBLINE;
                    } else {
                        LOG.info("Previous was selected but already at first page.");
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
        } catch (RuntimeException e) {
            view.print(e.getMessage());
            LOG.error("Error on listing's arguments on Table Company");
        }
    }

    /**
     * Let the user choose the computer id he wants to get details of and print
     * it.
     * @throws DAOException Error on getting data from DB.
     * @throws MapperException Error on mapping data.
     */
    private void showDetails() {

        String id = String.valueOf(view.printShowDetailsAction());

        ComputerDTO computer = null;
        CompanyDTO company = null;
        /* ------ GET COMPUTER BY ID ----- */
        computer = computerService.get(id);
        if (computer != null) {
            /* ------ GET COMPANY BY ID ----- */
            company = companyService.get(computer.getCompanyId());
            view.print(computer + ", which reference company [" + company + "]");
        }
    }

    /**
     * Update a computer, ask the user which computer he wants to update with
     * which fields.
     * @throws DAOException Error on getting data from DB.
     * @throws MapperException Error on mapping data.
     */
    private void updateComputer() {

        String idComputer = String.valueOf(view.getIntChoice("\nChoose a computer id to update: "));

        // find the computer chosen
        ComputerDTO computer = computerService.get(idComputer);

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
                    computer.setIntroduced(intro.format(formatter));
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
                    } else if (computer.getIntroduced() != null && discon.isBefore(LocalDate.parse(computer.getIntroduced(), formatter))) {
                        view.print("Discontinued can not be before introduced one");
                        choice = null;
                    } else {
                        computer.setDiscontinued(discon.format(formatter));
                    }
                }
            } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
        } while ((intro != null && discon != null) && intro.isAfter(discon));

        choice = null;
        do {
            choice = view.getStringChoice("\nDo you want to change the company ref id ?");
            if (choice.toLowerCase().equals("yes")) {
                int compIdRef = view.getIntChoice("\nEnter a new company ref id (for a null company: 0):");
                computer.setCompanyId(String.valueOf(compIdRef));
            }
        } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));

        if (computer != null) {
            if (computerService.update(computer) == 1) {
                view.print("Update success");
                LOG.info("Update computer done.\n");
            } else {
                view.print("Update error");
                LOG.info("Update computer error.\n");
            }
        }
    }

    /**
     * Delete the company that the user chose.
     */
    private void deleteCompany() {

        // ask the user to chose an id
        String id = String.valueOf(view.printDeleteCompanyAction());
        try {
            computerService.deleteFromCompanyId(id);
            // if delete success print success
            if (companyService.delete(id) == 1) {
                view.print("delete done.");
                LOG.info("delete company done.\n");
            } else {
                view.print("No company to delete.");
                LOG.info("No company to delete.\n");
            }
        } catch (DAOException | SQLException | RuntimeException e) {
            view.print("\nError on deleting company");
            LOG.error("Error on delete company");
        }
    }

    /**
     * Delete the computer that the user chose.
     */
    private void deleteComputer() {

        // ask the user to chose an id
        String id = String.valueOf(view.printDeleteComputerAction());
        try {
            // if delete success print success
            if (computerService.delete(id) == 1) {
                view.print("delete done.");
                LOG.info("delete computer done.\n");
            } else {
                view.print("No computer to delete.");
                LOG.info("No computer to delete.\n");
            }
        } catch (RuntimeException e) {
            view.print("\nError on deleting computer");
            LOG.error("Error on delete computer");
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
