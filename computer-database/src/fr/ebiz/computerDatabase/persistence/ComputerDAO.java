package fr.ebiz.computerDatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerDAO {

	private DateTimeFormatter formatter;
	
	private String TABLE_NAME;
	
	private Connection coMysql;
	
	public ComputerDAO() {
		// formatter for the LocalDateTime computer's fields
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		TABLE_NAME = Utils.COMPUTER_TABLE;
		coMysql = ConnectionMYSQL.getInstance().getConnection();
	}
	
	public ResultSet find(int idComp) throws SQLException {
		String query = "SELECT * FROM "+ TABLE_NAME +" WHERE id = ?";
		
		ResultSet resultat = this.getComputerById(query, idComp);
		
		return resultat;
	}
	
	public ResultSet findAll() throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME;
		return coMysql.createStatement().executeQuery(query);
	}
	
	/*
	 * Following the parameters, build a query that get 
	 * only 10nth lines of the Computer's table and return them
	 * 
	 * Return a list of 10 Computer
	 */
	public ResultSet findByPage(int numPage, int nbLine) throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME + " LIMIT ?, ?";
		
		PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
		prepStatement.setInt(1, numPage);
		prepStatement.setInt(2, nbLine);
		
		ResultSet resultat = prepStatement.executeQuery();
		
		return resultat;
	}
	
	/*
	 * Insert the computer given in parameter
	 * into the database. Get each of its parameters
	 * in order to build the query.
	 * 
	 *  return the result of the query.
	 */
	public int insert(Computer comp) throws SQLException {
		return this.insertOrUpdate(comp,0);
	}
	
	/*
	 * Update the computer given in parameter
	 * into the database. Get each of its parameters
	 * in order to build the query.
	 * 
	 *  return the result of the query.
	 */
	public int update(Computer comp) throws SQLException {
		return this.insertOrUpdate(comp,1);
	}
	
	public int delete(int id) throws SQLException {
		String query = "DELETE FROM "+ TABLE_NAME + " WHERE id = ?";
		PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
		prepStatement.setInt(1, id);
		return prepStatement.executeUpdate();
	}
	
	public ResultSet getComputerById(String query, int id) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
		prepStatement.setInt(1, id);
		return prepStatement.executeQuery();
	}
	
	public int insertOrUpdate(Computer comp, int typeOfRequest) throws SQLException {
		String name = comp.getName();
        LocalDateTime dateIntro = comp.getIntroduced();
		LocalDateTime dateDiscon = comp.getDiscontinued();
        int compIdRef = comp.getCompany_id();
        
        String strDateIntro = null, strDateDiscon = null;
        
        if(dateIntro != null)
            strDateIntro = dateIntro.format(formatter);
        
        if(dateDiscon != null)
            strDateDiscon = dateDiscon.format(formatter);
        
        String query = null;
        if(typeOfRequest == 0)// if Insert
        	query = "INSERT INTO " + TABLE_NAME + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";
        else
        	 query = "UPDATE " + TABLE_NAME + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
        
        PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
		prepStatement.setString(1, name);
		prepStatement.setString(2, strDateIntro);
		prepStatement.setString(3, strDateDiscon);
		if(compIdRef != 0)
			prepStatement.setInt(4, compIdRef);
		else
			prepStatement.setString(4, null);
		
		if(typeOfRequest != 0)// if Update we have one more parameter
			prepStatement.setLong(5, comp.getId());
		return prepStatement.executeUpdate();
	}
	
}
