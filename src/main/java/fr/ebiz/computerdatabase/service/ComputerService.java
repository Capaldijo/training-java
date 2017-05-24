package fr.ebiz.computerdatabase.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dao.ComputerDAO;
import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.exception.MapperException;
import fr.ebiz.computerdatabase.mapper.ComputerMapper;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;
import fr.ebiz.computerdatabase.validator.ComputerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Computer service is a singleton class. that will handle
 * all the Computer service part, get all or one company
 * using the ComputerDAO class and ComputerMapper class
 */
@Service
@Transactional(rollbackFor = {DAOException.class, SQLException.class, MapperException.class})
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

        if (!ComputerValidator.isValid(computerDTO)) {
            LOG.error("[VALIDATION] The computer you tried to add is not valid.");
            throw new RuntimeException("[VALIDATION] The computer you tried to add is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);
                if (computer != null) {

                    if (computerDAO.insert(computer) == 1) {
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
        ComputerDTO computerDTO = null;
        Computer computer = null;

        try {
            /* ------ GET COMPUTER BY ID ----- */
            Long idComp = Long.parseLong(id);

            computer = computerDAO.find(idComp);

            computerDTO = computerMapper.toDTO(computer);
        } catch (NumberFormatException | DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return computerDTO;
    }

    @Override
    public List<ComputerDTO> getByPage(int numPage, int nbLine, PaginationFilters filters) {
        List<ComputerDTO> listComputerDTO = null;

        try {
            listComputerDTO = computerDAO.findByPage(filters, numPage, nbLine);
        } catch (DAOException e) {
            LOG.error("[GET_COMPUTER_BY_PAGE] Error on getting data.");
            throw new RuntimeException(e.getMessage());
        }
        return listComputerDTO;
    }

    @Override
    public int update(ComputerDTO computerDTO) {
        int res = 0;

        if (!ComputerValidator.isValid(computerDTO)) {
            throw new RuntimeException("[VALIDATION] The computer you tried to update is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);

                computerDAO.update(computer);
                if (computerDAO.update(computer) == 1) {
                    res = 1;
                } else {
                    LOG.info("[UPDATE_COMPUTER] Update computer error.\n");
                    res = -1;
                }

            } catch (DAOException | MapperException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public int count(String research) {
        int count = 0;
        research = research.trim();
        try {
            count = computerDAO.countAfterSearch(research);
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return count;
    }

    @Override
    public int deleteComputers(String id) {
        int res = 0;
        if (id != null) { // if POST
            String[] ids = id.split(",");
            try {
                if (computerDAO.delete(ids) == 1) {
                    res = 1;
                } else {
                    LOG.info("[DELETE_COMPUTER] Delete computer error.\n");
                    res = 0;
                }
            } catch (DAOException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        } else {
            res = -1;
        }
        return res;
    }

    @Override
    public int deleteComputer(String id) {
        int res = 0;

        try {
            if (computerDAO.delete(id) == 1) {
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
        } catch (SQLException | DAOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return res;
    }

    @Override
    public int deleteFromCompanyId(String id) {
        int res = 0;

        try {
            if (computerDAO.deleteFromCompanyId(id) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
        } catch (SQLException | DAOException e) {
            LOG.error("[DELETE_COMPANY_FROM_ID] Error on accessing data.");
            throw new RuntimeException(e.getMessage());
        }
        return res;
    }
}
