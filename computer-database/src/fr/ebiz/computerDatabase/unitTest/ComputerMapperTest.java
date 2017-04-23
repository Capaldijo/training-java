package fr.ebiz.computerDatabase.unitTest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.mapper.ComputerMapper;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.model.ComputerDTO;

public class ComputerMapperTest {

    ComputerMapper computerMapper = null;
    
    ComputerDTO computerDTO = null;
    Computer computer = null;
    
    String intro = "1995-05-04";
    String discon = "2005-06-04";
    
    LocalDate introDate = null, disconDate = null;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Before
    public void beforeTest() {
        
        computerMapper = new ComputerMapper();
        
        computerDTO = new ComputerDTO.ComputerDTOBuilder("test")
                            .id("1")
                            .introduced(intro)
                            .discontinued(discon)
                            .companyId("5")
                            .build();
        
        introDate = LocalDate.parse(intro, formatter);
        disconDate = LocalDate.parse(discon, formatter);
        
        computer = new Computer.ComputerBuilder("test")
                        .id(1L)
                        .introduced(introDate)
                        .discontinued(disconDate)
                        .companyId(5)
                        .build();
    }
    
    @Test
    public void testToDTO() {
        try {
            assertEquals("Testing to DTO", computer ,computerMapper.toModel(computerDTO));
        } catch (MapperException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testToModel() {
        assertEquals("Testing to DTO", computerDTO ,computerMapper.toDTO(computer));
    }

}
