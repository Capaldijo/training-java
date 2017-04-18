package fr.ebiz.computerDatabase.service;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import fr.ebiz.computerDatabase.mapper.ComputerMapper;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.persistence.ComputerDAO;

public class ComputerService {

	final Logger logger = LoggerFactory.getLogger(ComputerService.class);

	private ComputerDAO computerDAO;
	
	private ComputerMapper computerMapper;
	
	public ComputerService() {
		computerDAO = new ComputerDAO();
		computerMapper = new ComputerMapper();
	}
	
	/*
	 * Create a computer, enter its name, introduce and discontinued date, and
	 * its referenced company id return the computer constructed by those fields
	 */
	public void addComputer(ComputerDTO computerDTO) {

		Computer computer = computerMapper.toModel(computerDTO);

		try {
			if (computer != null)
				if (computerDAO.insert(computer) == 1) {
					logger.info("insert computer done.\n");
				}
		} catch (MySQLIntegrityConstraintViolationException ie) {
			logger.error("[INSERT] Error on getting invalid Company by its ID");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error inserting Computer");
		}
	}
}
