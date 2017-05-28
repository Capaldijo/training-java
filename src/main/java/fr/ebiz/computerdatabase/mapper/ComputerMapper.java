package fr.ebiz.computerdatabase.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import fr.ebiz.computerdatabase.dao.CompanyDAO;
import fr.ebiz.computerdatabase.dao.ICompanyDAO;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.model.Company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.exception.MapperException;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComputerMapper implements IComputerMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerMapper.class);

    private static DateTimeFormatter formatterWEB;

    private final ICompanyDAO companyService;
    /**
     * Constructor ComputerMapper.
     */
    @Autowired
    public ComputerMapper(CompanyDAO companyService) {
        this.companyService = companyService;
        // formatter for the LocalDateTime computer's fields
        formatterWEB = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
    }


    @Override
    public ComputerDTO toDTO(Computer computer) {
        ComputerDTO computerDTO = null;

        String companyId = computer.getCompany() != null ? computer.getCompany().getName() : "";

        ComputerDTO.Builder builder = new ComputerDTO.Builder(computer.getName())
                .id(String.valueOf(computer.getId()))
                .companyId(companyId);

        if (computer.getIntroduced() != null) {
            builder.introduced(computer.getIntroduced().format(formatterWEB));
        } else {
            builder.introduced("");
        }
        if (computer.getDiscontinued() != null) {
            builder.discontinued(computer.getDiscontinued().format(formatterWEB));
        } else {
            builder.discontinued("");
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
            Long compIdRef = Long.parseLong(computerDTO.getCompanyId());

            Company company = null;
            try {
                company = companyService.find(compIdRef);
            } catch (DAOException e) {
                LOG.error(e.getMessage());
                throw  new MapperException(e.getMessage());
            }

            computer = new Computer.Builder(computerDTO.getName())
                    .id(id)
                    .introduced(intro)
                    .discontinued(discon)
                    .company(company).build();

        } catch (DateTimeParseException e) {
            LOG.error("[TOMODEL] Error on parsing date.");
            throw new MapperException("[TOMODEL] Error on parsing date.");
        }

        return computer;
    }
}
