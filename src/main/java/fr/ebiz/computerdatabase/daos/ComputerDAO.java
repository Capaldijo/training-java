package fr.ebiz.computerdatabase.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.List;

import fr.ebiz.computerdatabase.mappers.ComputerRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.interfaces.IComputerDAO;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.models.Operator;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ComputerDAO implements IComputerDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

    private DateTimeFormatter formatter, formatterDB;

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

    private static final String QUERY_SELECT = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
            + "FROM " + Utils.COMPUTER_TABLE;

    private static final String QUERY_FINDALL = QUERY_SELECT + " as c LEFT JOIN company as comp ON c.company_id = comp.id";

    private static final String QUERY_COUNT = "SELECT COUNT(*) as count FROM " + Utils.COMPUTER_TABLE;

    private static final String QUERY_COUNTAFTERSEARCH = QUERY_COUNT + " as c LEFT JOIN company as" +
            " comp ON c.company_id = comp.id WHERE c.name LIKE ? OR comp.name LIKE ?";

    private static final String QUERY_INSERT = "INSERT INTO " + Utils.COMPUTER_TABLE
            + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";

    private static final String QUERY_UPDATE = "UPDATE " + Utils.COMPUTER_TABLE
            + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

    private static final String QUERY_DELETECOMPIDREF = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE company_id = ?";

    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor ComputerDAO.
     * @param jdbcTemplate .
     * @throws ConnectionException error on co db
     */
    @Autowired
    public ComputerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // formatter for the LocalDateTime computer's fields
        formatterDB = DateTimeFormatter.ofPattern(Utils.FORMATTER_DB);
        formatter = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
    }

    @Override
    public Computer find(Long idComp) throws DAOException {
        Computer computer = null;
        try {
            computer = jdbcTemplate.queryForObject(QUERY_FIND, new Object[] {idComp}, new ComputerRowMapper());
        } catch (DataAccessException e) {
            LOG.info("[FIND] Error queryForObject.");
            throw new DAOException("[FIND] Error queryForObject.");
        }
        return computer;
    }

    @Override
    public List<ComputerDTO> findByPage(PaginationFilters filters, int numPage, int nbLine) throws DAOException {

        List<ComputerDTO> list = null;

        StringBuilder query = new StringBuilder();
        query.append(QUERY_FINDALL);
        try {
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

            list = jdbcTemplate.query(query.toString(),
                    filters.getFilterValues().stream().map(Operator::getValue).toArray(),
                    new ComputerDTORowMapper());
        } catch (DataAccessException e) {
            LOG.info("[FINDBYSEARCH] Error on accessing data");
            throw new DAOException("[FINDBYSEARCH] Error on accessing data.");
        }
        return list;
    }

    @Override
    public int count() throws DAOException {
        int count = 0;

        try {
            count = jdbcTemplate.queryForObject(QUERY_COUNT, Integer.class);
        } catch (DataAccessException e) {
            LOG.info("[COUNT] Error queryForObject.");
            throw new DAOException("[COUNT] Error queryForObject.");
        }
        return count;
    }

    @Override
    public int countAfterSearch(String search) throws DAOException {
        int count = 0;

        try {
            String s = '%' + search + '%';
            count = jdbcTemplate.queryForObject(QUERY_COUNTAFTERSEARCH, new Object[] {s, s}, Integer.class);
        } catch (DataAccessException e) {
            LOG.info("[COUNT] Error queryForObject.");
            throw new DAOException("[COUNT] Error queryForObject.");
        }
        return count;
    }

    @Override
    public int insert(Computer comp) throws DAOException {
        LocalDate dateIntro = comp.getIntroduced();
        LocalDate dateDiscon = comp.getDiscontinued();

        String strDateIntro = null, strDateDiscon = null;

        if (dateIntro != null) {
            strDateIntro = dateIntro.format(formatter);
        }
        if (dateDiscon != null) {
            strDateDiscon = dateDiscon.format(formatter);
        }

        int res = 0;
        try {
            Object[] objects = null;
            if (comp.getCompanyId() != 0) {
                objects = new Object[] {comp.getName(), strDateIntro, strDateDiscon, comp.getCompanyId()};
            } else {
                objects = new Object[] {comp.getName(), strDateIntro, strDateDiscon, null};
            }
            res = jdbcTemplate.update(QUERY_INSERT, objects);
        } catch (DataAccessException e) {
            e.printStackTrace();
            LOG.error("[INSERT] Error on jdbcTemplate.update.");
            throw new DAOException("[INSERT] Error on jdbcTemplate.update.");
        }

        return res;
    }

    @Override
    public int update(Computer comp) throws DAOException {
        LocalDate dateIntro = comp.getIntroduced();
        LocalDate dateDiscon = comp.getDiscontinued();

        String strDateIntro = null, strDateDiscon = null;

        if (dateIntro != null) {
            strDateIntro = dateIntro.format(formatter);
        }
        if (dateDiscon != null) {
            strDateDiscon = dateDiscon.format(formatter);
        }

        int res = 0;
        try {
            Object[] objects = null;
            if (comp.getCompanyId() != 0) {
                objects = new Object[] {comp.getName(), strDateIntro, strDateDiscon, comp.getCompanyId()};
            } else {
                objects = new Object[] {comp.getName(), strDateIntro, strDateDiscon, null};
            }
            res = jdbcTemplate.update(QUERY_UPDATE, objects);
        } catch (DataAccessException e) {
            LOG.error("[UPDATE] Error on jdbcTemplate.update.");
            throw new DAOException("[UPDATE] Error on jdbcTemplate.update.");
        }
        return res;
    }

    @Override
    public int delete(String id) throws SQLException, DAOException {
        int res = 0;
        try {
            res = jdbcTemplate.update(QUERY_DELETE, id);
        } catch (DataAccessException e) {
            LOG.error("[DELETE] Error on jdbcTemplate.update.");
            throw new DAOException("[DELETE] Error on jdbcTemplate.update.");
        }
        return res;
    }

    @Override
    public int delete(String... computersId) throws DAOException {
        int res = 1;
        for (String id : computersId) {
            try {
                if (delete(id) == 1) {
                    LOG.info("[DELETE] computerId: " + id + " deleted");
                } else {
                    res = -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("[DELETE] error on deleting computers");
                throw new DAOException("[DELETE] error on deleting computers");
            }
        }
        return res;
    }

    /**
     *  Delete all computers relating to company id ref.
     * @param id of the company given.
     * @return 1 if deleted else 0.
     * @throws SQLException Error on id given.
     * @throws DAOException Error on connecting to db.
     */
    @Override
    public int deleteFromCompanyId(String id) throws SQLException, DAOException {
        int res = 0;
        try {
            res = jdbcTemplate.update(QUERY_DELETECOMPIDREF, id);
        } catch (DataAccessException e) {
            LOG.error("[DELETE] Error on jdbcTemplate.update.");
            throw new DAOException("[DELETE] Error on jdbcTemplate.update.");
        }
        return res;
    }

    private class ComputerDTORowMapper implements RowMapper<ComputerDTO> {
        private final Logger LOG = LoggerFactory.getLogger(ComputerRowMapper.class);

        @Override
        public ComputerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ComputerDTO computer = null;
            try {
                String id = rs.getString(Utils.COLUMN_ID);
                String name = rs.getString(Utils.COLUMN_NAME);
                String dateIntro = rs.getString(Utils.COLUMN_INTRODUCED);
                String dateDiscon = rs.getString(Utils.COLUMN_DISCONTINUED);
                String compIdRef = rs.getString(Utils.COLUMN_COMPANYID);

                if (dateIntro != null) {
                    dateIntro = dateIntro.split(" ")[0];
                }
                if (dateDiscon != null) {
                    dateDiscon = dateDiscon.split(" ")[0];
                }

                computer = new ComputerDTO.Builder(name).introduced(dateIntro).discontinued(dateDiscon).companyId(compIdRef)
                        .id(id).build();

            } catch (SQLException | DateTimeParseException e) {
                e.printStackTrace();
                LOG.error("[TOCOMPUTER] Error on parsing date.");
                throw new SQLException("[TOCOMPUTER] Error on parsing date.");
            }
            return computer;
        }
    }
}
