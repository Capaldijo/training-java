package fr.ebiz.computerDatabase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import fr.ebiz.computerDatabase.mapper.CompanyMapper;
import fr.ebiz.computerDatabase.mapper.ComputerMapper;
import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.CompanyDAO;
import fr.ebiz.computerDatabase.persistence.ComputerDAO;
import fr.ebiz.computerDatabase.persistence.ConnectionMYSQL;
import fr.ebiz.computerDatabase.utils.Utils;
import fr.ebiz.computerDatabase.view.Cli;

public class Service {
	
	final Logger logger = LoggerFactory.getLogger(Service.class);

	private ComputerDAO computerDAO;
	
	private CompanyDAO companyDAO;
	
	private ComputerMapper computerMapper;
	
	private CompanyMapper companyMapper;
	
	private boolean shouldKeepGoin = true; 
	
	private Cli view;
	
	public Service() {
		computerDAO = new ComputerDAO();
		companyDAO = new CompanyDAO();
		computerMapper = new ComputerMapper();
		companyMapper = new CompanyMapper();
		view = new Cli();
	}
	
	public void init() {
		
		while(shouldKeepGoin) {
			
				switch(view.printMenu()) {
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
			
		}// while(shouldKeepGoin)
		try {
			ConnectionMYSQL.getInstance().closeAll();
		} catch (SQLException e) {
			view.print("Error on close DB");
			logger.error("Error on close DB");
		} catch (NullPointerException npe){
			logger.error("Error on closing to DB.");
		}
	} // init
	
	/*
	 * Get all the data from computer 10 by 10
	 * let choose the user to previous or next ou quit
	 */
	private void listCompanies() {
		try {
			int numPage = 0;
			ResultSet res = null;
			List<Company> list = null;
			boolean stop = false;
			while(!stop){
				// get 10 companies in the list
				res = companyDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
				list = companyMapper.fromDBToCompanies(res);
				
				// if list is empty bc the user gone to far in pages, get to previous half full list
				if(list.isEmpty() && numPage > 0){
					logger.info("Next was selected but Company's list is Empty, getting back to last page.");
					numPage-=Utils.PAGEABLE_NBLINE;
					res = companyDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
					list = companyMapper.fromDBToCompanies(res);
				}
				switch(view.printPageableList(list)){
					case 1: // Previous Page
						if(numPage > 0)
							numPage-=Utils.PAGEABLE_NBLINE;
						else
							logger.info("Previous was selected but already at first page.");
						break;
					case 2: // Next Page
						if(!list.isEmpty())
							numPage+=Utils.PAGEABLE_NBLINE;
						break;
					case 3: // Quit
						stop = true;
						break;
					default:
						view.print("Error choice listing Computer.");
				}
			}
		} catch (SQLException e) {
			view.print("Error on Page chosen");
			logger.error("Error on listing's arguments on Table Company");
		}
	}
	
	/*
	 * Only print info about computers:
	 * 	List, Show details, Update or Delete
	 */
	private void listComputer() {
		/* ------ GET ALL COMPUTER ----- */
		boolean stop = false;
		while(!stop){
			try {
				switch(view.printSubMenuComputers()){
					case 1:
						// pageable list computer
						pageableListComputer();
						break;
					case 2:
						//show details of a computer
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
			} catch (SQLException e) {
				view.print("Error on request findAll companies");
				logger.error("Error request select all Companies");
			}
		}
	}
	
	/*
	 * Create a computer, enter its name, introduce and 
	 * discontinued date, and its referenced company id
	 * return the computer constructed by those fields
	 */
	private void createComputer() {
		
		view.print("\n---- Create a Computer ----");
		
		String name = view.getStringChoice("\nEnter a name:");
		LocalDateTime intro = this.stringToDate("\nIntroduced date:");
		LocalDateTime discon = this.stringToDate("\nDiscontinued date:");
		//discontinued date can not be before introduced one
		while((intro != null && discon != null) && discon.isBefore(intro)) {
			view.print("\nDiscontinued date can not be before introduce one.");
			discon = this.stringToDate("\nEnter the discontinued date again:");
		}
		int compIdRed = view.getIntChoice("\nEnter a company id reference (for a null company: 0): ");
		
		Computer computer = new Computer(name, intro, discon, compIdRed);
		
		try {
			if(computer != null )
				if(computerDAO.insert(computer) == 1){
					view.print("Insert done");
					logger.info("insert computer done.\n");
				}
		} catch (MySQLIntegrityConstraintViolationException ie){
			view.print("Creating computer failed, field company_id invalid");
			logger.error("[INSERT] Error on getting invalid Company by its ID");
		} catch (SQLException e) {
			view.print("Error on insert");
			e.printStackTrace();
			logger.error("Error inserting Computer");
		}
	}
	
	/*
	 * Get all the data from computer 10 by 10
	 * let choose the user to previous or next ou quit
	 */
	private void pageableListComputer(){
		/*---- LIST COMPUTER WITH PAGEABLE FEATURE -----*/
		try {
			int numPage = 0;
			List<Computer> list = null;
			ResultSet res = null;
			boolean stop = false;
			while(!stop){
				// get 10 computers in the list
				res = computerDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
				list = computerMapper.fromDBToComputers(res);
				// if list is empty bc the user gone to far in pages, get to previous half full list
				if(list.isEmpty() && numPage > 0){
					logger.info("Next was selected but Computer's list is Empty, getting back to last page.");
					numPage-=Utils.PAGEABLE_NBLINE;
					res = computerDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
					list = computerMapper.fromDBToComputers(res);
				}
				switch(view.printPageableList(list)){
					case 1: // Previous Page
						if(numPage > 0)
							numPage-=Utils.PAGEABLE_NBLINE;
						else
							logger.info("Previous was selected but already at first page.");
						break;
					case 2: // Next Page
						if(!list.isEmpty())
							numPage+=Utils.PAGEABLE_NBLINE;
						break;
					case 3: // Quit
						stop = true;
						break;
					default:
						view.print("Error choice listing Computer.");
							
				}
			}
		} catch (SQLException e) {
			view.print("Error on Page choosen");
			logger.error("Error on listing's arguments on Table Computer");
		}
	}
	
	/*
	 * Let the user choose the computer id he wants 
	 * to get details of and print it
	 */
	private void showDetails() {

		int id = view.printShowDetailsAction();
		
		Computer computer = null;
		Company company = null;
		ResultSet res = null;
		try {
			/* ------ GET COMPUTER BY ID ----- */
			res = computerDAO.find(id);
			res.next();
			computer = computerMapper.fromDBToComputer(res);
			if(computer != null){
				/* ------ GET COMPANY BY ID ----- */
				res = companyDAO.find(computer.getCompany_id());
				res.next();
				company = companyMapper.fromDBToCompany(res);
				view.print(computer + ", which reference company [" + company + "]");
			}
				
		} catch (SQLException e) {
			view.print("The computer or the company referenced does not exist.");
			logger.error("[SELECT] Error on getting invalid Company by its ID OR Computer by its ID");
			
		}
	}
	
	/*
	 * Update a computer, ask the user which computer
	 * he wants to update with which fields
	 */
	private void updateComputer() {

		int idComputer = view.getIntChoice("\nChoose a computer id to update: ");
		
		try {			
			// find the computer chosen
			ResultSet res = computerDAO.find(idComputer);
			res.next();
			Computer computer = computerMapper.fromDBToComputer(res);
			
			// ask the user what to edit
			view.print("Here is the computer's info you want to update:");
			view.print(computer.toString());
			String choice = null;
			
			String name = null;
			LocalDateTime intro = null;
			LocalDateTime discon = null;
			
			do{
				choice = view.getStringChoice("\nDo you want to change the name ?");
				if(choice.toLowerCase().equals("yes")){
					name = view.getStringChoice("\nEnter a new name:");
					computer.setName(name);
				}
			}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
			
			do{ // check in the db if introduced date is before discontinued one			
				choice = null;
				do{
					choice = view.getStringChoice("\nDo you want to change the introduced date ?");
					if(choice.toLowerCase().equals("yes")){
						intro = this.stringToDate("\nEnter a new introduced date:");
						computer.setIntroduced(intro);
					}
				}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
				
				choice = null;
				do{
					choice = view.getStringChoice("\nDo you want to change the discontinued date ?");
					if(choice.toLowerCase().equals("yes")){
						discon = this.stringToDate("\nEnter a new discontinued date:");
						// if user updated the date
						if(intro != null){
							if(discon != null && discon.isBefore(intro)){
								view.print("Discontinued can not be before introduced one");
								choice=null;
							}
						// else we compare the stored date of the computer in db to the new one
						}else if(computer.getIntroduced() != null && discon.isBefore(computer.getIntroduced())){
							view.print("Discontinued can not be before introduced one");
							choice=null;
						} else
							computer.setDiscontinued(discon);
					}
				}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
			}while((intro != null && discon != null) && intro.isAfter(discon));
			
			choice = null;
			do{
				choice = view.getStringChoice("\nDo you want to change the company ref id ?");
				if(choice.toLowerCase().equals("yes")){
					int compIdRef = view.getIntChoice("\nEnter a new company ref id (for a null company: 0):");
					computer.setCompany_id(compIdRef);
				}
			}while(choice == null || (!choice.toLowerCase().equals("yes") && !choice.toLowerCase().equals("no")));
			
			if(computer != null)
					if(computerDAO.update(computer) == 1){
						view.print("Update success");
						logger.info("Update computer done.\n");
					} else{
						view.print("Update error");
						logger.info("Update computer error.\n");
					}
					
		} catch (SQLException e) {
			view.print("The computer you try to update does not exist.");
			logger.error("Error selecting a computer that does not exist.");
		}
	}
	
	/*
	 * Delete the computer that the user chose
	 */
	private void deleteComputer() {
		
		// ask the user to chose an id
		int id = view.printDeleteComputerAction();
		try {
			// if delete success print success
			if(computerDAO.delete(id) == 1){
				view.print("delete done.");
				logger.info("delete computer done.\n");
			}
			else{
				view.print("No computer to delete.");
				logger.info("No computer to delete.\n");
			}
		} catch (SQLException e) {
			view.print("\nError on deleting computer");
			logger.error("Error on delete computer");
		}
	}
	
	/*
	 * Method that ask the user the year, month, day,
	 * hour and minute in order to build a LocalDateTime
	 * object for the computer.
	 * 
	 * The string parameter is here only here to be print
	 * nothing special
	 * 
	 * Return the LocalDateTime object built by
	 * the user's given answers
	 */
	public LocalDateTime stringToDate(String msg) {
		view.print(msg);
		LocalDateTime time = null;
		while(time == null) {
			int year = view.getIntChoice("Enter the year (for a null date: 0):");
			if(year == 0)
				return null;
				
			while( year<1970 || year>2020) {
				view.print("\nYear is too old or too far in the future.");
				year = view.getIntChoice("Enter the year again:");
			}
			
			int month = view.getIntChoice("\nEnter the month (between 1 to 12):");
			while( month>12 || month<1) {
				view.print("\nIncorrect answer. Please choose between 1 to 12.");
				month = view.getIntChoice("Enter the month again:");
			}
			
			int day = view.getIntChoice("\nEnter the day (between 1 to 31):");
			while( day>31 || day<1) {
				view.print("\nIncorrect answer. Please choose between 1 to 31.");
				day = view.getIntChoice("Enter the day again:");
			}
			
			int hour = view.getIntChoice("\nEnter the hour (between 0 to 23):");
			while( hour>23 || hour<0) {
				view.print("\nIncorrect answer. Please choose between 0 to 23.");
				hour = view.getIntChoice("Enter the hour again:");
			}
			
			int minute = view.getIntChoice("\nEnter the hour (between 0 to 59):");
			while( minute>59 || minute<0) {
				view.print("\nIncorrect answer. Please choose between 0 to 59.");
				minute = view.getIntChoice("Enter the minute again:");
			}
			
			try{
				time = LocalDateTime.of(year, month, day, hour, minute);
			} catch(DateTimeException dte) {
				view.print("\nInvalid Date given. Please try again.");
			}
		}		
		return time;
	}
	
}// class
