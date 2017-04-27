package fr.ebiz.computerdatabase.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.CompanyDAO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.interfaces.ServiceInterface;
import fr.ebiz.computerdatabase.mappers.CompanyMapper;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatanase.dtos.CompanyDTO;

/*
 * Company service is a singleton class. that will handle
 * all the Company service part, get all or one company
 * using the CompanyDAO class and CompanyMapper class
 */
public final class CompanyService implements ServiceInterface<CompanyDTO> {

    private static CompanyService instance = new CompanyService();

    static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

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

    @Override
    public List<CompanyDTO> getAll() {
        List<CompanyDTO> list = new ArrayList<>();

        try {
            List<Company> listCompany = companyDAO.findAll();
            list = companyMapper.toDTO(listCompany);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public CompanyDTO get(String id) {
        CompanyDTO companyDTO = new CompanyDTO();

        try {
            int idComp = Integer.parseInt(id);
            Company company = companyDAO.find(idComp);
            companyDTO = companyMapper.toDTO(company);
        } catch (DAOException | NumberFormatException e) {
            throw new RuntimeException(e.getMessage());
        }
        return companyDTO;
    }

    @Override
    public List<CompanyDTO> getByPage(String numPage, String nbLine, PaginationFilters filters) {
        LOG.error("[SERVICE] [GETBYPAGE] Not implemented.");
        throw new RuntimeException("[SERVICE] [GETBYPAGE] Not implemented.");
    }

    @Override
    public int count() {
        LOG.error("[SERVICE] [COUNT] Not implemented.");
        throw new RuntimeException("[SERVICE] [COUNT] Not implemented.");
    }

    @Override
    public int count(String research) {
        LOG.error("[SERVICE] [COUNT] Not implemented.");
        throw new RuntimeException("[SERVICE] [COUNT] Not implemented.");
    }

    @Override
    public int add(CompanyDTO entity) {
        LOG.error("[SERVICE] [ADD] Not implemented.");
        throw new RuntimeException("[SERVICE] [ADD] Not implemented.");
    }

    @Override
    public int update(CompanyDTO dto) {
        LOG.error("[SERVICE] [UPDATE] Not implemented.");
        throw new RuntimeException("[SERVICE] [UPDATE] Not implemented.");
    }

    @Override
    public int delete(String... ids) {
        LOG.error("[SERVICE] [DELETE] Not implemented.");
        throw new RuntimeException("[SERVICE] [DELETE] Not implemented.");
    }

    @Override
    public int delete(String id) {
        int res = 0;
        try {
            if (companyDAO.delete(id) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
        } catch (SQLException | ConnectionException e) {
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        }
        return res;
    }
}
