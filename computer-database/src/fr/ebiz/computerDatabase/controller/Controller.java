package fr.ebiz.computerDatabase.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ebiz.computerDatabase.mapper.CompanyDAO;
import fr.ebiz.computerDatabase.mapper.ComputerDAO;
import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.view.Cli;

public class Controller {

	private ComputerDAO computerDAO;
	
	private CompanyDAO companyDAO;
	
	private boolean shouldKeepGoin = true; 
	
	private Cli view;
	
	public Controller() {
		computerDAO = new ComputerDAO();
		companyDAO = new CompanyDAO();
		view = new Cli();
		init();
	}
	
	public void init() {
		
		while(shouldKeepGoin) {
			
			switch(view.printMenu()) {
				case 1:
					menuListCompanies();
					break;
				case 2: 
					menuListComputer();
					break;
				case 3:
					menuCreateComputer();
					break;
				case 4:
					shouldKeepGoin = false;
					view.print("Bye");
					break;
				default:
					view.print("Error top menu");
			}
				
				/* ------ DELETE COMPUTER ----- */
//				Computer comp = new Computer(578, "coco", LocalDateTime.of(2012, Month.APRIL, 8, 12, 30, 02)
//						,LocalDateTime.of(2016, Month.DECEMBER, 9, 12, 30, 02), 5);

//				if(compdao.deleteComputer(comp) == 1)
//					System.out.println("delete success");
				
				/* ------ UDPDATE COMPUTER ----- */
//				Computer comp = new Computer(577, "test2", LocalDateTime.of(2012, Month.APRIL, 8, 12, 30, 02)
//						,LocalDateTime.of(2016, Month.DECEMBER, 9, 12, 30, 02), 5);
//				
//				if(compdao.updateComputer(comp) == 1)
//					System.out.println("update success");
				
				/* ------ INSERT COMPUTER ----- */
//				Computer comp2 = new Computer("test", null, null, 500);
//				
//				if(computerDAO.insert(comp2) == 1)
//					System.out.println("Insert succes");
				
			    
	
//			} catch (MySQLIntegrityConstraintViolationException ie){
//				System.out.println("Impossible de créer le computer, champ company_id invalide");
//			} catch ( SQLException e ) {
//				e.printStackTrace();
//			}
		}// while(shouldKeepGoin)
		try {
			DatabaseManager.getInstance().closeAll();
		} catch (SQLException e) {
			view.print("Error on close DB");
		}
	} // init
	
	public void menuListCompanies() {
		/* ------ GET ALL COMPANIES ----- */
		try {
			ResultSet resultat = companyDAO.findAll();
			view.printSubMenuCompanies(resultat);
		} catch (SQLException e) {
			view.print("Error on request findAll companies");
		}	
	}
	
	public void menuListComputer() {
		/* ------ GET ALL COMPUTER ----- */
		boolean stop = false;
		while(!stop){
			try {
				ResultSet resultat = computerDAO.findAll();
				switch(view.printSubMenuComputers(resultat)){
					case 1:
						//show details of a computer
						menuShowDetails();
						break;
					case 2:
						// update computer
						break;
					case 3:
						// delete computer
						menuDeleteComputer();
						break;
					case 4:
						// quit
						stop = true;
						break;
					default:
						view.print("Error top menu");
						
				}
			} catch (SQLException e) {
				view.print("Error on request findAll companies");
			}
		}
	}
	
	public void menuCreateComputer() {
		Computer computer = view.printInsertComputerAction();
		try {
			if(computer != null )
				if(computerDAO.insert(computer) == 1)
					view.print("\nInsert done");
		} catch (SQLException e) {
			view.print("\nError on insert");
		}
	}
	
	public void menuShowDetails() {

		int id = view.printShowDetailsAction();
		
		Computer computer = null;
		Company company = null;
		try {
			/* ------ GET COMPUTER BY ID ----- */
			computer = computerDAO.find(id);
			
			/* ------ GET COMPANY BY ID ----- */
			company = companyDAO.find(computer.getCompany_id());
		} catch (SQLException e) {
			// catch error if company is null, no need to print
		}
		
		view.print(computer + ", which reference company [" + company + "]");
	}
	
	public void menuDeleteComputer() {
		int id = view.printDeleteComputerAction();
		try {
			if(computerDAO.delete(id) == 1)
				view.print("delete done.");
			else
				view.print("No computer to delete.");
		} catch (SQLException e) {
			view.print("\nError on deleting computer");
		}
	}
	
}// class
