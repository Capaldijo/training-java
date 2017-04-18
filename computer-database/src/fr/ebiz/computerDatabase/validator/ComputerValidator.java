package fr.ebiz.computerDatabase.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fr.ebiz.computerDatabase.model.ComputerDTO;

public class ComputerValidator {

	public static boolean isValid(ComputerDTO computer) {
		boolean isValid = true;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		String name = computer.getName();
		
		try{
			if(name.trim() == "")
				isValid = false;
			if(computer.getIntroduced().trim() != "")
				LocalDateTime.parse(computer.getIntroduced(), formatter);
			if(computer.getDiscontinued().trim() != "")
				LocalDateTime.parse(computer.getDiscontinued(), formatter);
			
			int compIdRef = Integer.parseInt(computer.getCompany_id());
			if(compIdRef<0)
				isValid = false;
		}catch(NumberFormatException | DateTimeParseException e){
			isValid = false;
		}		

		return isValid;
	}
	
}
