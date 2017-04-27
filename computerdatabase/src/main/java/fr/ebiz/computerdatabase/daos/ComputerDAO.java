package fr.ebiz.computerdatabase.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.interfaces.DAOInterface;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.persistence.ConnectionDB;
import fr.ebiz.computerdatabase.persistence.Operator;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

public class ComputerDAO implements DAOInterface<ComputerDTO, Computer> {

    static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

    private DateTimeFormatter formatter, formatterDB;

    private Connection co;

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

    private static final String QUERY_SELECT = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
            + "FROM " + Utils.COMPUTER_TABLE;

    private static final String QUERY_FINDALL = QUERY_SELECT + " as c LEFT JOIN company as comp ON c.id = comp.id";

    private static final String QUERY_FINDBYPAGE = QUERY_FINDALL + " LIMIT ?, ?";

    private static final String QUERY_FINDBYSEARCH = QUERY_FINDALL + " WHERE c.name LIKE ? OR "
            + "comp.name LIKE ? LIMIT ?, ?";

    private static final String QUERY_COUNT = "SELECT COUNT(*) as count FROM " + Utils.COMPUTER_TABLE;

    private static final String QUERY_COUNTAFTERSEARCH = QUERY_COUNT + " as c LEFT JOIN company as" +
            " comp ON c.company_id = comp.id WHERE c.name LIKE ? OR comp.name LIKE ?";

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

