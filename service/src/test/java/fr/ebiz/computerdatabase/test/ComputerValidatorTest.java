package fr.ebiz.computerdatabase.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fr.ebiz.computerdatabase.dto.CompanyDTO;
import org.junit.Before;
import org.junit.Test;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.validator.ComputerValidatorService;

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
        computerValidName = new ComputerDTO.Builder("src/main/resources/test").build();
        computerValidNameId = new ComputerDTO.Builder("src/main/resources/test").id("5").build();
        computerValidAll = new ComputerDTO.Builder("src/main/resources/test").id("5").introduced("1995-05-01")
                .discontinued("2001-06-04").company(new CompanyDTO("4", null)).build();
        computerInvalidId = new ComputerDTO.Builder("src/main/resources/test").id("-11").build();
        computerInvalidNameNull = new ComputerDTO.Builder(null).build();
        computerInvalideNameTag = new ComputerDTO.Builder("Test<html/>").build();
        computerInvalidIntroDate = new ComputerDTO.Builder("src/main/resources/test").introduced("plop").build();
        computerInvalidDisconDate = new ComputerDTO.Builder("src/main/resources/test").discontinued("123456").build();
        computerInvalidOrderDates = new ComputerDTO.Builder("src/main/resources/test").introduced("2001-06-04")
                .discontinued("1995-05-01").build();
        computerInvalidCompanyId = new ComputerDTO.Builder("src/main/resources/test").company(new CompanyDTO("-1", null)).build();
    }

    /**
     * Test if id valid.
     */
    @Test
    public void testIsValidId() {
        assertTrue("ID is valid", ComputerValidatorService.isValid(computerValidNameId));
        assertFalse("ID is invalid", ComputerValidatorService.isValid(computerInvalidId));
    }

    /**
     * Test if name valid.
     */
    @Test
    public void testIsValidName() {
        assertTrue("Name is valid", ComputerValidatorService.isValid(computerValidName));
        assertFalse("Name is invalid => null", ComputerValidatorService.isValid(computerInvalidNameNull));
        assertFalse("Name is invalid => tag", ComputerValidatorService.isValid(computerInvalideNameTag));
    }

    /**
     * Test if introduced date valid.
     */
    @Test
    public void testIsValidIntroducedDates() {
        assertTrue("Intro date is valid", ComputerValidatorService.isValid(computerValidAll));
        assertFalse("Intro date is invalid", ComputerValidatorService.isValid(computerInvalidIntroDate));
    }

    /**
     * Test if discontinued date valid.
     */
    @Test
    public void testIsValidDiscontinuedDates() {
        assertTrue("Discon date is valid", ComputerValidatorService.isValid(computerValidAll));
        assertFalse("Discon date is invalid", ComputerValidatorService.isValid(computerInvalidDisconDate));
    }

    /**
     * Test if date order valid.
     */
    @Test
    public void testIsValidIntroDateBeforeDisconDate() {
        assertTrue("Order dates is valid", ComputerValidatorService.isValid(computerValidAll));
        assertFalse("Order dates is invalid", ComputerValidatorService.isValid(computerInvalidOrderDates));
    }

    /**
     * Test if company id valid.
     */
    @Test
    public void testIsValidCompanyId() {
        assertTrue("Company id is valid", ComputerValidatorService.isValid(computerValidAll));
        assertFalse("Company id is invalid", ComputerValidatorService.isValid(computerInvalidCompanyId));
    }

}
