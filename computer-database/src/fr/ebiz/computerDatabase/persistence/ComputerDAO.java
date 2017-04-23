package fr.ebiz.computerDatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.model.Computer;
import fr.ebiz.computerDatabase.utils.Utils;

public class ComputerDAO {

    private DateTimeFormatter formatter;

    private Connection coMysql;

    public ComputerDAO() throws ConnectionException {
        // formatter for the LocalDateTime computer's fields
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        coMysql = ConnectionMYSQL.getInstance().getConnection();
    }

    public ResultSet find(Long idComp) throws DAOException {
        String query = "SELECT * FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

        ResultSet resultat = null;
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
            prepStatement.setLong(1, idComp);
            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[FIND] Error on accessing data.");
        }

        return resultat;
    }
    
    public ResultSet count() throws DAOException {
        String query = "SELECT COUNT(*) as count FROM " + Utils.COMPUTER_TABLE;
        ResultSet resultat = null;
        
        try {
            resultat = coMysql.createStatement().executeQuery(query);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[COUNT] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[COUNT] Error on accessing data.");
        }
        return resultat;
    }
    
    public ResultSet countSearch(String search) throws DAOException {
        String query = "SELECT COUNT(*) as count FROM " +
                "computer as c LEFT JOIN company as comp ON c.company_id = comp.id WHERE c.name LIKE ? OR " + 
                "comp.name LIKE ?";
        ResultSet resultat = null;
        
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
            prepStatement.setString(1, '%'+search+'%');
            prepStatement.setString(2, '%'+search+'%');

            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[COUNTSEARCH] No data for request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("[COUNTSEARCH] Error on accessing data.");
        }
        return resultat;
    }

    public ResultSet findAll() throws DAOException {
        String query = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
                + "FROM computer as c LEFT JOIN company as comp ON c.id = comp.id";

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
     * Following the parameters, build a query that get only 10nth lines of the
     * Computer's table and return them
     * 
     * Return a list of 10 Computer
     */
    public ResultSet findByPage(int numPage, int nbLine) throws DAOException {
        String query = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
                + "FROM computer as c LEFT JOIN company as comp ON c.company_id = comp.id LIMIT ?, ?";

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
    
    /*
     * Following the parameters, build a query, depending the research,
     *  that get only 10nth lines of the Computer's table and return them
     * 
     * Return a list of 10 Computer
     */
    public ResultSet searchByPage(String search, int numPage, int nbLine) throws DAOException {
        String query = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id FROM " +
                    "computer as c LEFT JOIN company as comp ON c.company_id = comp.id WHERE c.name LIKE ? OR " + 
                    "comp.name LIKE ? LIMIT ?, ?";
        ResultSet resultat = null;
        try {
            PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
            prepStatement.setString(1, '%'+search+'%');
            prepStatement.setString(2, '%'+search+'%');
            prepStatement.setInt(3, numPage);
            prepStatement.setInt(4, nbLine);

            resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[SEARCHBYPAGE] No data for request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("[SEARCHBYPAGE] Error on accessing data.");
        }

        return resultat;
    }

    /*
     * Insert the computer given in parameter into the database. Get each of its
     * parameters in order to build the query.
     * 
     * return the result of the query.
     */
    public int insert(Computer comp) throws DAOException {
        return this.insertOrUpdate(comp, 0);
    }

    /*
     * Update the computer given in parameter into the database. Get each of its
     * parameters in order to build the query.
     * 
     * return the result of the query.
     */
    public int update(Computer comp) throws DAOException {
        return this.insertOrUpdate(comp, 1);
    }

    /*
     * According to the given id in parameter,
     * build the query, find and delete it from the database.
     */
    public int delete(int id) throws SQLException {
        String query = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";
        PreparedStatement prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
        prepStatement.setInt(1, id);
        return prepStatement.executeUpdate();
    }

    private int insertOrUpdate(Computer comp, int typeOfRequest) throws DAOException {
        String name = comp.getName();
        LocalDate dateIntro = comp.getIntroduced();
        LocalDate dateDiscon = comp.getDiscontinued();
        int compIdRef = comp.getCompany_id();

        String strDateIntro = null, strDateDiscon = null;

        if (dateIntro != null)
            strDateIntro = dateIntro.format(formatter);

        if (dateDiscon != null)
            strDateDiscon = dateDiscon.format(formatter);

        String query = null;
        if (typeOfRequest == 0)// if Insert
            query = "INSERT INTO " + Utils.COMPUTER_TABLE
                    + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";
        else
            query = "UPDATE " + Utils.COMPUTER_TABLE
                    + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

        PreparedStatement prepStatement;
        int res = 0;
        try {
            prepStatement = (PreparedStatement) coMysql.prepareStatement(query);
            prepStatement.setString(1, name);
            prepStatement.setString(2, strDateIntro);
            prepStatement.setString(3, strDateDiscon);
            if (compIdRef != 0)
                prepStatement.setInt(4, compIdRef);
            else
                prepStatement.setString(4, null);

            if (typeOfRequest != 0)// if Update we have one more parameter
                prepStatement.setLong(5, comp.getId());
            res = prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("[INSORUPD] Error on accessing data.");
        }
        return res;
    }

}