    @Override
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
            computer = toModel(resultat);
            co.close();
        } catch (SQLException | ConnectionException e) {
            throw new DAOException("[FIND] Error on accessing data.");
        }

        return computer;
    }

    @Override
    public List<Computer> findAll() throws DAOException {

        List<Computer> list = null;
        try {
            co = ConnectionDB.getInstance().getConnection();
            ResultSet resultat = co.createStatement().executeQuery(QUERY_FINDALL);
            if (!resultat.isBeforeFirst()) {
                LOG.error("[FINDALL] No data for request.");
                throw new DAOException("[FINDALL] No data for request.");
            }
            list = toModels(resultat);
        } catch (SQLException | ConnectionException e) {
            LOG.error("[FINDALL] Error on accessing data.");
            throw new DAOException("[FINDALL] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                throw new DAOException("[FINDALL] Error on close co.");
            }
        }
        return list;
    }

    @Override
    public List<ComputerDTO> findByPage(PaginationFilters filters, int numPage, int nbLine) throws DAOException {

        List<ComputerDTO> list = null;
        //        try {
        //            co = ConnectionDB.getInstance().getConnection();
        //            PreparedStatement prepStatement = null;
        //            if (filters != null) {
        //                String search = filters.getSearch();
        //                String orderBy = filters.getOrderBy();
        //                String ascOrDesc = filters.getAscOrDesc();
        //                StringBuilder query = new StringBuilder();
        //                if (search != null) { // we search and maybe order
        //                    query.append(QUERY_FINDBYSEARCH);
        //                    if (orderBy != null) {
        //                        query.append(" ORDER BY ? ?");
        //                    }
        //                    prepStatement = co.prepareStatement(query.toString());
        //                    prepStatement.setString(1, '%' + search + '%');
        //                    prepStatement.setString(2, '%' + search + '%');
        //                    prepStatement.setInt(3, numPage);
        //                    prepStatement.setInt(4, nbLine);
        //                    if (orderBy != null) {
        //                        prepStatement.setString(5, orderBy);
        //                        prepStatement.setString(6, ascOrDesc.toUpperCase());
        //                    }
        //                } else { // we only order by
        //                    query.append(QUERY_FINDBYPAGE);
        //                    query.append(" ORDER BY ? ?");
        //                    prepStatement = co.prepareStatement(query.toString());
        //                    prepStatement.setInt(1, numPage);
        //                    prepStatement.setInt(2, nbLine);
        //                    prepStatement.setString(3, orderBy);
        //                    prepStatement.setString(4, ascOrDesc.toUpperCase());
        //                }
        //            } else {
        //                prepStatement = co.prepareStatement(QUERY_FINDBYPAGE);
        //                prepStatement.setInt(1, numPage);
        //                prepStatement.setInt(2, nbLine);
        //            }
        //            ResultSet resultat = prepStatement.executeQuery();
        //            if (!resultat.isBeforeFirst()) {
        //                LOG.error("[FINDBYSEARCH] No data for request.");
        //                throw new DAOException("[FINDBYSEARCH] No data for request.");
        //            }
        //            list = toDTOs(resultat);
        //        } catch (SQLException | ConnectionException e) {
        //            LOG.error("[FINDBYSEARCH] Error on accessing data.");
        //            throw new DAOException("[FINDBYSEARCH] Error on accessing data.");
        //        } finally {
        //            try {
        //                co.close();
        //            } catch (SQLException e) {
        //                throw new DAOException("[FINDBYSEARCH] Error on close co.");
        //            }
        //        }
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        query.append(QUERY_FINDALL);
        try {
            co = ConnectionDB.getInstance().getConnection();
            // If we have at least one column filtered
            Iterator<String> it = filters.getFilteredColumns().iterator();
            if (it.hasNext()) {
                query.append(" WHERE ");
                while (it.hasNext()) {
                    String col = it.next();
                    query.append(col + " " + filters.getFilterValue(col).getOperator() + " ? ");
                    if (it.hasNext()) {
                        query.append(" OR ");
                    }
                }
            }
            if (filters.getOrderBy() != null) {
                query.append("ORDER BY " + filters.getOrderBy() + " " + (filters.isAsc() ? " ASC " : " DESC "));
            }

            query.append(" LIMIT " + numPage + " , " + nbLine + " ");

            preparedStatement = co.prepareStatement(query.toString());
            int paramCount = 1;

            for (Operator op : filters.getFilterValues()) {
                preparedStatement.setString(paramCount++, op.getValue());
                System.out.println(paramCount);
            }
            rs = preparedStatement.executeQuery();
            //co.commit();
            list = toDTOs(rs);

            LOG.info("Succes getFromFilter ComputerDAO" + preparedStatement);
        } catch (SQLException | ConnectionException e) {
            LOG.info("Erreur getFromFilter ComputerDAO : " + e.getMessage() + " query built : " + query.toString());
        } finally {
            try {
                preparedStatement.close();
                co.close();
            } catch (SQLException e) {
                throw new DAOException("[FINDBYSEARCH] Error on close co.");
            }
        }

        return list;
    }

    @Override
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
        } catch (SQLException | ConnectionException e) {
            LOG.error("[COUNT] Error on accessing data.");
            throw new DAOException("[COUNT] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                throw new DAOException("[COUNT] Error on close co.");
            }
        }
        return count;
    }

    @Override
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
        } catch (SQLException | ConnectionException e) {
            LOG.error("[COUNTSEARCH] Error on accessing data.");
            throw new DAOException("[COUNTSEARCH] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                throw new DAOException("[COUNTSEARCH] Error on close co.");
            }
        }
        return count;
    }

    @Override
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
        } catch (SQLException | ConnectionException e) {
            LOG.error("[INSERT] Error on accessing data.");
            throw new DAOException("[INSERT] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                throw new DAOException("[INSERT] Error on close co.");
            }
        }
        return res;
    }

    @Override
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
        } catch (SQLException | ConnectionException e) {
            LOG.error("[UPDATE] Error on accessing data.");
            throw new DAOException("[UPDATE] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                throw new DAOException("[UPDATE] Error on close co.");
            }
        }
        return res;
    }

    @Override
    public int delete(String id) throws SQLException, ConnectionException {

        co = ConnectionDB.getInstance().getConnection();
        PreparedStatement prepStatement = co.prepareStatement(QUERY_DELETE);
        prepStatement.setString(1, id);
        int res = prepStatement.executeUpdate();
        co.close();
        return res;
    }

    @Override
    public int delete(String... computersId) throws DAOException {
        int res = 1;
        for (String id : computersId) {
            try {
                if (delete(id) == 1) {
                    LOG.info("[DELETE] computerId:" + id + " deleted");
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

    @Override
    public Computer toModel(ResultSet resultat) throws DAOException {
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

    @Override
    public List<Computer> toModels(ResultSet resultat) throws DAOException {
        List<Computer> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(toModel(resultat));
            }
        } catch (SQLException sqle) {
            LOG.error("[TOCOMPUTERS] Error on accessing data.");
            throw new DAOException("[TOCOMPUTERS] Error on accessing data.");
        }

        return list;
    }

    @Override
    public ComputerDTO toDTO(ResultSet resultat) throws DAOException {
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

    @Override
    public List<ComputerDTO> toDTOs(ResultSet resultat) throws DAOException {
        List<ComputerDTO> list = new ArrayList<>();

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
    public Computer find(int id) throws DAOException {
        LOG.error("[FIND INT] Not implemented yet.");
        throw new RuntimeException("[FIND INT] Not implemented yet.");
    }
}
