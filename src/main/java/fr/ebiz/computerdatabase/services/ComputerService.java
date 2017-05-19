package fr.ebiz.computerdatabase.services;

import java.sql.SQLException;
import java.util.List;

import fr.ebiz.computerdatabase.interfaces.IComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.ComputerDAO;
import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.mappers.ComputerMapper;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.validators.ComputerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Computer service is a singleton class. that will handle
 * all the Computer service part, get all or one company
 * using the ComputerDAO class and ComputerMapper class
 */
@Service
@Transactional
public final class ComputerService implements IComputerService {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerService.class);

    private final ComputerDAO computerDAO;

    private final ComputerMapper computerMapper;

    /**
     * Default constructor.
     * @param computerDAO .
     * @param computerMapper .
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
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);
                if (computer != null) {

                    if (computerDAO.insert(computer) == 1) {
                        res = 1;
                    } else {
                        LOG.info("insert computer error.\n");
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
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (NumberFormatException e) {
            LOG.error("[GETCOMPUTER] Error on ID given.");
            throw new RuntimeException("[GETCOMPUTER] Error on ID given.");
        }
        return computerDTO;
    }

    @Override
    public List<ComputerDTO> getByPage(String numPage, String nbLine, PaginationFilters filters) {
        List<ComputerDTO> listComputerDTO = null;

        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            listComputerDTO = computerDAO.findByPage(filters, numP, nbL);

        } catch (NumberFormatException | DAOException e) {
            LOG.error("[GETCOMPUTERBYPAGE] Error on getting data.");
            throw new RuntimeException("[GETCOMPUTERBYPAGE] Error on getting data.");
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
                    LOG.info("Update computer error.\n");
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
    public int count() {
        int count = 0;

        try {
            count = computerDAO.count();
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return count;
    }

    @Override
    public int count(String research) {
        int count = 0;

        try {
            count = computerDAO.countAfterSearch(research);
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = DAOException.class)
    public int delete(String...ids) {
        int res = 0;
        try {
            if (computerDAO.delete(ids) == 1) {
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = {DAOException.class, SQLException.class})
    public int delete(String id) {
        int res = 0;

        try {
            if (computerDAO.delete(id) == 1) {
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
        } catch (SQLException | DAOException e) {
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        }
        return res;
    }

    /**
     * Delete a computer which is built by company id parameter.
     * @param id of company id ref
     * @return 1 if deleted 0 either way
     */
    @Transactional(rollbackFor = {DAOException.class, SQLException.class})
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
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPANYFROMID] Error on accessing data.");
        }
        return res;
    }

    @Override
    public List<ComputerDTO> getAll() {
        LOG.error("[SERVICE] [GETALL] Not implemented anymore.");
        throw new RuntimeException("[SERVICE] [GETALL] Not implemented anymore.");
    }
}
