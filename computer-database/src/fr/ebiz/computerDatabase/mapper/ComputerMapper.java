package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.service.Service;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerMapper {

	final Logger logger = LoggerFactory.getLogger(ComputerMapper.class);
	
	private DateTimeFormatter formatter;
	
	public ComputerMapper() {
		// formatter for the LocalDateTime computer's fields
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	}
	
	/*
	 * get a computer from the database and build it
	 * as an instance of Computer class
	 * 
	 * return a Computer object
	 */
	public Computer fromDBToComputer(ResultSet resultat) throws SQLException {
		int id = resultat.getInt(Utils.COLUMN_ID);
        String name = resultat.getString(Utils.COLUMN_NAME);
        String strDateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
        String strDateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
        int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);
        
        LocalDateTime dateIntro = null, dateDiscon = null;
        
        if(strDateIntro != null)
        	dateIntro = LocalDateTime.parse(strDateIntro, formatter);
        
        if(strDateDiscon != null)
        	dateDiscon = LocalDateTime.parse(strDateDiscon, formatter);
        
		return new Computer(id, name, dateIntro, dateDiscon, compIdRef);
	}
	
	public List<Computer> fromDBToComputers(ResultSet resultat) {
		List<Computer> list = new ArrayList<>();
		
		try{
			while(resultat.next()){
				list.add(fromDBToComputer(resultat));
			}
		} catch(SQLException sqle) {
			logger.error("Error on reading data from db.");
		}
		
		return list;
	}
}
