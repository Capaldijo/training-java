package fr.ebiz.computerdatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.model.Company;
import fr.ebiz.computerdatabase.utils.Utils;

public class CompanyDAO {

    static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private Connection coMysql;

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private static final String QUERY_FINDALL = "SELECT * FROM " + Utils.COMPANY_TABLE;

    private static final String QUERY_FINDBYPAGE = "SELECT * FROM " + Utils.COMPANY_TABLE + " LIMIT ?, ?";

    /**
     * Constructor companyDAO.
     * @throws ConnectionException Error on co to db.
     */
    public CompanyDAO() throws ConnectionException {
        coMysql = ConnectionDB.getInstance().getConnection();
    }

    /**
     * Following the given ID, return the company corresponding into a ResultSet
     * object.
     * @param id of the company to get.
     * @return a ResultSetl containing a company.
     * @throws DAOException Error on getting data from DB.
     */
    public Company find(int id) throws DAOException {
        Company company = null;
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(QUERY_FIND);
            prepStatement.setInt(1, id);

            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
            company = toCompany(resultat);
        } catch (SQLException e) {
            throw new DAOException("[FIND] Error on accessing data.");
        }

        return company;
    }

    /**
     * Get all the company stored in the database into a ResultSet object.
     * @return into a ResultSet all the company.
     * @throws DAOException Error on getting data from DB.
     */
    public List<Company> findAll() throws DAOException {
        List<Company> list = null;
        try {
            ResultSet resultat = coMysql.createStatement().executeQuery(QUERY_FINDALL);
            list = toCompanies(resultat);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDALL] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[FINDALL] Error on accessing data.");
        }
        return list;
    }

    /**
     * Following the parameters, build a query that get only 10nth lines of the
     * Company's table and return them into a ResultSet object.
     * @param numPage the page the user wants to go on.
     * @param nbLine number of line to print.
     * @return a ResultSet containing the page.
     * @throws DAOException Error on getting data from DB.
     */
    public List<Company> findByPage(int numPage, int nbLine) throws DAOException {

        List<Company> list = new ArrayList<>();
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(QUERY_FINDBYPAGE);
            prepStatement.setInt(1, numPage);
            prepStatement.setInt(2, nbLine);
            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDBYPAGE] No data for request.");
            }
            while (resultat.next()) {
                list.add(toCompany(resultat));
            }
        } catch (SQLException e) {
            throw new DAOException("[FINDBYPAGE] Error on accessing data.");
        }
        return list;
    }

    /**
     * Get a list company from the database and build it as an instance of Company
     * class return a company object list.
     * @param resultat contains all companies from db.
     * @return a list of Companies.
     * @throws SQLException Error on getting data.
     */
    private List<Company> toCompanies(ResultSet resultat) throws SQLException {
        List<Company> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(toCompany(resultat));
            }
        } catch (SQLException sqle) {
            LOG.error("Error on reading data from db.");
            throw new SQLException(sqle.getMessage());
        }

        return list;
    }

    /**
     * Get a company from the database and build it as an instance of Company
     * class return a company object.
     * @param resultat contains a company from db.
     * @return a company.
     * @throws SQLException Error on getting data.
     */
    private Company toCompany(ResultSet resultat) throws SQLException {
        Long id = 0L;
        String name = null;
        id = resultat.getLong(Utils.COLUMN_ID);
        name = resultat.getString(Utils.COLUMN_NAME);

        return new Company(id, name);
    }
}
