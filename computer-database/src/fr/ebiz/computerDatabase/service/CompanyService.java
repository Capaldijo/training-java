package fr.ebiz.computerDatabase.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.mapper.CompanyMapper;
import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.CompanyDTO;
import fr.ebiz.computerDatabase.persistence.CompanyDAO;

/*
 * Company service is a singleton class. that will handle
 * all the Company service part, get all or one company
 * using the CompanyDAO class and CompanyMapper class
 */
public final class CompanyService {

    private static volatile CompanyService instance = null;

    final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private CompanyDAO companyDAO;

    private CompanyMapper companyMapper;

    /**
     * Constructor.
     * @throws ConnectionException error on db
     */
    private CompanyService() throws ConnectionException {
        companyDAO = new CompanyDAO();
        companyMapper = new CompanyMapper();
    }

    /**
     * GetInstance.
     * @return CompanyService instance
     * @throws ConnectionException error on db
     */
    public static CompanyService getInstance() throws ConnectionException {

        if (CompanyService.instance == null) {
            synchronized (CompanyService.class) {
                if (CompanyService.instance == null) {
                    CompanyService.instance = new CompanyService();
                }
            }
        }
        return CompanyService.instance;
    }

    /**
     * Get, in a list, all the Companies stored in the database
     * mapped into CompanyDTO objects.
     * @throws DAOException error on dao
     * @throws MapperException error on mapper
     * @return list of companyDTO
     */
    public List<CompanyDTO> getCompanies() throws DAOException, MapperException {
        List<CompanyDTO> list = new ArrayList<>();

        ResultSet dbCompanies = companyDAO.findAll();
        List<Company> listCompany = companyMapper.fromDBToCompanies(dbCompanies);
        list = companyMapper.toDTO(listCompany);

        return list;
    }

    /**
     * Get the company of that correspond to the
     * given id in parameter, into a mapped CompanyDTO.
     * @param id id to found
     * @throws DAOException error on dao
     * @throws MapperException error on mapper
     * @return ComputerDTO
     */
    public CompanyDTO getCompany(int id) throws DAOException, MapperException {
        CompanyDTO companyDTO = new CompanyDTO();

        ResultSet dbCompany = companyDAO.find(id);
        Company company = companyMapper.fromDBToCompany(dbCompany);
        companyDTO = companyMapper.toDTO(company);

        return companyDTO;
    }
}
