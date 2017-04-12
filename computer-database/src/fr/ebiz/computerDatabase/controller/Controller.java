package fr.ebiz.computerDatabase.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import fr.ebiz.computerDatabase.mapper.CompanyDAO;
import fr.ebiz.computerDatabase.mapper.ComputerDAO;
import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;
import fr.ebiz.computerDatabase.view.Cli;

public class Controller {
	
	final Logger logger = LoggerFactory.getLogger(Controller.class);

	private ComputerDAO computerDAO;
	
	private CompanyDAO companyDAO;
	
	private boolean shouldKeepGoin = true; 
	
	private Cli view;
	
	public Controller() {
		computerDAO = new ComputerDAO();
		companyDAO = new CompanyDAO();
		view = new Cli();
	}
	
	public void init() {
		
		while(shouldKeepGoin) {
			
			switch(view.printMenu()) {
				case 1: // list companies
					menuListCompanies();
					break;
				case 2: // list computers
					menuListComputer();
					break;
				case 3: // Create a computer
					menuCreateComputer();
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
			DatabaseManager.getInstance().closeAll();
		} catch (SQLException e) {
			view.print("Error on close DB");
			logger.error("Error on close DB");
		}
	} // init
	
	/*
	 * Get all the data from computer 10 by 10
	 * let choose the user to previous or next ou quit
	 */
	private void menuListCompanies() {
		try {
			int numPage = 0;
			List<Company> list = null;
			boolean stop = false;
			while(!stop){
				// get 10 companies in the list
				list = companyDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
				
				// if list is empty bc the user gone to far in pages, get to previous half full list
				if(list.isEmpty() && numPage > 0){
					logger.info("Next was selected but Company's list is Empty, getting back to last page.");
					numPage-=Utils.PAGEABLE_NBLINE;
					list = companyDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
				}
				switch(view.printPageableList(list)){
					case 1:
						if(numPage > 0)
							numPage-=Utils.PAGEABLE_NBLINE;
						logger.info("Previous was selected but already at first page.");
						break;
					case 2:
						if(!list.isEmpty())
							numPage+=Utils.PAGEABLE_NBLINE;
						break;
					case 3:
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
	private void menuListComputer() {
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
						menuShowDetails();
						break;
					case 3:
						// update computer
						menuUpdateComputer();
						break;
					case 4:
						// delete computer
						menuDeleteComputer();
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
	 * let the user create a computer
	 */
	private void menuCreateComputer() {

		Computer computer = view.printInsertComputerAction();
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
			boolean stop = false;
			while(!stop){
				// get 10 computers in the list
				list = computerDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
				
				// if list is empty bc the user gone to far in pages, get to previous half full list
				if(list.isEmpty() && numPage > 0){
					logger.info("Next was selected but Computer's list is Empty, getting back to last page.");
					numPage-=Utils.PAGEABLE_NBLINE;
					list = computerDAO.findByPage(numPage, Utils.PAGEABLE_NBLINE);
				}
				switch(view.printPageableList(list)){
					case 1:
						if(numPage > 0)
							numPage-=Utils.PAGEABLE_NBLINE;
						logger.info("Previous was selected but already at first page.");
						break;
					case 2:
						if(!list.isEmpty())
							numPage+=Utils.PAGEABLE_NBLINE;
						break;
					case 3:
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
	private void menuShowDetails() {

		int id = view.printShowDetailsAction();
		
		Computer computer = null;
		Company company = null;
		try {
			/* ------ GET COMPUTER BY ID ----- */
			computer = computerDAO.find(id);
			if(computer != null){
				/* ------ GET COMPANY BY ID ----- */
				company = companyDAO.find(computer.getCompany_id());
				
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
	private void menuUpdateComputer() {

		int idComputer = view.getIntChoice("\nChoose a computer id to update: ");
		try {
			// find the computer chosen
			Computer computer = computerDAO.find(idComputer);
			// ask the user what to edit
			computer = view.printUpdateComputerAction(computer);
			if(computer != null)
				if(computerDAO.update(computer) == 1){
					view.print("Update success");
					logger.info("Update computer done.\n");
				}
		} catch (SQLException e) {
			view.print("The computer you try to update does not exist.");
			logger.error("Error selecting a computer that does not exist.");
		}
	}
	
	/*
	 * Delete the computer that the user chose
	 */
	private void menuDeleteComputer() {
		
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
	
}// class
