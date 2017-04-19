package fr.ebiz.computerDatabase.main;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.service.*; 

public class Main {

	public static void main(String[] args) {
		Service service;
		try {
			service = new Service();
			service.init();
		} catch (ConnectionException | DAOException | MapperException e) {
		}
	}

}
