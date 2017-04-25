package fr.ebiz.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.utils.Utils;

public class ComputerDAO {

    static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

    private DateTimeFormatter formatter, formatterDB;

    private Connection co;

    /**
     * Constructor ComputerDAO.
     * @throws ConnectionException error on co db
     */
    public ComputerDAO() throws ConnectionException {
        // formatter for the LocalDateTime computer's fields
        formatterDB = DateTimeFormatter.ofPattern(Utils.FORMATTER_DB);
        formatter = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
        co = ConnectionDB.getInstance().getConnection();
    }

    /**
     * Get a computer by its ID.
     * @param idComp id computer.
     * @return ResultSet containing a Computer.
     * @throws DAOException error on getting data.
     */
    public Computer find(Long idComp) throws DAOException {
        String query = "SELECT * FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";
        Computer computer = null;
        try {
            PreparedStatement prepStatement = co.prepareStatement(query);
            prepStatement.setLong(1, idComp);
            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
            computer = toComputer(resultat);
        } catch (SQLException e) {
            throw new DAOException("[FIND] Error on accessing data.");
        }

        return computer;
    }

    /**
     * Get all the computer from the db.
     * @return ResultSet containing all Computers.
     * @throws DAOException error on getting data.
     */
    public ResultSet findAll() throws DAOException {
        String query = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
                + "FROM computer as c LEFT JOIN company as comp ON c.id = comp.id";

        ResultSet resultat = null;
        try {
            resultat = co.createStatement().executeQuery(query);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FINDALL] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[FINDALL] Error on accessing data.");
        }
        return resultat;
    }

    /**
     * Following the parameters, build a query that get only 10nth lines of the
     * Computer's table and return them.
     * @param numPage the page the user want to go on.
     * @param nbLine number of line to print.
     * @return a list of 10 Computer.
     * @throws DAOException error on getting data.
     */
    public ResultSet findByPage(int numPage, int nbLine) throws DAOException {
        String query = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
                + "FROM computer as c LEFT JOIN company as comp ON c.company_id = comp.id LIMIT ?, ?";

        ResultSet resultat = null;
        try {
            PreparedStatement prepStatement = co.prepareStatement(query);
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

    /**
     * Following the parameters, build a query, depending the research, that get
     * only 10nth lines of the Computer's table and return them.
     * @param search given by the user.
     * @param numPage the page the user wants to go on.
     * @param nbLine number of line to print.
     * @return a list of 10 Computer.
     * @throws DAOException error on getting data.
     */
    public ResultSet findBySearch(String search, int numPage, int nbLine) throws DAOException {
        String query = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id FROM "
                + "computer as c LEFT JOIN company as comp ON c.company_id = comp.id WHERE c.name LIKE ? OR "
                + "comp.name LIKE ? LIMIT ?, ?";
        ResultSet resultat = null;
        try {
            PreparedStatement prepStatement = co.prepareStatement(query);
            prepStatement.setString(1, '%' + search + '%');
            prepStatement.setString(2, '%' + search + '%');
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

    /**
     * Count number of computer in DB.
     * @return into ResultSet the number of computer.
     * @throws DAOException error on getting data.
     */
    public ResultSet count() throws DAOException {
        String query = "SELECT COUNT(*) as count FROM " + Utils.COMPUTER_TABLE;
        ResultSet resultat = null;

        try {
            resultat = co.createStatement().executeQuery(query);
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[COUNT] No data for request.");
            }
        } catch (SQLException e) {
            throw new DAOException("[COUNT] Error on accessing data.");
        }
        return resultat;
    }

    /**
     * Count number of computer in DB following the research given.
     * @param search type in by the user.
     * @return into ResultSet the number of computer depending the research.
     * @throws DAOException error on getting data.
     */
    public ResultSet countAfterSearch(String search) throws DAOException {
        String query = "SELECT COUNT(*) as count FROM "
                + "computer as c LEFT JOIN company as comp ON c.company_id = comp.id WHERE c.name LIKE ? OR "
                + "comp.name LIKE ?";
        ResultSet resultat = null;

        try {
            PreparedStatement prepStatement = co.prepareStatement(query);
            prepStatement.setString(1, '%' + search + '%');
            prepStatement.setString(2, '%' + search + '%');

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

    /**
     * Insert the computer given in parameter into the database. Get each of its
     * parameters in order to build the query.
     * @param comp the computer to insert
     * @return the result of the query.
     * @throws DAOException error on getting data.
     */
    public int insert(Computer comp) throws DAOException {
        String name = comp.getName();
        LocalDate dateIntro = comp.getIntroduced();
        LocalDate dateDiscon = comp.getDiscontinued();
        int compIdRef = comp.getCompanyId();

        String strDateIntro = null, strDateDiscon = null;

        if (dateIntro != null) {
            strDateIntro = dateIntro.format(formatter);
        }

        if (dateDiscon != null) {
            strDateDiscon = dateDiscon.format(formatter);
        }

        String query = "INSERT INTO " + Utils.COMPUTER_TABLE
                + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";

        PreparedStatement prepStatement;
        int res = 0;
        try {
            prepStatement = co.prepareStatement(query);
            prepStatement.setString(1, name);
            prepStatement.setString(2, strDateIntro);
            prepStatement.setString(3, strDateDiscon);
            if (compIdRef != 0) {
                prepStatement.setInt(4, compIdRef);
            } else {
                prepStatement.setString(4, null);
            }

            res = prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("[INSERT] Error on accessing data.");
        }
        return res;
    }

    /**
     * Update the computer given in parameter into the database. Get each of its
     * parameters in order to build the query.
     * @param comp the computer to update.
     * @return the result of the query.
     * @throws DAOException error on getting data.
     */
    public int update(Computer comp) throws DAOException {
        String name = comp.getName();
        LocalDate dateIntro = comp.getIntroduced();
        LocalDate dateDiscon = comp.getDiscontinued();
        int compIdRef = comp.getCompanyId();

        String strDateIntro = null, strDateDiscon = null;

        if (dateIntro != null) {
            strDateIntro = dateIntro.format(formatter);
        }

        if (dateDiscon != null) {
            strDateDiscon = dateDiscon.format(formatter);
        }

        String query = "UPDATE " + Utils.COMPUTER_TABLE
                + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

        PreparedStatement prepStatement;
        int res = 0;
        try {
            prepStatement = co.prepareStatement(query);
            prepStatement.setString(1, name);
            prepStatement.setString(2, strDateIntro);
            prepStatement.setString(3, strDateDiscon);
            if (compIdRef != 0) {
                prepStatement.setInt(4, compIdRef);
            } else {
                prepStatement.setString(4, null);
            }
            prepStatement.setLong(5, comp.getId());

            res = prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("[UPDATE] Error on accessing data.");
        }
        return res;
    }

    /**
     * According to the given id in parameter, build the query, find and delete
     * it from the database.
     * @param id the computer id to delete.
     * @return int depending the delete is done or not.
     * @throws SQLException error on co to db.
     */
    public int delete(int id) throws SQLException {
        String query = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";
        PreparedStatement prepStatement = co.prepareStatement(query);
        prepStatement.setInt(1, id);
        return prepStatement.executeUpdate();
    }

    /**
     * Build a Computer from the different data from the ResultSet pass in
     * parameter.
     * @param resultat contains a computer
     * @return a Computer object
     * @throws DAOException error on mapping computer
     */
    public Computer toComputer(ResultSet resultat) throws DAOException {
        Computer computer = null;
        try {
            Long id = resultat.getLong(Utils.COLUMN_ID);
            String name = resultat.getString(Utils.COLUMN_NAME);
            String strDateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
            String strDateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
            int compIdRef = resultat.getInt(Utils.COLUMN_COMPANYID);

            LocalDate dateIntro = null, dateDiscon = null;

            if (strDateIntro != null) {
                dateIntro = LocalDate.parse(strDateIntro, formatterDB);
            }

            if (strDateDiscon != null) {
                dateDiscon = LocalDate.parse(strDateDiscon, formatterDB);
            }

            computer = new Computer.Builder(name).introduced(dateIntro).discontinued(dateDiscon).companyId(compIdRef)
                    .id(id).build();

        } catch (SQLException e) {
            LOG.error("Error on reading data from db.");
            throw new DAOException("[TOCOMPUTER] Error on accessing data.");
        } catch (DateTimeParseException e) {
            LOG.error("Error on parsing date from db.");
            throw new DAOException("[TOCOMPUTER] Error on parsing date.");
        }
        return computer;
    }

    /**
     * Build a List of Computer from the different data from the ResultSet pass
     * in parameter.
     * @param resultat contains all computers
     * @return a List of Computer object
     * @throws DAOException error on mapping computer
     */
    public List<Computer> toComputers(ResultSet resultat) throws DAOException {
        List<Computer> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(toComputer(resultat));
            }
        } catch (SQLException sqle) {
            throw new DAOException("[TOCOMPUTERS] Error on accessing data.");
        }

        return list;
    }
}
