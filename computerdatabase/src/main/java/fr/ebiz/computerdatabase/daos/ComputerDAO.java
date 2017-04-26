package fr.ebiz.computerdatabase.daos;

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
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.persistence.ConnectionDB;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

public class ComputerDAO {

    static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

    private DateTimeFormatter formatter, formatterDB;

    private Connection co;

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

    private static final String QUERY_FINDALL = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
            + "FROM " + Utils.COMPUTER_TABLE + " as c LEFT JOIN company as comp ON c.id = comp.id";

    private static final String QUERY_FINDBYPAGE = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
            + "FROM " + Utils.COMPUTER_TABLE + " as c LEFT JOIN company as comp ON c.company_id = comp.id LIMIT ?, ?";

    private static final String QUERY_FINDBYSEARCH = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id FROM "
            + Utils.COMPUTER_TABLE + " as c LEFT JOIN company as comp ON c.company_id = comp.id WHERE c.name LIKE ? OR "
            + "comp.name LIKE ? LIMIT ?, ?";

    private static final String QUERY_COUNT = "SELECT COUNT(*) as count FROM " + Utils.COMPUTER_TABLE;

    private static final String QUERY_COUNTAFTERSEARCH = "SELECT COUNT(*) as count FROM "
            + "computer as c LEFT JOIN company as comp ON c.company_id = comp.id WHERE c.name LIKE ? OR "
            + "comp.name LIKE ?";

    private static final String QUERY_INSERT = "INSERT INTO " + Utils.COMPUTER_TABLE
            + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";

    private static final String QUERY_UPDATE = "UPDATE " + Utils.COMPUTER_TABLE
            + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

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
     * @return A Computer.
     * @throws DAOException error on getting data.
     */
    public Computer find(Long idComp) throws DAOException {

        Computer computer = null;
        try {
            co = ConnectionDB.getInstance().getConnection();
            PreparedStatement prepStatement = co.prepareStatement(QUERY_FIND);
            prepStatement.setLong(1, idComp);
            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                throw new DAOException("[FIND] No data for request.");
            }
            resultat.next();
            computer = toComputer(resultat);
            co.close();
        } catch (SQLException | ConnectionException e) {
            throw new DAOException("[FIND] Error on accessing data.");
        }

