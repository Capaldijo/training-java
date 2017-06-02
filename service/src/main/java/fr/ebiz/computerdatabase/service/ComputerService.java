package fr.ebiz.computerdatabase.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dao.ComputerDAO;
import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.dao.DAOException;
import fr.ebiz.computerdatabase.mapper.MapperException;
import fr.ebiz.computerdatabase.mapper.ComputerMapper;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;
import fr.ebiz.computerdatabase.validator.ComputerValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Computer service is a singleton class. that will handle
 * all the Computer service part, get all or one company
 * using the ComputerDAO class and ComputerMapper class
 */
@Service
@Transactional(rollbackFor = {DAOException.class, MapperException.class})
public final class ComputerService implements IComputerService {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerService.class);

    private final ComputerDAO computerDAO;

    private final ComputerMapper computerMapper;

    /**
     * Default constructor.
     * @param computerDAO DAO for computer.
     * @param computerMapper Mapper for computer.
     */
    @Autowired
    public ComputerService(ComputerDAO computerDAO, ComputerMapper computerMapper) {
        this.computerDAO = computerDAO;
        this.computerMapper = computerMapper;
    }


    @Override
    public int add(ComputerDTO computerDTO) {
        int res = 0;

        if (!ComputerValidatorService.isValid(computerDTO)) {
            LOG.error("[VALIDATION] The computer you tried to add is not valid.");
            throw new RuntimeException("[VALIDATION] The computer you tried to add is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);
                if (computer != null) {

                    if (computerDAO.insert(computer) > 0) {
                        res = 1;
                    } else {
                        LOG.info("[ADD] Insert computer error.\n");
                        res = -1;
                    }
                }
            } catch (MapperException | DAOException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public ComputerDTO get(String id) {
        try {
            /* ------ GET COMPUTER BY ID ----- */
            Long idComp = Long.parseLong(id);

            Computer computer = computerDAO.find(idComp);

            return computerMapper.toDTO(computer);
        } catch (NumberFormatException | DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ComputerDTO> getByPage(int numPage, int nbLine, PaginationFilters filters) {
        try {
            return computerMapper.toDTO(computerDAO.findByPage(filters, numPage, nbLine));
        } catch (DAOException e) {
            LOG.error("[GET_COMPUTER_BY_PAGE] Error on getting data.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int update(ComputerDTO computerDTO) {
        if (!ComputerValidatorService.isValid(computerDTO)) {
            throw new RuntimeException("[VALIDATION] The computer you tried to update is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);

                computerDAO.update(computer);
                if (computerDAO.update(computer) > 0) {
                    return  1;
                } else {
                    LOG.info("[UPDATE_COMPUTER] Update computer error.\n");
                    return  -1;
                }

            } catch (DAOException | MapperException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public int count(String research) {
        research = research.trim();
        try {
            return computerDAO.count(research);
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int deleteComputers(String id) {
        if (id != null) { // if POST
            String[] ids = id.split(",");
            if (computerDAO.delete(ids) == 1) {
                return  1;
            } else {
                LOG.info("[DELETE_COMPUTER] Delete computer error.\n");
                return  0;
            }
        } else {
            return  -1;
        }
    }

    @Override
    public int deleteComputer(String id) {
        if (computerDAO.delete(id) == 1) {
            return  1;
        } else {
            LOG.info("Delete computer error.\n");
            return  0;
        }
    }
}
