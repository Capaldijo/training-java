package fr.ebiz.computerdatabase.services;

import java.sql.Connection;
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
import fr.ebiz.computerdatabase.persistence.ConnectionDB;
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

    private Connection co;

    /**
     * Constructor CompanyService.
     */
    private CompanyService() {
        companyDAO = new CompanyDAO();
        companyMapper = new CompanyMapper();
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
            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("GETALL");
            TransactionHolder.set(co);

            List<Company> listCompany = companyDAO.findAll();

            TransactionHolder.unset();
            co.commit();

            list = companyMapper.toDTO(listCompany);
        } catch (DAOException | SQLException | ConnectionException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return list;
    }

    @Override
    public CompanyDTO get(String id) {
        CompanyDTO companyDTO = new CompanyDTO();

        try {
            int idComp = Integer.parseInt(id);

            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("GETID");
            TransactionHolder.set(co);

            Company company = companyDAO.find(idComp);

            TransactionHolder.unset();
            co.commit();
            companyDTO = companyMapper.toDTO(company);
        } catch (DAOException | SQLException | ConnectionException | NumberFormatException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
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
            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("DELETE");
            TransactionHolder.set(co);
            if (companyDAO.delete(id) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
            TransactionHolder.unset();
            co.commit();
        } catch (SQLException | ConnectionException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return res;
    }
}
