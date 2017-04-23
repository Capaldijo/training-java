package fr.ebiz.computerDatabase.unitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.validator.ComputerValidator;

public class ComputerValidatorTest {

    ComputerDTO computerValidName = null;
    ComputerDTO computerValidNameId = null;
    ComputerDTO computerValidAll = null;
    ComputerDTO computerInvalidId = null;
    ComputerDTO computerInvalidNameNull = null;
    ComputerDTO computerInvalideNameTag = null;
    ComputerDTO computerInvalidIntroDate = null;
    ComputerDTO computerInvalidDisconDate = null;
    ComputerDTO computerInvalidOrderDates = null;
    ComputerDTO computerInvalidCompanyId = null;

    @Before
    public void beforeTest() {
        computerValidName = new ComputerDTO.ComputerDTOBuilder("test").build();
        computerValidNameId = new ComputerDTO.ComputerDTOBuilder("test").id("5").build();
        computerValidAll = new ComputerDTO.ComputerDTOBuilder("test").id("5").introduced("1995-05-01")
                .discontinued("2001-06-04").companyId("4").build();
        computerInvalidId = new ComputerDTO.ComputerDTOBuilder("test").id("-11").build();
        computerInvalidNameNull = new ComputerDTO.ComputerDTOBuilder(null).build();
        computerInvalideNameTag = new ComputerDTO.ComputerDTOBuilder("Test<html/>").build();
        computerInvalidIntroDate = new ComputerDTO.ComputerDTOBuilder("test").introduced("plop").build();
        computerInvalidDisconDate = new ComputerDTO.ComputerDTOBuilder("test").discontinued("123456").build();
        computerInvalidOrderDates = new ComputerDTO.ComputerDTOBuilder("test").introduced("2001-06-04")
                .discontinued("1995-05-01").build();
        computerInvalidCompanyId = new ComputerDTO.ComputerDTOBuilder("test").companyId("0").build();
    }

    @Test
    public void testIsValidId() {
        assertTrue("ID is valid", ComputerValidator.isValid(computerValidNameId));
        assertFalse("ID is invalid", ComputerValidator.isValid(computerInvalidId));
    }

    @Test
    public void testIsValidName() {
        assertTrue("Name is valid", ComputerValidator.isValid(computerValidName));
        assertFalse("Name is invalid => null", ComputerValidator.isValid(computerInvalidNameNull));
        assertFalse("Name is invalid => tag", ComputerValidator.isValid(computerInvalideNameTag));
    }

    @Test
    public void testIsValidIntroducedDates() {
        assertTrue("Intro date is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Intro date is invalid", ComputerValidator.isValid(computerInvalidIntroDate));
    }

    @Test
    public void testIsValidDiscontinuedDates() {
        assertTrue("Discon date is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Discon date is invalid", ComputerValidator.isValid(computerInvalidDisconDate));
    }

    @Test
    public void testIsValidIntroDateBeforeDisconDate() {
        assertTrue("Order dates is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Order dates is invalid", ComputerValidator.isValid(computerInvalidOrderDates));
    }

    @Test
    public void testIsValidCompanyId() {
        assertTrue("Company id is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Company id is invalid", ComputerValidator.isValid(computerInvalidCompanyId));
    }

}
