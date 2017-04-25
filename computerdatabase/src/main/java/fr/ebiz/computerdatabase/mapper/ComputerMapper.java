package fr.ebiz.computerdatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.model.ComputerDTO;
import fr.ebiz.computerdatabase.utils.Utils;

public class ComputerMapper {

    final Logger logger = LoggerFactory.getLogger(ComputerMapper.class);

    private static DateTimeFormatter formatterWEB;

    /**
     * Constructor ComputerMapper.
     */
    public ComputerMapper() {
        // formatter for the LocalDateTime computer's fields
        formatterWEB = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
    }

    /**
     * Build a computerDTO directly from the db.
     * @param resultat contains a computer
     * @return a computerDTO
     * @throws MapperException error on mapping computer
     */
    public ComputerDTO fromDBToDTO(ResultSet resultat) throws MapperException {
        ComputerDTO computer = null;
        try {
            String id = resultat.getString(Utils.COLUMN_ID);
            String name = resultat.getString(Utils.COLUMN_NAME);
            String dateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
            String dateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
            String compIdRef = resultat.getString(Utils.COLUMN_COMPANYID);

            if (dateIntro != null) {
                dateIntro = dateIntro.split(" ")[0];
            }
            if (dateDiscon != null) {
                dateDiscon = dateDiscon.split(" ")[0];
            }

            computer = new ComputerDTO.Builder(name).
                    introduced(dateIntro)
                    .discontinued(dateDiscon)
                    .companyId(compIdRef)
                    .id(id).build();

        } catch (SQLException e) {
            throw new MapperException("[FDBTODTO] Error on accessing data.");
        } catch (DateTimeParseException e) {
            throw new MapperException("[FDBTODTO] Error on parsing date.");
        }
        return computer;
    }

    /**
     * Build a list of computerDTO directly from the db.
     * @param resultat contains all the computer from db
     * @return a list of computerDTO
     * @throws MapperException error on mapping computer
     */
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

    /**
     * Map a computer to a computerDTO.
     * @param computer computer to map
     * @return a computerDTO
     */
    public ComputerDTO toDTO(Computer computer) {
        ComputerDTO computerDTO = null;

        ComputerDTO.Builder builder = new ComputerDTO.Builder(computer.getName())
                .id(String.valueOf(computer.getId())).companyId(String.valueOf(computer.getCompanyId()));

        if (computer.getIntroduced() != null) {
            builder.introduced(computer.getIntroduced().format(formatterWEB));
        } else {
            builder.introduced("null");
        }
        if (computer.getDiscontinued() != null) {
            builder.discontinued(computer.getDiscontinued().format(formatterWEB));
        } else {
            builder.discontinued("null");
        }

        computerDTO = builder.build();
        return computerDTO;
    }

    /**
     * Map a list of computer to list of computerDTO.
     * @param listComputer computers to map
     * @return a list of computerDTOs
     */
    public List<ComputerDTO> toDTO(List<Computer> listComputer) {
        List<ComputerDTO> listComputerDTO = new ArrayList<>();

        for (Computer computer : listComputer) {
            listComputerDTO.add(toDTO(computer));
        }

        return listComputerDTO;
    }

    /**
     * Map a computerDTO into a computer.
     * @param computerDTO to map
     * @return a computer
     * @throws MapperException error on mapping computerDTO
     */
    public Computer toComputer(ComputerDTO computerDTO) throws MapperException {
        Computer computer = null;

        Long id = 0L;
        LocalDate intro = null, discon = null;
        try {
            if (computerDTO.getId() != null) {
                id = Long.parseLong(computerDTO.getId());
            }
            if (computerDTO.getIntroduced().trim() != "") {
                intro = LocalDate.parse(computerDTO.getIntroduced(), formatterWEB);
            }
            if (computerDTO.getDiscontinued().trim() != "") {
                discon = LocalDate.parse(computerDTO.getDiscontinued(), formatterWEB);
            }
            int compIdRef = Integer.parseInt(computerDTO.getCompanyId());

            computer = new Computer.Builder(computerDTO.getName())
                    .id(id)
                    .introduced(intro)
                    .discontinued(discon)
                    .companyId(compIdRef).build();

        } catch (DateTimeParseException e) {
            throw new MapperException("[TOMODEL] Error on parsing date.");
        }

        return computer;
    }
}