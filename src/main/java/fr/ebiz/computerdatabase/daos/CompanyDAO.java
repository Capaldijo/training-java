package fr.ebiz.computerdatabase.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import fr.ebiz.computerdatabase.interfaces.ICompanyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAO implements ICompanyDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private static final String QUERY_FINDALL = "SELECT * FROM " + Utils.COMPANY_TABLE;

    private static final String QUERY_FINDBYPAGE = "SELECT * FROM " + Utils.COMPANY_TABLE + " LIMIT ?, ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private final HikariDataSource dataSource;

    /**
     * Constructor companyDAO.
     * @param dataSource .
     * @throws ConnectionException Error on co to db.
     */
    @Autowired
    public CompanyDAO(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Company find(int id) throws DAOException {
        Company company = null;
        PreparedStatement prepStatement = null;
        ResultSet resultat = null;
        Connection co = null;
        try {
            co = DataSourceUtils.getConnection(dataSource);
            prepStatement = co.prepareStatement(QUERY_FIND);
            prepStatement.setInt(1, id);

            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
            resultat.next();
            company = toModel(resultat);
        } catch (SQLException e) {
            LOG.error("[FIND] Error accessing data.");
            throw new DAOException("[FIND] Error on accessing data.");
        } finally {
            DataSourceUtils.releaseConnection(co, dataSource);
            Utils.closeObjects(prepStatement, resultat);
        }

        return company;
    }

    @Override
    public List<Company> findAll() throws DAOException {
        List<Company> list = null;
        ResultSet resultat = null;
        Connection co = null;
        try {
            co = DataSourceUtils.getConnection(dataSource);
            resultat = co.createStatement().executeQuery(QUERY_FINDALL);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDALL] No data for request.");
            }
            list = toModels(resultat);
        } catch (SQLException e) {
            LOG.error("[FINDALL] Error accessing data.");
            throw new DAOException("[FINDALL] Error on accessing data.");
        } finally {
            DataSourceUtils.releaseConnection(co, dataSource);
            Utils.closeObjects(null, resultat);
        }
        return list;
    }

    @Override
    public List<CompanyDTO> findByPage(int numPage, int nbLine) throws DAOException {
        List<CompanyDTO> list = new ArrayList<>();
        PreparedStatement prepStatement = null;
        ResultSet resultat = null;
        Connection co = null;
        try {
            co = DataSourceUtils.getConnection(dataSource);
            prepStatement = co.prepareStatement(QUERY_FINDBYPAGE);
            prepStatement.setInt(1, numPage);
            prepStatement.setInt(2, nbLine);
            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDBYPAGE] No data for request.");
            }
            list = toDTOs(resultat);
        } catch (SQLException e) {
            LOG.error("[FINDBYPAGE] Error accessing data.");
            throw new DAOException("[FINDBYPAGE] Error on accessing data.");
        } finally {
            DataSourceUtils.releaseConnection(co, dataSource);
            Utils.closeObjects(prepStatement, resultat);
        }
        return list;
    }

    @Override
    public int delete(String id) throws SQLException, DAOException {

        Connection co = DataSourceUtils.getConnection(dataSource);
        PreparedStatement prepStatement = co.prepareStatement(QUERY_DELETE);
        prepStatement.setString(1, id);
        int res = prepStatement.executeUpdate();
        if (!DataSourceUtils.isConnectionTransactional(co, dataSource)) {
            DataSourceUtils.releaseConnection(co, dataSource);
        }
        Utils.closeObjects(prepStatement, null);
        return res;
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
}
