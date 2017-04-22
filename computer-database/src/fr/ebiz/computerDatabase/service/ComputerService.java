package fr.ebiz.computerDatabase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.exceptions.ServiceException;
import fr.ebiz.computerDatabase.mapper.ComputerMapper;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.persistence.ComputerDAO;
import fr.ebiz.computerDatabase.validator.ComputerValidator;

public final class ComputerService {

    private static volatile ComputerService instance = null;

    final Logger logger = LoggerFactory.getLogger(ComputerService.class);

    private ComputerDAO computerDAO;

    private ComputerMapper computerMapper;

    private ComputerService() throws ConnectionException {
        computerDAO = new ComputerDAO();
        computerMapper = new ComputerMapper();
    }

    public final static ComputerService getInstance() throws ConnectionException {

        if (ComputerService.instance == null) {
            synchronized (ComputerService.class) {
                if (ComputerService.instance == null) {
                    ComputerService.instance = new ComputerService();
                }
            }
        }
        return ComputerService.instance;
    }

    /*
     * Check if ComputerDTO is Valid, if so map it to a Computer and insert into
     * db else throw an ServiceException.
     */
    public void addComputer(ComputerDTO computerDTO) throws ServiceException, DAOException, MapperException {
        if (!ComputerValidator.isValid(computerDTO))
            throw new ServiceException("[VALIDATION] The computer you tried to add is not valid.");
        else {
            Computer computer = computerMapper.toModel(computerDTO);
            if (computer != null)
                if (computerDAO.insert(computer) == 1) {
                    logger.info("insert computer done.\n");
                }
        }
    }

    /*
     * Check first if the String passed is type of Long, if so, go get the
     * Computer map it to ComputerDTO and return it. Else throw an
     * ServiceException.
     */
    public ComputerDTO getComputer(String id) throws DAOException, MapperException, ServiceException {
        ComputerDTO computerDTO = null;
        ResultSet res = null;
        Computer computer = null;

        try {
            /* ------ GET COMPUTER BY ID ----- */
            Long idComp = Long.parseLong(id);
            res = computerDAO.find(idComp);
            res.next();
            computer = computerMapper.fromDBToComputer(res);
            computerDTO = computerMapper.toDTO(computer);
        } catch (SQLException e) {
            throw new ServiceException("[GETCOMPUTER] Error on accessing data.");
        } catch (NumberFormatException e) {
            throw new ServiceException("[GETCOMPUTER] Error on ID given.");
        }

        return computerDTO;
    }

    /*
     * Check if the ComputerDTO is valid first, if so, update it into db else
     * throw an ServiceException
     */
    public void updateComputer(ComputerDTO computerDTO) throws DAOException, MapperException, ServiceException {
        if (!ComputerValidator.isValid(computerDTO))
            throw new ServiceException("[VALIDATION] The computer you tried to update is not valid.");
        else {
            Computer computer = computerMapper.toModel(computerDTO);
            computerDAO.update(computer);
            if (computerDAO.update(computer) == 1) {
                logger.info("Update computer done.\n");
            } else {
                logger.error("Update computer error.\n");
            }
        }
    }

    public List<ComputerDTO> getComputersByPage(int numPage, String research, int nbLine) throws ServiceException, MapperException, DAOException {
        List<ComputerDTO> listComputerDTO = new ArrayList<>();

        try {
            ResultSet res = null;

            if (!research.equals(""))
                res = computerDAO.searchByPage(research, numPage, nbLine);
            else
                res = computerDAO.findByPage(numPage, nbLine);

            listComputerDTO = computerMapper.fromDBToDTOs(res);
        } catch (NumberFormatException e) {
            throw new ServiceException("[GETCOMPUTERBYPAGE] Error on ID given.");
        }

        return listComputerDTO;
    }

    public int getNbComputer(String research) throws ServiceException, DAOException {
        int count = 0;
        ResultSet res = null;
        try {
            if (research.equals(""))
                res = computerDAO.count();
            else
                res = computerDAO.countSearch(research);
            res.next();
            count = res.getInt("count");
        } catch (SQLException e) {
            throw new ServiceException("[GETNBCOMPUTER] Error on accessing data.");
        }

        return count;
    }
}
