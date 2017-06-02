package fr.ebiz.computerdatabase.validator;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ComputerValidatorService {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerValidatorService.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
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
                    LOG.info("[VALIDATOR] getID false");
                }
            }

            // check if name isn't empty string
            if (name != null && name.trim() == "") {
                isValid = false;
                LOG.info("[VALIDATOR] getName empty false");
            }

            // check if name contain some tag elmt like <abc/>
            if (name.matches("(?s).*(<(\\w+)[^>]*>.*</\\2>|<(\\w+)[^>]*/>).*")) {
                isValid = false;
                LOG.info("[VALIDATOR] getName tag false");
            }

            // check if date are parsable => type of yyyy-MM-dd
            if (computer.getIntroduced() != null && computer.getIntroduced().trim() != "") {
                intro = LocalDate.parse(computer.getIntroduced(), FORMATTER);
            }
            if (computer.getDiscontinued() != null && computer.getDiscontinued().trim() != "") {
                discon = LocalDate.parse(computer.getDiscontinued(), FORMATTER);
            }

            // check if intro date is before discon date
            if (intro != null && discon != null) {
                if (intro.isAfter(discon)) {
                    isValid = false;
                    LOG.info("[VALIDATOR] intro before discon false");
                }
            }

            if (computer.getCompany() != null) {
                int compIdRef = Integer.parseInt(computer.getCompany().getId());
                if (compIdRef < 0) {
                    isValid = false;
                    LOG.info("[VALIDATOR] getCompanyId false");
                }
            }
        } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
            isValid = false;
            LOG.info("[VALIDATOR] error exception");
        }

        return isValid;
    }
}
