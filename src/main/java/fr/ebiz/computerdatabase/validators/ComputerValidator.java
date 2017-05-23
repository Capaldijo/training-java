package fr.ebiz.computerdatabase.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ComputerValidator implements Validator {
    private static final Logger LOG = LoggerFactory.getLogger(ComputerValidator.class);
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

            if (computer.getCompanyId() != null) {
                int compIdRef = Integer.parseInt(computer.getCompanyId());
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

    @Override
    public boolean supports(Class<?> aClass) {
        return ComputerDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        ComputerDTO computerDTO = (ComputerDTO) o;

        if (computerDTO.getId() != null) {
            if (Integer.parseInt(computerDTO.getId()) <= 0) {
                errors.rejectValue("id", "negative.value", new Object[]{"id"},
                        "can't be negative");
            }
        }

        LocalDate intro = null, discon = null;
        if (computerDTO.getIntroduced() != null && computerDTO.getIntroduced().trim() != "") {
            intro = LocalDate.parse(computerDTO.getIntroduced(), FORMATTER);
        }
        if (computerDTO.getDiscontinued() != null && computerDTO.getDiscontinued().trim() != "") {
            discon = LocalDate.parse(computerDTO.getDiscontinued(), FORMATTER);
        }
        // check if intro date is before discon date
        if (intro != null && discon != null) {
            if (intro.isAfter(discon)) {
                errors.rejectValue("introduced", "date.required", new Object[]{"introduced"},
                        "can't be before discontinued");
            }
        }

        if (computerDTO.getCompanyId() != null) {
            int compIdRef = Integer.parseInt(computerDTO.getCompanyId());
            if (compIdRef < 0) {
                errors.rejectValue("company_id", "negative.value", new Object[]{"company_id"},
                        "can't be negative");
            }
        }
    }
}
