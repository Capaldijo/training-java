package fr.ebiz.computerDatabase.main;

import java.sql.SQLException;

import fr.ebiz.computerDatabase.mapper.ComputerDAO;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		try {		    
		    
		    

		    /* Récupération des données du résultat de la requête de lecture *
		    while ( resultat.next() ) {
		        int idComp = resultat.getInt("id");
		        String nameComp = resultat.getString("name");
		        
		        System.out.println("id: " + idComp + ", name: " + nameComp);
		    } */
		    ComputerDAO compdao = new ComputerDAO();
			Computer comp = compdao.getComputer(1);
			comp.toString();
			
		    DatabaseManager.getInstance().closeAll();

		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
			e.printStackTrace();
		}
	}

}
