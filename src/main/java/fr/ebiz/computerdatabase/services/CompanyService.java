package fr.ebiz.computerdatabase.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.CompanyDAO;
import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.interfaces.ServiceInterface;
import fr.ebiz.computerdatabase.mappers.CompanyMapper;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.persistence.ConnectionDB;

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
            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            List<Company> listCompany = companyDAO.findAll();

            TransactionHolder.get().commit();

            list = companyMapper.toDTO(listCompany);
        } catch (DAOException | SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return list;
    }

    @Override
    public CompanyDTO get(String id) {
        CompanyDTO companyDTO = new CompanyDTO();

        try {
            int idComp = Integer.parseInt(id);

            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            Company company = companyDAO.find(idComp);

            TransactionHolder.get().commit();

            companyDTO = companyMapper.toDTO(company);
        } catch (DAOException | SQLException | NumberFormatException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return companyDTO;
    }

    @Override
    public int delete(String id) {
        int res = 0;

        try {
            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            if (companyDAO.delete(id) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }

            TransactionHolder.get().commit();
        } catch (SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return res;
    }

    @Override
    public List<CompanyDTO> getByPage(String numPage, String nbLine, PaginationFilters filters) {
        List<CompanyDTO> listCompanyDTO = null;

        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            listCompanyDTO = companyDAO.findByPage(filters, numP, nbL);

            TransactionHolder.get().commit();
        } catch (NumberFormatException | DAOException e) {
            LOG.error("[GETCOMPANYBYPAGE] Error on getting data.");
            throw new RuntimeException("[GETCOMPANYBYPAGE] Error on getting data.");
        } catch (SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
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
