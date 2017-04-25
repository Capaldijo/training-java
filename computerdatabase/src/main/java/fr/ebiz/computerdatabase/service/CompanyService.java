package fr.ebiz.computerdatabase.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.mapper.CompanyMapper;
import fr.ebiz.computerdatabase.model.Company;
import fr.ebiz.computerdatabase.model.CompanyDTO;
import fr.ebiz.computerdatabase.persistence.CompanyDAO;

/*
 * Company service is a singleton class. that will handle
 * all the Company service part, get all or one company
 * using the CompanyDAO class and CompanyMapper class
 */
public final class CompanyService {

    private static CompanyService instance = new CompanyService();

    final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private CompanyDAO companyDAO;

    private CompanyMapper companyMapper;

    /**
     * Constructor CompanyService.
     */
    private CompanyService() {
        try {
            companyDAO = new CompanyDAO();
            companyMapper = new CompanyMapper();
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GetInstance.
     * @return CompanyService instance
     */
    public static CompanyService getInstance() {

        if (CompanyService.instance == null) {
            CompanyService.instance = new CompanyService();
        }
        return CompanyService.instance;
    }

    /**
     * Get, in a list, all the Companies stored in the database
     * mapped into CompanyDTO objects.
     * @return list of companyDTO
     */
    public List<CompanyDTO> getCompanies() {
        List<CompanyDTO> list = new ArrayList<>();

        try {
            List<Company> listCompany = companyDAO.findAll();
            list = companyMapper.toDTO(listCompany);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    /**
     * Get the company of that correspond to the
     * given id in parameter, into a mapped CompanyDTO.
     * @param id id to found
     * @return ComputerDTO
     */
    public CompanyDTO getCompany(int id) {
        CompanyDTO companyDTO = new CompanyDTO();

        try {
            Company company = companyDAO.find(id);
            companyDTO = companyMapper.toDTO(company);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return companyDTO;
    }
}
