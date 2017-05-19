package fr.ebiz.computerdatabase.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.mappers.ComputerMapper;
import fr.ebiz.computerdatabase.models.Computer;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ComputerMapperTest {

    private ComputerMapper computerMapper = null;

    private ComputerDTO computerDTO = null;
    private Computer computer = null;

    private String intro = "1995-05-04";
    private String discon = "2005-06-04";

    private LocalDate introDate = null, disconDate = null;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * All var to init before test.
     */
    @Before
    public void beforeTest() {

        computerMapper = new ComputerMapper();

        computerDTO = new ComputerDTO.Builder("test")
                .id("1")
                .introduced(intro)
                .discontinued(discon)
                .companyId("5")
                .build();

        introDate = LocalDate.parse(intro, formatter);
        disconDate = LocalDate.parse(discon, formatter);

        computer = new Computer.Builder("test")
                .id(1L)
                .introduced(introDate)
                .discontinued(disconDate)
                .companyId(5)
                .build();
    }

    /**
     * Test if mapper to dto is done well.
     */
    @Test
    public void testToDTO() {
        try {
            assertEquals("Testing to DTO", computer, computerMapper.toModel(computerDTO));
        } catch (MapperException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test if mapper to model is done well.
     */
    @Test
    public void testToModel() {
        assertEquals("Testing to DTO", computerDTO, computerMapper.toDTO(computer));
    }

}
