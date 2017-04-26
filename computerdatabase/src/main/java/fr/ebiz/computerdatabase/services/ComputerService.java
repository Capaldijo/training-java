package fr.ebiz.computerdatabase.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.ComputerDAO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.interfaces.ServiceInterface;
import fr.ebiz.computerdatabase.mappers.ComputerMapper;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.validators.ComputerValidator;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

/**
 * Computer service is a singleton class. that will handle
 * all the Computer service part, get all or one company
 * using the ComputerDAO class and ComputerMapper class
 */
public final class ComputerService implements ServiceInterface<ComputerDTO> {

    private static ComputerService instance = new ComputerService();

    static final Logger LOG = LoggerFactory.getLogger(ComputerService.class);

    private ComputerDAO computerDAO;

    private ComputerMapper computerMapper;

    /**
     * Constructor ComputerService.
     * @throws ConnectionException Error on co to db
     */
    private ComputerService() {
        try {
            computerDAO = new ComputerDAO();
            computerMapper = new ComputerMapper();
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get the instance of ComputerService Singleton class.
     * @return the instance of ComputerSerice.
     */
    public static ComputerService getInstance() {

        if (ComputerService.instance == null) {
            ComputerService.instance = new ComputerService();
        }
        return ComputerService.instance;
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
                        LOG.info("insert computer done.\n");
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
    public List<ComputerDTO> getByPage(String numPage, String research, String nbLine) {
        List<ComputerDTO> listComputerDTO = null;
        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            if (research.equals("")) {
                listComputerDTO = computerDAO.findByPage(numP, nbL);
            } else {
                listComputerDTO = computerDAO.findBySearch(research, numP, nbL);
            }
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
                    LOG.info("Update computer done.\n");
                    res = 1;
                } else {
                    LOG.info("Update computer error.\n");
                    res = -1;
                }
            } catch (DAOException | MapperException e) {
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
    public int delete(String...ids) {
        int res = 0;
        try {
            if (computerDAO.deleteComputers(ids) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
        } catch (DAOException e) {
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        }
        return res;
    }

    @Override
    public int delete(String id) {
        LOG.error("[SERVICE] [DELETE] Not implemented.");
        throw new RuntimeException("[SERVICE] [DELETE] Not implemented.");
    }

    @Override
    public List<ComputerDTO> getAll() {
        LOG.error("[SERVICE] [GETALL] Not implemented anymore.");
        throw new RuntimeException("[SERVICE] [GETALL] Not implemented anymore.");
    }
}
