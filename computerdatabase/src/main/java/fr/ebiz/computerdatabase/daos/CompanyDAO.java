package fr.ebiz.computerdatabase.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.interfaces.DAOInterface;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.services.TransactionHolder;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatanase.dtos.CompanyDTO;

public class CompanyDAO implements DAOInterface<CompanyDTO, Company> {

    static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private Connection co = null;

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private static final String QUERY_FINDALL = "SELECT * FROM " + Utils.COMPANY_TABLE;

    private static final String QUERY_FINDBYPAGE = "SELECT * FROM " + Utils.COMPANY_TABLE + " LIMIT ?, ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    /**
     * Constructor companyDAO.
     * @throws ConnectionException Error on co to db.
     */
    public CompanyDAO() {
    }

    @Override
    public int delete(String id) throws SQLException {

        co = TransactionHolder.get();
        PreparedStatement prepStatement = co.prepareStatement(QUERY_DELETE);
        prepStatement.setString(1, id);
        int res = prepStatement.executeUpdate();
        prepStatement.close();
        return res;
    }

    @Override
    public Company find(int id) throws DAOException {
        Company company = null;
        PreparedStatement prepStatement = null;
        try {
            co = TransactionHolder.get();
            prepStatement = co.prepareStatement(QUERY_FIND);
            prepStatement.setInt(1, id);

            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
            resultat.next();
            company = toModel(resultat);
        } catch (SQLException e) {
            LOG.error("[FIND] Error accessing data.");
            throw new DAOException("[FIND] Error on accessing data.");
        } finally {
            try {
                prepStatement.close();
            } catch (SQLException e) {
                throw new DAOException("[FIND] Error on close co.");
            }
        }

        return company;
    }

    @Override
    public List<Company> findAll() throws DAOException {
        List<Company> list = null;
        try {
            co = TransactionHolder.get();
            ResultSet resultat = co.createStatement().executeQuery(QUERY_FINDALL);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDALL] No data for request.");
            }
            list = toModels(resultat);
        } catch (SQLException e) {
            LOG.error("[FINDALL] Error accessing data.");
            throw new DAOException("[FINDALL] Error on accessing data.");
        }
        return list;
    }

    @Override
    public List<CompanyDTO> findByPage(PaginationFilters filters, int numPage, int nbLine) throws DAOException {
        List<CompanyDTO> list = new ArrayList<>();
        PreparedStatement prepStatement = null;
        try {
            co = TransactionHolder.get();
            prepStatement = co.prepareStatement(QUERY_FINDBYPAGE);
            prepStatement.setInt(1, numPage);
            prepStatement.setInt(2, nbLine);
            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDBYPAGE] No data for request.");
            }
            list = toDTOs(resultat);
        } catch (SQLException e) {
            LOG.error("[FINDBYPAGE] Error accessing data.");
            throw new DAOException("[FINDBYPAGE] Error on accessing data.");
        } finally {
            try {
                prepStatement.close();
            } catch (SQLException e) {
                throw new DAOException("[FINDBYPAGE] Error on close co.");
            }
        }
        return list;
    }

    @Override
    public List<Company> toModels(ResultSet resultat) throws DAOException {
        List<Company> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(toModel(resultat));
            }
        } catch (SQLException sqle) {
            LOG.error("Error on reading data from db.");
            throw new DAOException(sqle.getMessage());
        }

        return list;
    }

    @Override
    public Company toModel(ResultSet resultat) throws DAOException {
        Long id = 0L;
        String name = null;
        try {
            id = resultat.getLong(Utils.COLUMN_ID);
            name = resultat.getString(Utils.COLUMN_NAME);
        } catch (SQLException e) {
            LOG.error("Error on reading data from db.");
            throw new DAOException(e.getMessage());
        }

        return new Company(id, name);
    }

    @Override
    public CompanyDTO toDTO(ResultSet resultat) throws DAOException {
        String id = null;
        String name = null;
        try {
            id = resultat.getString(Utils.COLUMN_ID);
            name = resultat.getString(Utils.COLUMN_NAME);
        } catch (SQLException e) {
            LOG.error("[TOCOMPUTERDTO] Error on accessing data.");
            throw new DAOException("[TOCOMPUTERDTO] Error on accessing data.");
        }

        return new CompanyDTO(id, name);
    }

    @Override
    public List<CompanyDTO> toDTOs(ResultSet resultat) throws DAOException {
        List<CompanyDTO> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(toDTO(resultat));
            }
        } catch (SQLException sqle) {
            LOG.error("[TOCOMPUTERDTOS]Error on reading data from db.");
            throw new DAOException("[TOCOMPUTERDTOS]Error on reading data from db.");
        }
        return list;
    }

    @Override
    public Company find(Long idComp) throws DAOException {
        LOG.error("[FIND] Not implemented yet.");
        throw new RuntimeException("[FIND] Not implemented yet.");
    }

    @Override
    public int count() throws DAOException {
        LOG.error("[COUNT] Not implemented yet.");
        throw new RuntimeException("[COUNT] Not implemented yet.");
    }

    @Override
    public int countAfterSearch(String search) throws DAOException {
        LOG.error("[COUNTAFTERSEARCH] Not implemented yet.");
        throw new RuntimeException("[COUNTAFTERSEARCH] Not implemented yet.");
    }

    @Override
    public int insert(Company comp) throws DAOException {
        LOG.error("[INSERT] Not implemented yet.");
        throw new RuntimeException("[INSERT] Not implemented yet.");
    }

    @Override
    public int update(Company comp) throws DAOException {
        LOG.error("[UPDATE] Not implemented yet.");
        throw new RuntimeException("[UPDATE] Not implemented yet.");
    }

    @Override
    public int delete(String... modelsId) throws DAOException {
        LOG.error("[DELETE] Not implemented yet.");
        throw new RuntimeException("[DELETE] Not implemented yet.");
    }
}
