package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerMapper {

	final Logger logger = LoggerFactory.getLogger(ComputerMapper.class);

	private DateTimeFormatter formatterDB;
	
	private DateTimeFormatter formatterWEB;

	public ComputerMapper() {
		// formatter for the LocalDateTime computer's fields
		formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		formatterWEB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	}

	/*
	 * get a computer from the database and build it as an instance of Computer
	 * class
	 * 
	 * return a Computer object
	 */
	public Computer fromDBToComputer(ResultSet resultat) throws SQLException {
		Long id = resultat.getLong(Utils.COLUMN_ID);
		String name = resultat.getString(Utils.COLUMN_NAME);
		String strDateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
		String strDateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
		int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);

		LocalDateTime dateIntro = null, dateDiscon = null;

		if (strDateIntro != null)
			dateIntro = LocalDateTime.parse(strDateIntro, formatterDB);

		if (strDateDiscon != null)
			dateDiscon = LocalDateTime.parse(strDateDiscon, formatterDB);

		Computer computer = new Computer.ComputerBuilder(name)
								.introduced(dateIntro)
								.discontinued(dateDiscon)
								.companyId(compIdRef)
								.id(id)
								.build();
		
		return computer;
	}

	public List<Computer> fromDBToComputers(ResultSet resultat) {
		List<Computer> list = new ArrayList<>();

		try {
			while (resultat.next()) {
				list.add(fromDBToComputer(resultat));
			}
		} catch (SQLException sqle) {
			logger.error("Error on reading data from db.");
		}

		return list;
	}
	
	public Computer toModel(ComputerDTO computerDTO) {
		Computer computer = null;
		
		LocalDateTime intro = LocalDateTime.parse(computerDTO.getIntroduced(), formatterWEB);
		LocalDateTime discon = LocalDateTime.parse(computerDTO.getDiscontinued(), formatterWEB);
		int compIdRef = Integer.parseInt(computerDTO.getCompany_id());
		
		computer = new Computer.ComputerBuilder(computerDTO.getName())
					.introduced(intro)
					.discontinued(discon)
					.companyId(compIdRef)
					.build();
		
		return computer;
	}
}
