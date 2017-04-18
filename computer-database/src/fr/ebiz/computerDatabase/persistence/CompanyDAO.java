package fr.ebiz.computerDatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyDAO {

	private String TABLE_NAME;

	private Connection coMysql;

	public CompanyDAO() {
		TABLE_NAME = Utils.COMPANY_TABLE;
		coMysql = ConnectionMYSQL.getInstance().getConnection();
	}

	public ResultSet findAll() throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME;
		return coMysql.createStatement().executeQuery(query);
	}

	public ResultSet find(int id) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

		PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
		prepStatement.setInt(1, id);

		ResultSet resultat = prepStatement.executeQuery();

		return resultat;
	}

	/*
	 * Following the parameters, build a query that get only 10nth lines of the
	 * Company's table and return them
	 * 
	 * Return a list of 10 Company
	 */
	public ResultSet findByPage(int numPage, int nbLine) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " LIMIT ?, ?";
		ResultSet resultat = this.execQueryPageable(query, numPage, nbLine);
		return resultat;
	}

	public ResultSet execQueryPageable(String query, int numPage, int nbLine) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
		prepStatement.setInt(1, numPage);
		prepStatement.setInt(2, nbLine);
		return prepStatement.executeQuery();
	}
}
