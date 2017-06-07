package fr.ebiz.computerdatabase.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.view.Cli;
import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.util.Utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CLIController {

    private static final int PAGEABLE_NB_LINE = 10;

    private static final Logger LOG = LoggerFactory.getLogger(CLIController.class);

    private boolean shouldKeepGoin = true;

    private Cli view;

    private DateTimeFormatter formatter;

    private static final String URI_API = "http://localhost:8080/api/";

    private static final String URI_API_COMPUTER = URI_API + "computers";

    private static final String URI_API_COMPANY = URI_API + "companies";

    /**
     * Constructor CLIController.
     */
    public CLIController() {
        formatter = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
        view = new Cli();
    }

    /**
     * Init method.
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
     */
    private void createComputer() {
        Client client = getAuthenticatedClient("toto", "toto");

        view.print("\n---- Create a Computer ----");

        String name = view.getStringChoice("\nEnter a name:");
        LocalDate intro = this.stringToDate("\nIntroduced date:");
        LocalDate discon = this.stringToDate("\nDiscontinued date:");

        // discontinued date can not be before introduced one
        while ((intro != null && discon != null) && discon.isBefore(intro)) {
            view.print("\nDiscontinued date can not be before introduce one.");
            discon = this.stringToDate("\nEnter the discontinued date again:");
        }
        String compIdRef = view.getStringChoice("\nEnter a company id reference (for a null company: 0): ");

        CompanyDTO  companyDTO = new CompanyDTO(compIdRef, null);

        ComputerDTO computer = new ComputerDTO.Builder(name)
                .introduced(intro != null ? intro.format(formatter) : "")
                .discontinued(discon != null ? discon.format(formatter) : "")
                .company(companyDTO).build();

        if (computer != null) {
            Response response = client.target(URI_API_COMPUTER + "/")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(computer, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                view.print("Computer created.");
            } else {
                view.print("Error on Computer created. " + response.getStatus());
            }
        }
    }

    /**
     * Get all the data from computer 10 by 10 let choose the user to previous
     * or next ou quit.
     */
    private void pageableListComputer() {
        Client client = getAuthenticatedClient("toto", "toto");
        int numPage = 0;
        List<ComputerDTO> list = null;
        boolean stop = false;
        while (!stop) {
            // get 10 computers in the list
            list = client.target(URI_API_COMPUTER + "/pages/" + numPage + "/lines/" + PAGEABLE_NB_LINE)
                    .request(MediaType.APPLICATION_JSON).get(new GenericType<List<ComputerDTO>>() {

                    });
            switch (view.printPageableList(list)) {
                case 1: // Previous Page
                    if (numPage > 0) {
                        numPage -= 1;
                    } else {
                        LOG.info("Previous was selected but already at first page.");
                    }
                    break;
                case 2: // Next Page
                    if (list != null && list.size() == PAGEABLE_NB_LINE) {
                        numPage += 1;
                    } else {
                        LOG.info("Next was selected but Computer's list is Empty, getting back to last page.");
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
            Client client = getAuthenticatedClient("toto", "toto");

            int numPage = 0;
            List<CompanyDTO> list = null;
            boolean stop = false;
            while (!stop) {
                // get 10 companies in the list
                list = client.target(URI_API_COMPANY + "/pages/" + numPage + "/lines/" + PAGEABLE_NB_LINE)
                        .request(MediaType.APPLICATION_JSON).get(new GenericType<List<CompanyDTO>>() {

                        });
                switch (view.printPageableList(list)) {
                    case 1: // Previous Page
                        if (numPage > 0) {
                            numPage -= 1;
                        } else {
                            LOG.info("Previous was selected but already at first page.");
                        }
                        break;
                    case 2: // Next Page
                        if (list != null && list.size() == PAGEABLE_NB_LINE) {
                            numPage += 1;
                        } else {
                            LOG.info("Next was selected but Computer's list is Empty, getting back to last page.");
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
     */
    private void showDetails() {
        Client client = getAuthenticatedClient("toto", "toto");

        String id = String.valueOf(view.printShowDetailsAction());

        ComputerDTO computer = null;
        /* ------ GET COMPUTER BY ID ----- */
        Response response = client.target(URI_API_COMPUTER + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            view.print(response.readEntity(ComputerDTO.class).toString());
        } else {
            view.print("Error. Computer not found. " + response.getStatus());
        }
    }

    /**
     * Update a computer, ask the user which computer he wants to update with
     * which fields.
     */
    private void updateComputer() {
        Client client = getAuthenticatedClient("toto", "toto");

        String idComputer = view.getStringChoice("\nChoose a computer id to update: ");

        // find the computer chosen
        ComputerDTO computer = client.target(URI_API_COMPUTER + "/" + idComputer)
                .request(MediaType.APPLICATION_JSON)
                .get(ComputerDTO.class);

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
            choice = null;
            do {
                choice = view.getStringChoice("\nDo you want to change the introduced date ?");
                if (choice.toLowerCase().equals("yes")) {
                    intro = this.stringToDate("\nEnter a new introduced date:");
                    computer.setIntroduced(intro != null ? intro.format(formatter) : "");
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
                        computer.setDiscontinued(discon != null ? discon.format(formatter) : "");
                    }
                }
            } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
        } while ((intro != null && discon != null) && intro.isAfter(discon));

        choice = null;
        do {
            choice = view.getStringChoice("\nDo you want to change the company ref id ?");
            if (choice.toLowerCase().equals("yes")) {
                String compIdRef = view.getStringChoice("\nEnter a new company ref id (for a null company: 0):");
                CompanyDTO  companyDTO = new CompanyDTO(compIdRef, null);
                computer.setCompany(companyDTO);
            }
        } while (choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));

        if (computer != null) {
            Response response = client.target(URI_API_COMPUTER + "/" + idComputer)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(computer, MediaType.APPLICATION_JSON), Response.class);
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                view.print("Computer updated.");
            } else {
                view.print("Error on Computer updated. " + response.getStatus());
            }
        }
    }

    /**
     * Delete the company that the user chose.
     */
    private void deleteCompany() {
        Client client = getAuthenticatedClient("toto", "toto");
        // ask the user to chose an id
        String id = String.valueOf(view.printDeleteCompanyAction());
        try {
            Response response = client.target(URI_API_COMPANY + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .delete(Response.class);
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                view.print("Company updated.");
            } else {
                view.print("Error on Company updated. " + response.getStatus());
            }
        } catch (RuntimeException e) {
            view.print("\nError on deleting company");
            LOG.error("Error on delete company");
        }
    }

    /**
     * Delete the computer that the user chose.
     */
    private void deleteComputer() {
        Client client = getAuthenticatedClient("toto", "toto");
        // ask the user to chose an id
        String id = String.valueOf(view.printDeleteComputerAction());
        try {
            Response response = client.target(URI_API_COMPUTER + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .delete(Response.class);
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                view.print("Computer deleted.");
            } else {
                view.print("Error on Computer deleted. " + response.getStatus());
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
    private LocalDate stringToDate(String msg) {
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

    /**
     * Get an authenticated client.
     * @param user username.
     * @param passwd password.
     * @return client.
     */
    private Client getAuthenticatedClient(String user, String passwd) {
        Client client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(user, passwd);
        return client.register(feature);
    }
}
