package fr.ebiz.computerDatabase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.exceptions.ServiceException;
import fr.ebiz.computerDatabase.mapper.ComputerMapper;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.persistence.ComputerDAO;
import fr.ebiz.computerDatabase.validator.ComputerValidator;

public class ComputerService {

	final Logger logger = LoggerFactory.getLogger(ComputerService.class);

	private ComputerDAO computerDAO;
	
	private ComputerMapper computerMapper;
	
	public ComputerService() throws ConnectionException {
		computerDAO = new ComputerDAO();
		computerMapper = new ComputerMapper();
	}
	
	/*
	 * Create a computer, enter its name, introduce and discontinued date, and
	 * its referenced company id return the computer constructed by those fields
	 */
	public void addComputer(ComputerDTO computerDTO) throws ServiceException, DAOException, MapperException {
		if(!ComputerValidator.isValid(computerDTO))
			throw new ServiceException("[VALIDATION] The computer you tried to add is not valid.");
		else{
			Computer computer = computerMapper.toModel(computerDTO);
			if (computer != null)
				if (computerDAO.insert(computer) == 1) {
					logger.info("insert computer done.\n");
				}
		}
	}
	
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
        } catch (NumberFormatException e){
            throw new ServiceException("[GETCOMPUTER] Error on ID given.");
        }
		
		return computerDTO;
	}
	
	/*
	 * Update a computer, ask the user which computer he wants to update with
	 * which fields
	 */
	public void updateComputer(ComputerDTO computerDTO) throws DAOException, MapperException, ServiceException {
	    if (!ComputerValidator.isValid(computerDTO))
	        throw new ServiceException("[VALIDATION] The computer you tried to update is not valid.");
	    else{
            Computer computer = computerMapper.toModel(computerDTO);
            computerDAO.update(computer);
            if (computerDAO.update(computer) == 1) {
                logger.info("Update computer done.\n");
            } else {
                logger.error("Update computer error.\n");
            }
	    }            

	}
}
