package fr.ebiz.computerdatabase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.mapper.ComputerMapper;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.model.ComputerDTO;
import fr.ebiz.computerdatabase.persistence.ComputerDAO;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatabase.validator.ComputerValidator;

/**
 * Computer service is a singleton class. that will handle
 * all the Computer service part, get all or one company
 * using the ComputerDAO class and ComputerMapper class
 */
public final class ComputerService {

    private static ComputerService instance = new ComputerService();

    final Logger logger = LoggerFactory.getLogger(ComputerService.class);

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

    /**
     * Add a computer into the database.
     * Check if ComputerDTO is Valid, if so map it to a Computer and insert it
     * into db else throw an ServiceException.
     * @param computerDTO computer to add into db.
     */
    public void addComputer(ComputerDTO computerDTO) {
        if (!ComputerValidator.isValid(computerDTO)) {
            throw new RuntimeException("[VALIDATION] The computer you tried to add is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toComputer(computerDTO);
                if (computer != null) {
                    if (computerDAO.insert(computer) == 1) {
                        logger.info("insert computer done.\n");
                    }
                }
            } catch (MapperException | DAOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * Get a Computer by its ID given in parameter.
     * Check first if the String passed is type of Long, if so, go get the
     * Computer map it to ComputerDTO and return it. Else throw an
     * ServiceException.
     * @param id of the computer to get.
     * @return a ComputerDTO.
     * @throws DAOException Error on getting data from DB.
     */
    public ComputerDTO getComputer(String id) {
        ComputerDTO computerDTO = null;
        Computer computer = null;

        try {
            /* ------ GET COMPUTER BY ID ----- */
            Long idComp = Long.parseLong(id);
            computer = computerDAO.find(idComp);
            computerDTO = computerMapper.toDTO(computer);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new RuntimeException("[GETCOMPUTER] Error on ID given.");
        }

        return computerDTO;
    }

    /**
     * Update a computer into the database.
     * Check if the ComputerDTO is valid first, if so, update it into db else
     * @param computerDTO computer to update.
     */
    public void updateComputer(ComputerDTO computerDTO) {

        if (!ComputerValidator.isValid(computerDTO)) {
            throw new RuntimeException("[VALIDATION] The computer you tried to update is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toComputer(computerDTO);
                computerDAO.update(computer);
                if (computerDAO.update(computer) == 1) {
                    logger.info("Update computer done.\n");
                } else {
                    logger.error("Update computer error.\n");
                }
            } catch (DAOException | MapperException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * Return a list of ComputerDTO objects.
     * If the research parameter is empty, get all the computers
     * depending the current page. If not, get the computers
     * depending the given research parameter.
     * @param numPage get the page the user wants to go on.
     * @param research given by the user.
     * @param nbLine number of line to print.
     * @return list of ComputerDTO.
     */
    public List<ComputerDTO> getComputersByPage(String numPage, String research, String nbLine) {
        List<ComputerDTO> listComputerDTO = new ArrayList<>();

        try {
            ResultSet res = null;
            int numP = 0, nbL = Utils.PAGEABLE_NBLINE;

            if (numPage != null) {
                numP = Integer.parseInt(numPage);
            }
            if (nbLine != null) {
                nbL = Integer.parseInt(nbLine);
            }
            if (!(research == null)) {
                res = computerDAO.findBySearch(research, numP, nbL);
            } else {
                res = computerDAO.findByPage(numP, nbL);
            }

            listComputerDTO = computerMapper.fromDBToDTOs(res);
        } catch (NumberFormatException | MapperException | DAOException e) {
            throw new RuntimeException("[GETCOMPUTERBYPAGE] Error on ID given.");
        }

        return listComputerDTO;
    }

    /**
     * Get the number of all the computers stored in the database if
     * research parameter is empty, else return the number of computers
     * corresponding to the research.
     * @param research given by the user.
     * @return the number of computer following the research.
     * @throws ServiceException Error on getting data from DAO.
     * @throws DAOException Error on getting data from db.
     */
    public int getNbComputer(String research) {
        int count = 0;
        ResultSet res = null;
        try {
            if (research == null) {
                res = computerDAO.count();
            } else {
                res = computerDAO.countAfterSearch(research);
            }
            res.next();
            count = res.getInt("count");
        } catch (SQLException | DAOException e) {
            throw new RuntimeException("[GETNBCOMPUTER] Error on accessing data.");
        }

        return count;
    }
}