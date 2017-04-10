package fr.ebiz.computerDatabase.main;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import fr.ebiz.computerDatabase.mapper.ComputerDAO;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;

public class Main {

	public static void main(String[] args) {
		
		try {		    
		    
			ComputerDAO compdao = new ComputerDAO();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime dateTime = LocalDateTime.of(1986, Month.APRIL, 8, 12, 30, 02);
			String formattedDateTime = dateTime.format(formatter);
			System.out.println(formattedDateTime);
			
		    /* Récupération des données du résultat de la requête de lecture *
		    while ( resultat.next() ) {
		        int idComp = resultat.getInt("id");
		        String nameComp = resultat.getString("name");
		        
		        System.out.println("id: " + idComp + ", name: " + nameComp);
		    } */ 
			
			// insert into computer (name,introduced,discontinued,company_id) values ('Moto x Style','2016-10-14',null,2);
		    
			Computer comp = compdao.getComputer(1);
			System.out.println(comp.toString());
			
		    DatabaseManager.getInstance().closeAll();

		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
			e.printStackTrace();
		}
	}

}
