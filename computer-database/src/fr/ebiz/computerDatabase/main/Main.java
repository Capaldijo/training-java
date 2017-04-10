package fr.ebiz.computerDatabase.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import fr.ebiz.computerDatabase.persistence.DatabaseManager;
import fr.ebiz.computerDatabase.utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		try {
			String url = Utils.urlDB;
			String user = Utils.userDB;
			String pwd = Utils.passwdDB;
	
		    
		    /* Exécution d'une requête de lecture */
		    ResultSet resultat = DatabaseManager.getInstance().getCompanies();

		    /* Récupération des données du résultat de la requête de lecture */
		    while ( resultat.next() ) {
		        int idComp = resultat.getInt("id");
		        String nameComp = resultat.getString("name");
		        
		        System.out.println("id: " + idComp + ", name: " + nameComp);
		    }
		    
		    DatabaseManager.getInstance().closeAll();

		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
			e.printStackTrace();
		}
	}

}