        return computer;
    }

    /**
     * Get all the computer from the db.
     * @return All Computers.
     * @throws DAOException error on getting data.
     */
    public List<Computer> findAll() throws DAOException {

        List<Computer> list = null;
        try {
            co = ConnectionDB.getInstance().getConnection();
            ResultSet resultat = co.createStatement().executeQuery(QUERY_FINDALL);
            if (!resultat.isBeforeFirst()) {
                LOG.error("[FINDALL] No data for request.");
                throw new DAOException("[FINDALL] No data for request.");
            }
            list = toComputers(resultat);
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[FINDALL] Error on accessing data.");
            throw new DAOException("[FINDALL] Error on accessing data.");
        }
        return list;
    }

    /**
     * Following the parameters, build a query that get only 10nth lines of the
     * Computer's table and return them.
     * @param numPage the page the user want to go on.
     * @param nbLine number of line to print.
     * @return a list of 10 Computer.
     * @throws DAOException error on getting data.
     */
    public List<ComputerDTO> findByPage(int numPage, int nbLine) throws DAOException {

        List<ComputerDTO> list = null;
        try {
            co = ConnectionDB.getInstance().getConnection();
            PreparedStatement prepStatement = co.prepareStatement(QUERY_FINDBYPAGE);
            prepStatement.setInt(1, numPage);
            prepStatement.setInt(2, nbLine);

            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                LOG.error("[FINDBYPAGE] No data for request.");
                throw new DAOException("[FINDBYPAGE] No data for request.");
            }
            list = toComputerDTOs(resultat);
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[FINDBYPAGE] Error on accessing data.");
            throw new DAOException("[FINDBYPAGE] Error on accessing data.");
        }

        return list;
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
    public List<ComputerDTO> findBySearch(String search, int numPage, int nbLine) throws DAOException {

        List<ComputerDTO> list = null;
        try {
            co = ConnectionDB.getInstance().getConnection();
            PreparedStatement prepStatement = co.prepareStatement(QUERY_FINDBYSEARCH);
            prepStatement.setString(1, '%' + search + '%');
            prepStatement.setString(2, '%' + search + '%');
            prepStatement.setInt(3, numPage);
            prepStatement.setInt(4, nbLine);

            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                LOG.error("[FINDBYSEARCH] No data for request.");
                throw new DAOException("[FINDBYSEARCH] No data for request.");
            }
            list = toComputerDTOs(resultat);
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[FINDBYSEARCH] Error on accessing data.");
            throw new DAOException("[FINDBYSEARCH] Error on accessing data.");
        }

        return list;
    }

    /**
     * Count number of computer in DB.
     * @return The number of computer.
     * @throws DAOException error on getting data.
     */
    public int count() throws DAOException {

        int count = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            ResultSet resultat = co.createStatement().executeQuery(QUERY_COUNT);
            if (!resultat.isBeforeFirst()) {
                LOG.error("[COUNT] No data for request.");
                throw new DAOException("[COUNT] No data for request.");
            }
            resultat.next();
            count = resultat.getInt("count");
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[COUNT] Error on accessing data.");
            throw new DAOException("[COUNT] Error on accessing data.");
        }
        return count;
    }

    /**
     * Count number of computer in DB following the research given.
     * @param search type in by the user.
     * @return The number of computer depending the research.
     * @throws DAOException error on getting data.
     */
    public int countAfterSearch(String search) throws DAOException {

        int count = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            PreparedStatement prepStatement = co.prepareStatement(QUERY_COUNTAFTERSEARCH);
            prepStatement.setString(1, '%' + search + '%');
            prepStatement.setString(2, '%' + search + '%');

            ResultSet resultat = prepStatement.executeQuery();
            if (!resultat.isBeforeFirst()) {
                LOG.error("[COUNTSEARCH] No data for request.");
                throw new DAOException("[COUNTSEARCH] No data for request.");
            }
            resultat.next();
            count = resultat.getInt("count");
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[COUNTSEARCH] Error on accessing data.");
            throw new DAOException("[COUNTSEARCH] Error on accessing data.");
        }
        return count;
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

        PreparedStatement prepStatement;
        int res = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            prepStatement = co.prepareStatement(QUERY_INSERT);
            prepStatement.setString(1, name);
            prepStatement.setString(2, strDateIntro);
            prepStatement.setString(3, strDateDiscon);
            if (compIdRef != 0) {
                prepStatement.setInt(4, compIdRef);
            } else {
                prepStatement.setString(4, null);
            }

            res = prepStatement.executeUpdate();
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[INSERT] Error on accessing data.");
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

        PreparedStatement prepStatement;
        int res = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            prepStatement = co.prepareStatement(QUERY_UPDATE);
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
            co.close();
        } catch (SQLException | ConnectionException e) {
            LOG.error("[UPDATE] Error on accessing data.");
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
     * @throws ConnectionException Error on accessing data.
     */
    public int delete(String id) throws SQLException, ConnectionException {

        co = ConnectionDB.getInstance().getConnection();
        PreparedStatement prepStatement = co.prepareStatement(QUERY_DELETE);
        prepStatement.setString(1, id);
        int res = prepStatement.executeUpdate();
        co.close();
        return res;
    }

    /**
     * Delete all computer contained in parameter.
     * @param computersId list of computer's id to deletes
     * @throws DAOException error on accessing data.
     * @return res.
     */
    public int deleteComputers(String...computersId) throws DAOException {
        int res = 0;
        for (String id : computersId) {
            try {
                if (delete(id) == 1) {
                    LOG.info("[DELETE] computerId:" + id + " deleted");
                    res = 1;
                } else {
                    res = -1;
                }
            } catch (SQLException | ConnectionException e) {
                LOG.error("[DELETE] error on deleting computers");
                throw new DAOException("[DELETE] error on deleting computers");
            }
        }
        return res;
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
            LOG.error("[TOCOMPUTER] Error on reading data from db.");
            throw new DAOException("[TOCOMPUTER] Error on accessing data.");
        } catch (DateTimeParseException e) {
            LOG.error("[TOCOMPUTER] Error on parsing date from db.");
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
            LOG.error("[TOCOMPUTERS] Error on accessing data.");
            throw new DAOException("[TOCOMPUTERS] Error on accessing data.");
        }

        return list;
    }

    /**
     * Build a computerDTO directly from the db.
     * @param resultat contains a computer
     * @return a computerDTO
     * @throws DAOException Error on mapping data
     */
    public ComputerDTO toComputerDTO(ResultSet resultat) throws DAOException {
        ComputerDTO computerDTO = null;
        try {
            String id = resultat.getString(Utils.COLUMN_ID);
            String name = resultat.getString(Utils.COLUMN_NAME);
            String dateIntro = resultat.getString(Utils.COLUMN_INTRODUCED);
            String dateDiscon = resultat.getString(Utils.COLUMN_DISCONTINUED);
            String compIdRef = resultat.getString(Utils.COLUMN_COMPANYID);

            if (dateIntro != null) {
                dateIntro = dateIntro.split(" ")[0];
            }
            if (dateDiscon != null) {
                dateDiscon = dateDiscon.split(" ")[0];
            }

            computerDTO = new ComputerDTO.Builder(name).
                    introduced(dateIntro)
                    .discontinued(dateDiscon)
                    .companyId(compIdRef)
                    .id(id).build();

        } catch (SQLException e) {
            LOG.error("[TOCOMPUTERDTO] Error on accessing data.");
            throw new DAOException("[TOCOMPUTERDTO] Error on accessing data.");
        } catch (DateTimeParseException e) {
            LOG.error("[TOCOMPUTERDTO] Error on parsing date.");
            throw new DAOException("[TOCOMPUTERDTO] Error on parsing date.");
        }
        return computerDTO;
    }

    /**
     * Build a list of computerDTO directly from the db.
     * @param resultat contains all the computer from db
     * @return a list of computerDTO
     * @throws DAOException Error on mapping data
     */
    public List<ComputerDTO> toComputerDTOs(ResultSet resultat) throws DAOException {
        List<ComputerDTO> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(toComputerDTO(resultat));
            }
        } catch (SQLException sqle) {
            LOG.error("[TOCOMPUTERDTOS]Error on reading data from db.");
            throw new DAOException("[TOCOMPUTERDTOS]Error on reading data from db.");
        }
        return list;
    }
}
