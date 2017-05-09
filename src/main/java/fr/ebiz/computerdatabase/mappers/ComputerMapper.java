package fr.ebiz.computerdatabase.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.interfaces.MapperInterface;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.utils.Utils;

public class ComputerMapper implements MapperInterface<ComputerDTO, Computer> {

    final Logger logger = LoggerFactory.getLogger(ComputerMapper.class);

    private static DateTimeFormatter formatterWEB;

    /**
     * Constructor ComputerMapper.
     */
    public ComputerMapper() {
        // formatter for the LocalDateTime computer's fields
        formatterWEB = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
    }


    @Override
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

    @Override
    public List<ComputerDTO> toDTO(List<Computer> listComputer) {
        List<ComputerDTO> listComputerDTO = new ArrayList<>();

        for (Computer computer : listComputer) {
            listComputerDTO.add(toDTO(computer));
        }

        return listComputerDTO;
    }

    @Override
    public Computer toModel(ComputerDTO computerDTO) throws MapperException {
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
