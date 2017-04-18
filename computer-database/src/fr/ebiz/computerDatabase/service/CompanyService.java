package fr.ebiz.computerDatabase.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.mapper.CompanyMapper;
import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.CompanyDTO;
import fr.ebiz.computerDatabase.persistence.CompanyDAO;

public class CompanyService {

	final Logger logger = LoggerFactory.getLogger(CompanyService.class);

	private CompanyDAO companyDAO;

	private CompanyMapper companyMapper;

	public CompanyService() throws ConnectionException {
		companyDAO = new CompanyDAO();
		companyMapper = new CompanyMapper();
	}

	public List<CompanyDTO> getCompanies() {
		List<CompanyDTO> list = new ArrayList<>();

		try {
			ResultSet dbCompanies = companyDAO.findAll();
			List<Company> listCompany = companyMapper.fromDBToCompanies(dbCompanies);
			list = companyMapper.toDTO(listCompany);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public CompanyDTO getCompany(int id) {
		CompanyDTO companyDTO = new CompanyDTO();

		try {
			ResultSet dbCompany = companyDAO.find(id);
			Company company = companyMapper.fromDBToCompany(dbCompany);
			companyDTO = companyMapper.toDTO(company);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return companyDTO;
	}
}
