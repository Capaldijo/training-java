package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.CompanyDTO;
import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyMapper {

	final Logger logger = LoggerFactory.getLogger(CompanyMapper.class);

	public CompanyMapper() {
	}

	/*
	 * get a company from the database and build it as an instance of Company
	 * class
	 * 
	 * return a company object
	 */
	public Company fromDBToCompany(ResultSet resultat) throws SQLException {
		Long id = resultat.getLong(Utils.COLUMN_ID);
		String name = resultat.getString(Utils.COLUMN_NAME);

		return new Company(id, name);
	}

	public List<Company> fromDBToCompanies(ResultSet resultat) {
		List<Company> list = new ArrayList<>();

		try {
			while (resultat.next()) {
				list.add(fromDBToCompany(resultat));
			}
		} catch (SQLException sqle) {
			logger.error("Error on reading data from db.");
		}

		return list;
	}

	public List<CompanyDTO> toDTO(List<Company> listCompany) {
		List<CompanyDTO> listDTO = new ArrayList<>();

		for (Company company : listCompany) {
			String id = String.valueOf(company.getId());
			String name = company.getName();
			listDTO.add(new CompanyDTO(id, name));
		}

		return listDTO;
	}
	
	public CompanyDTO toDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		
		String id = String.valueOf(company.getId());
		String name = company.getName();
		
		companyDTO.setId(id);
		companyDTO.setName(name);
		
		return companyDTO;
	}
}
