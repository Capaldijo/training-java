package fr.ebiz.computerdatabase.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dao.CompanyDAO;
import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.mapper.CompanyMapper;
import fr.ebiz.computerdatabase.model.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Company service is a singleton class. that will handle
 * all the Company service part, get all or one company
 * using the CompanyDAO class and CompanyMapper class
 */
@Service
@Transactional
public final class CompanyService implements ICompanyService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyDAO companyDAO;

    private final CompanyMapper companyMapper;

    /**
     * Default Constructor.
     * @param companyDAO .
     * @param companyMapper .
     */
    @Autowired
    public CompanyService(CompanyDAO companyDAO, CompanyMapper companyMapper) {
        this.companyDAO = companyDAO;
        this.companyMapper = companyMapper;
    }

    /**
     * Constructor CompanyService.
     */


    @Override
    public List<CompanyDTO> getAll() {
        List<CompanyDTO> list = new ArrayList<>();
        try {

            List<Company> listCompany = companyDAO.findAll();

            list = companyMapper.toDTO(listCompany);
        } catch (DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public CompanyDTO get(String id) {
        CompanyDTO companyDTO = null;

        try {
            Long idComp = Long.parseLong(id);

            Company company = companyDAO.find(idComp);

            companyDTO = companyMapper.toDTO(company);
        } catch (DAOException | NumberFormatException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return companyDTO;
    }

    @Override
    @Transactional(rollbackFor = DAOException.class)
    public int delete(String id) {
        int res = 0;

        if (companyDAO.delete(id) == 1) {
            res = 1;
        } else {
            LOG.info("Delete company error.\n");
            res = 0;
        }

        return res;
    }

    @Override
    public List<CompanyDTO> getByPage(String numPage, String nbLine) {
        List<CompanyDTO> listCompanyDTO = null;

        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            listCompanyDTO = companyDAO.findByPage(numP, nbL);

        } catch (NumberFormatException | DAOException e) {
            LOG.error("[GETCOMPANYBYPAGE] Error on getting data.");
            throw new RuntimeException("[GETCOMPANYBYPAGE] Error on getting data.");
        }
        return listCompanyDTO;
    }
}
