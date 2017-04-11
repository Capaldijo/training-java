package fr.ebiz.computerDatabase.controller;

import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import fr.ebiz.computerDatabase.mapper.CompanyDAO;
import fr.ebiz.computerDatabase.mapper.ComputerDAO;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;

public class Controller {

	private ComputerDAO computerDAO;
	
	private CompanyDAO companyDAO;
	
	public Controller() {
		computerDAO = new ComputerDAO();
		companyDAO = new CompanyDAO();
		
		init();
	}
	
	public void init() {
		try {		    
			
			/* ------ GET ALL COMPANIES ----- */
//			ResultSet resultat = companyDAO.getAllCompanies();
//			while(resultat.next()) {
//				int id = resultat.getInt(Utils.COLUMN_ID);
//				String name = resultat.getString(Utils.COLUMN_NAME);
//				
//				System.out.println("id: " + id + ", name: " + name);
//			}
			
			
			/* ------ DELETE COMPUTER ----- */
//			Computer comp = new Computer(578, "coco", LocalDateTime.of(2012, Month.APRIL, 8, 12, 30, 02)
//					,LocalDateTime.of(2016, Month.DECEMBER, 9, 12, 30, 02), 5);
//
//			if(compdao.deleteComputer(comp) == 1)
//				System.out.println("delete success");
			
			/* ------ UDPDATE COMPUTER ----- */
//			Computer comp = new Computer(577, "test2", LocalDateTime.of(2012, Month.APRIL, 8, 12, 30, 02)
//					,LocalDateTime.of(2016, Month.DECEMBER, 9, 12, 30, 02), 5);
//			
//			if(compdao.updateComputer(comp) == 1)
//				System.out.println("update success");
			
			/* ------ INSERT COMPUTER ----- */
			Computer comp2 = new Computer("test", null, null, 500);
			
			if(computerDAO.createComputer(comp2) == 1)
				System.out.println("Insert succes");
			
			
			/* ------ GET COMPUTER BY ID ----- */
//			Computer comp = compdao.getComputer(12);
//			System.out.println(comp.toString());
			
			/* ------ GET COMPANY BY ID ----- */
//			Company comp = companyDAO.getCompany(1);
//			System.out.println(comp.toString());
			
		    DatabaseManager.getInstance().closeAll();

		} catch (MySQLIntegrityConstraintViolationException ie){
			System.out.println("Impossible de créer le computer, champ company_id invalide");
		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
			e.printStackTrace();
		} 
	} //init
}// class
