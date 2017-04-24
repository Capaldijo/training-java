package fr.ebiz.computerDatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyDAO {

	private String TABLE_NAME;

	private Connection coMysql;

	public CompanyDAO() throws ConnectionException {
		TABLE_NAME = Utils.COMPANY_TABLE;
		coMysql = ConnectionMYSQL.getInstance().getConnection();
	}
	
	/*
	 * Return all the company stored in the database
	 * into a ResultSet object.
	 */
	public ResultSet findAll() throws DAOException {
		String query = "SELECT * FROM " + TABLE_NAME;
		
		ResultSet resultat = null;
		try {
            resultat = coMysql.createStatement().executeQuery(query);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDALL] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[FINDALL] Error on accessing data.");
        }
		return resultat;
	}

	/*
	 * Following the given ID, return the company corresponding into
	 * a ResultSet object.
	 */
	public ResultSet find(int id) throws DAOException {
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
		
		ResultSet resultat = null;
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
            prepStatement.setInt(1, id);

            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[FIND] Error on accessing data.");
        }

		return resultat;
	}

	/*
	 * Following the parameters, build a query that get only 10nth lines of the
	 * Company's table and return them into a ResultSet object
	 * 
	 * Return a list of 10 Company
	 */
	public ResultSet findByPage(int numPage, int nbLine) throws DAOException {
		String query = "SELECT * FROM " + TABLE_NAME + " LIMIT ?, ?";
		
		ResultSet resultat = null;
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
            prepStatement.setInt(1, numPage);
            prepStatement.setInt(2, nbLine);
            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDBYPAGE] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[FINDBYPAGE] Error on accessing data.");
        }
		return resultat;
	}
}
