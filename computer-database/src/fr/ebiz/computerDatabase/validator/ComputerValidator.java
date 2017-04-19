package fr.ebiz.computerDatabase.validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fr.ebiz.computerDatabase.model.ComputerDTO;

public class ComputerValidator {

    public static boolean isValid(ComputerDTO computer) {
        boolean isValid = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String name = computer.getName();

        LocalDate intro = null, discon = null;

        System.out.println(computer);

        try {
            /*
             * If called before update, check if id is != "null" && is type of
             * Long and is > 0
             */
            if (computer.getId() != null) {
                if(Long.parseLong(computer.getId()) <= 0L)
                    isValid = false;
            }

            // check if name isn't empty string
            if (name.trim() == "")
                isValid = false;

            // check if date are parsable => type of yyyy-MM-dd
            if (computer.getIntroduced().trim() != "")
                intro = LocalDate.parse(computer.getIntroduced(), formatter);
            if (computer.getDiscontinued().trim() != "")
                discon = LocalDate.parse(computer.getDiscontinued(), formatter);

            // check if intro date is before discon date
            if (intro != null && discon != null) {
                if (intro.isAfter(discon))
                    isValid = false;
            }

            int compIdRef = Integer.parseInt(computer.getCompany_id());
            if (compIdRef < 0)
                isValid = false;
        } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
            isValid = false;
        }

        return isValid;
    }

}
