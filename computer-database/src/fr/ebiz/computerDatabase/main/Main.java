package fr.ebiz.computerDatabase.main;

import fr.ebiz.computerDatabase.service.Service;

public class Main {

	public static void main(String[] args) {
		Service controller = new Service();
		controller.init();
	}

}
