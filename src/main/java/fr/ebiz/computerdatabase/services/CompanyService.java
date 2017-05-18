package fr.ebiz.computerdatabase.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.CompanyDAO;
import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.interfaces.ServiceInterface;
import fr.ebiz.computerdatabase.mappers.CompanyMapper;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.models.PaginationFilters;

/*
 * Company service is a singleton class. that will handle
 * all the Company service part, get all or one company
 * using the CompanyDAO class and CompanyMapper class
 */
public final class CompanyService implements ServiceInterface<CompanyDTO> {

    private static CompanyService instance;

    static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    private static HikariDataSource connection;

    private CompanyDAO companyDAO;

    private CompanyMapper companyMapper;

    /**
     * Constructor CompanyService.
     */
    private CompanyService() { }

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

    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public void setConnection(HikariDataSource connection) {
        this.connection = connection;
    }

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
        CompanyDTO companyDTO = new CompanyDTO();

        try {
            int idComp = Integer.parseInt(id);

            Company company = companyDAO.find(idComp);

            companyDTO = companyMapper.toDTO(company);
        } catch (DAOException | NumberFormatException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return companyDTO;
    }

    @Override
    public int delete(String id) {
        int res = 0;

        try {
            if (companyDAO.delete(id) == 1) {
                res = 1;
            } else {
                LOG.info("Delete company error.\n");
                res = 0;
            }
        } catch (SQLException | DAOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPANY] Error on accessing data.");
        }
        return res;
    }

    @Override
    public List<CompanyDTO> getByPage(String numPage, String nbLine, PaginationFilters filters) {
        List<CompanyDTO> listCompanyDTO = null;

        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            listCompanyDTO = companyDAO.findByPage(filters, numP, nbL);

        } catch (NumberFormatException | DAOException e) {
            LOG.error("[GETCOMPANYBYPAGE] Error on getting data.");
            throw new RuntimeException("[GETCOMPANYBYPAGE] Error on getting data.");
        }
        return listCompanyDTO;
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
}
