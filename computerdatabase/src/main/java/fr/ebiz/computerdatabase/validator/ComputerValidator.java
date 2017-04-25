package fr.ebiz.computerdatabase.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fr.ebiz.computerdatabase.model.ComputerDTO;

public class ComputerValidator {

    /**
     * Check if the computer is a valid computer, that is to say if it has a
     * name, if it has a valid intro & discon date yyyy-DD-mm and a valid
     * company id ref.
     * @param computer
     *      computer to test
     * @return true of false depending the validity
     */
    public static boolean isValid(ComputerDTO computer) {
        boolean isValid = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String name = computer.getName();

        LocalDate intro = null, discon = null;

        try {
            /*
             * If called before update, check if id is != "null" && is type of
             * Long and is > 0
             */
            if (computer.getId() != null) {
                if (Long.parseLong(computer.getId()) <= 0L) {
                    isValid = false;
                }
            }

            // check if name isn't empty string
            if (name != null && name.trim() == "") {
                isValid = false;
            }

            // check if name contain some tag elmt like <abc/>
            if (name.matches("(?s).*(<(\\w+)[^>]*>.*</\\2>|<(\\w+)[^>]*/>).*")) {
                isValid = false;
            }

            // check if date are parsable => type of yyyy-MM-dd
            if (computer.getIntroduced() != null && computer.getIntroduced().trim() != "") {
                intro = LocalDate.parse(computer.getIntroduced(), formatter);
            }
            if (computer.getDiscontinued() != null && computer.getDiscontinued().trim() != "") {
                discon = LocalDate.parse(computer.getDiscontinued(), formatter);
            }

            // check if intro date is before discon date
            if (intro != null && discon != null) {
                if (intro.isAfter(discon)) {
                    isValid = false;
                }
            }

            if (computer.getCompanyId() != null) {
                int compIdRef = Integer.parseInt(computer.getCompanyId());
                if (compIdRef <= 0) {
                    isValid = false;
                }
            }
        } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
            isValid = false;
        }

        return isValid;
    }

}
