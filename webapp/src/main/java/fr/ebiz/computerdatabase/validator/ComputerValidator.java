package fr.ebiz.computerdatabase.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ComputerValidator implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerValidatorService.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);

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
                LOG.error("[VALIDATOR] ID: can't have negative value.");
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
                LOG.error("[VALIDATOR] DATE: can't have introduced before discontinued.");
                errors.rejectValue("introduced", "date.required", new Object[]{"introduced"},
                        "can't be before discontinued");
            }
        }

        if (computerDTO.getCompany() != null) {
            int compIdRef = Integer.parseInt(computerDTO.getCompany().getId());
            if (compIdRef < 0) {
                LOG.error("[VALIDATOR] COMPANY_ID: can't have negative value");
                errors.rejectValue("company_id", "negative.value", new Object[]{"company_id"},
                        "can't be negative");
            }
        }
    }
}
