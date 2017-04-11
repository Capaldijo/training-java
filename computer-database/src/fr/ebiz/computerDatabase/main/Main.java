package fr.ebiz.computerDatabase.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import fr.ebiz.computerDatabase.controller.Controller;

public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.init();
	}

}
