package fr.ebiz.computerdatabase.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.validators.ComputerValidator;

public class ComputerValidatorTest {

    private ComputerDTO computerValidName = null;
    private ComputerDTO computerValidNameId = null;
    private ComputerDTO computerValidAll = null;
    private ComputerDTO computerInvalidId = null;
    private ComputerDTO computerInvalidNameNull = null;
    private ComputerDTO computerInvalideNameTag = null;
    private ComputerDTO computerInvalidIntroDate = null;
    private ComputerDTO computerInvalidDisconDate = null;
    private ComputerDTO computerInvalidOrderDates = null;
    private ComputerDTO computerInvalidCompanyId = null;

    /**
     * All var to initialize before test.
     */
    @Before
    public void beforeTest() {
        computerValidName = new ComputerDTO.Builder("test").build();
        computerValidNameId = new ComputerDTO.Builder("test").id("5").build();
        computerValidAll = new ComputerDTO.Builder("test").id("5").introduced("1995-05-01")
                .discontinued("2001-06-04").companyId("4").build();
        computerInvalidId = new ComputerDTO.Builder("test").id("-11").build();
        computerInvalidNameNull = new ComputerDTO.Builder(null).build();
        computerInvalideNameTag = new ComputerDTO.Builder("Test<html/>").build();
        computerInvalidIntroDate = new ComputerDTO.Builder("test").introduced("plop").build();
        computerInvalidDisconDate = new ComputerDTO.Builder("test").discontinued("123456").build();
        computerInvalidOrderDates = new ComputerDTO.Builder("test").introduced("2001-06-04")
                .discontinued("1995-05-01").build();
        computerInvalidCompanyId = new ComputerDTO.Builder("test").companyId("0").build();
    }

    /**
     * Test if id valid.
     */
    @Test
    public void testIsValidId() {
        assertTrue("ID is valid", ComputerValidator.isValid(computerValidNameId));
        assertFalse("ID is invalid", ComputerValidator.isValid(computerInvalidId));
    }

    /**
     * Test if name valid.
     */
    @Test
    public void testIsValidName() {
        assertTrue("Name is valid", ComputerValidator.isValid(computerValidName));
        assertFalse("Name is invalid => null", ComputerValidator.isValid(computerInvalidNameNull));
        assertFalse("Name is invalid => tag", ComputerValidator.isValid(computerInvalideNameTag));
    }

    /**
     * Test if introduced date valid.
     */
    @Test
    public void testIsValidIntroducedDates() {
        assertTrue("Intro date is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Intro date is invalid", ComputerValidator.isValid(computerInvalidIntroDate));
    }

    /**
     * Test if discontinued date valid.
     */
    @Test
    public void testIsValidDiscontinuedDates() {
        assertTrue("Discon date is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Discon date is invalid", ComputerValidator.isValid(computerInvalidDisconDate));
    }

    /**
     * Test if date order valid.
     */
    @Test
    public void testIsValidIntroDateBeforeDisconDate() {
        assertTrue("Order dates is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Order dates is invalid", ComputerValidator.isValid(computerInvalidOrderDates));
    }

    /**
     * Test if company id valid.
     */
    @Test
    public void testIsValidCompanyId() {
        assertTrue("Company id is valid", ComputerValidator.isValid(computerValidAll));
        assertFalse("Company id is invalid", ComputerValidator.isValid(computerInvalidCompanyId));
    }

}
