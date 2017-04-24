package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.model.ComputerDTO.ComputerDTOBuilder;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerMapper {

    final Logger logger = LoggerFactory.getLogger(ComputerMapper.class);

    private static DateTimeFormatter formatterDB;

    private static DateTimeFormatter formatterWEB;

    public ComputerMapper() {
        // formatter for the LocalDateTime computer's fields
        formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        formatterWEB = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    /*
     * Build a Computer from the different data from the ResultSet pass
     * in parameter.
     * 
     * return a Computer object
     */
    public Computer fromDBToComputer(ResultSet resultat) throws MapperException {
        Computer computer = null;
        try {
            Long id = resultat.getLong(Utils.COLUMN_ID);
            String name = resultat.getString(Utils.COLUMN_NAME);
            String strDateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
            String strDateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
            int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);

            LocalDate dateIntro = null, dateDiscon = null;

            if (strDateIntro != null)
                dateIntro = LocalDate.parse(strDateIntro, formatterDB);

            if (strDateDiscon != null)
                dateDiscon = LocalDate.parse(strDateDiscon, formatterDB);

            computer = new Computer.ComputerBuilder(name).introduced(dateIntro).discontinued(dateDiscon)
                    .companyId(compIdRef).id(id).build();

        } catch (SQLException e) {
            throw new MapperException("[FDBTCOMP] Error on accessing data.");
        } catch (DateTimeParseException e) {
            throw new MapperException("[FDBTCOMP] Error on parsing date.");
        }
        return computer;
    }
    
    /*
     * Build a List of Computer from the different data from the ResultSet pass
     * in parameter.
     * 
     * return a List of Computer object
     */
    public List<Computer> fromDBToComputers(ResultSet resultat) throws MapperException {
        List<Computer> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(fromDBToComputer(resultat));
            }
        } catch (SQLException sqle) {
            logger.error("Error on reading data from db.");
        }

        return list;
    }
    
    public ComputerDTO fromDBToDTO(ResultSet resultat) throws MapperException {
        ComputerDTO computer = null;
        try {
            String id = resultat.getString(Utils.COLUMN_ID);
            String name = resultat.getString(Utils.COLUMN_NAME);
            String dateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
            String dateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
            String compIdRef = resultat.getString(Utils.COLUMN_COMPANYID);
            
            if(dateIntro != null)
                dateIntro = dateIntro.split(" ")[0];
            if(dateDiscon != null)
                dateDiscon = dateDiscon.split(" ")[0];
            
            computer = new ComputerDTO.ComputerDTOBuilder(name).introduced(dateIntro).discontinued(dateDiscon)
                    .companyId(compIdRef).id(id).build();

        } catch (SQLException e) {
            throw new MapperException("[FDBTODTO] Error on accessing data.");
        } catch (DateTimeParseException e) {
            throw new MapperException("[FDBTODTO] Error on parsing date.");
        }
        return computer;
    }
    
    public List<ComputerDTO> fromDBToDTOs(ResultSet resultat) throws MapperException {
        List<ComputerDTO> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(fromDBToDTO(resultat));
            }
        } catch (SQLException sqle) {
            logger.error("Error on reading data from db.");
        }
        return list;
    }

    public ComputerDTO toDTO(Computer computer) {
        ComputerDTO computerDTO = null;

        ComputerDTOBuilder builder = new ComputerDTO.ComputerDTOBuilder(computer.getName())
                .id(String.valueOf(computer.getId())).companyId(String.valueOf(computer.getCompany_id()));

        if (computer.getIntroduced() != null)
            builder.introduced(computer.getIntroduced().format(formatterWEB));
        else
            builder.introduced("null");
        if (computer.getDiscontinued() != null)
            builder.discontinued(computer.getDiscontinued().format(formatterWEB));
        else
            builder.discontinued("null");

        computerDTO = builder.build();
        return computerDTO;
    }
    
    public List<ComputerDTO> toDTO(List<Computer> listComputer) {
        List<ComputerDTO> listComputerDTO = new ArrayList<>();
        
        for(Computer computer : listComputer)
            listComputerDTO.add(toDTO(computer));
        
        return listComputerDTO;
    }

    public Computer toModel(ComputerDTO computerDTO) throws MapperException {
        Computer computer = null;

        Long id = 0L;
        LocalDate intro = null, discon = null;
        try {
            if (computerDTO.getId() != null)
                id = Long.parseLong(computerDTO.getId());
            if (computerDTO.getIntroduced().trim() != "")
                intro = LocalDate.parse(computerDTO.getIntroduced(), formatterWEB);
            if (computerDTO.getDiscontinued().trim() != "")
                discon = LocalDate.parse(computerDTO.getDiscontinued(), formatterWEB);
            int compIdRef = Integer.parseInt(computerDTO.getCompany_id());

            computer = new Computer.ComputerBuilder(computerDTO.getName()).id(id).introduced(intro).discontinued(discon)
                    .companyId(compIdRef).build();

        } catch (DateTimeParseException e) {
            throw new MapperException("[TOMODEL] Error on parsing date.");
        }

        return computer;
    }
}
