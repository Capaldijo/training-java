package fr.ebiz.computerdatabase.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import fr.ebiz.computerdatabase.model.utils.SearchFilter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exception.ConnectionException;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;
import fr.ebiz.computerdatabase.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ComputerDAO implements IComputerDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

    private DateTimeFormatter formatter, formatterDB;

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

    private static final String QUERY_SELECT = "SELECT c.id, c.name, c.introduced, c.discontinued, comp.name as company_id "
            + "FROM " + Utils.COMPUTER_TABLE;

    private static final String QUERY_FIND_BY_PAGE = "select new Computer(c.id, c.name, c.introduced, c.discontinued, c.company)"
            + " from Computer as c left join c.company";

    private static final String QUERY_COUNT = "select count(*) from Computer as c left join c.company" +
            " where c.name like ? or c.company.name like ?";

    private static final String QUERY_INSERT = "INSERT INTO " + Utils.COMPUTER_TABLE
            + " (name,introduced,discontinued,company_id) VALUES (?, ?, ?, ?)";

    private static final String QUERY_UPDATE = "UPDATE " + Utils.COMPUTER_TABLE
            + " SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE id = ?";

    private static final String QUERY_DELETE_COMP_IDREF = "DELETE FROM " + Utils.COMPUTER_TABLE + " WHERE company_id = ?";

    private JdbcTemplate jdbcTemplate;

    private SessionFactory sessionFactory;

    /**
     * Constructor ComputerDAO.
     * @param jdbcTemplate query handler.
     * @param sessionFactory .
     * @throws ConnectionException error on co db
     */
    @Autowired
    public ComputerDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
        // formatter for the LocalDateTime computer's fields
        formatterDB = DateTimeFormatter.ofPattern(Utils.FORMATTER_DB);
        formatter = DateTimeFormatter.ofPattern(Utils.FORMATTER_WEB);
    }

    @Override
    public Computer find(Long idComp) throws DAOException {
        Computer computer = null;
        /*try {
            computer = jdbcTemplate.queryForObject(QUERY_FIND, new Object[] {idComp}, new ComputerRowMapper());
        } catch (DataAccessException e) {
            LOG.info("[FIND] Error queryForObject.");
            throw new DAOException("[FIND] Error queryForObject.");
        }*/
        return sessionFactory.getCurrentSession().get(Computer.class, idComp);
    }

    @Override
    public List<Computer> findByPage(PaginationFilters filters, int numPage, int nbLine) throws DAOException {

        List<Computer> list = null;

        StringBuilder query = new StringBuilder();
        query.append(QUERY_FIND_BY_PAGE);
        try {
            // If we have at least one column filtered
            Iterator<String> it = filters.getFilteredColumns().iterator();
            if (it.hasNext()) {
                query.append(" where ");
                while (it.hasNext()) {
                    String col = it.next();
                    query.append(col + " " + filters.getFilterValue(col).getOperator() + " ? ");
                    if (it.hasNext()) {
                        query.append(" or ");
                    }
                }
            }

            if (filters.getOrderBy() != null) {
                query.append("ORDER BY " + filters.getOrderBy() + " " + (filters.isAsc() ? " ASC " : " DESC "));
            }

            // By default, createQuery return raw type, so we have to cast to Computer type.
            TypedQuery<Computer> queryDB = sessionFactory.getCurrentSession().createQuery(query.toString(), Computer.class);

            // Following the query, we have to set each parameter
            // depending the filters that have been set.
            int paramCount = 0;
            for (SearchFilter op : filters.getFilterValues()) {
                queryDB.setParameter(paramCount++, op.getValue());
            }

            queryDB.setFirstResult(numPage).setMaxResults(nbLine);

            list = queryDB.getResultList();
        } catch (DataAccessException e) {
            LOG.info("[FIND_BY_SEARCH] Error on accessing data");
            throw new DAOException("[FIND_BY_SEARCH] Error on accessing data.");
        }
        return list;
    }

    @Override
    public int count(String search) throws DAOException {
        int count = 0;

        try {
            String s = '%' + search + '%';
            count = (int) (long) sessionFactory.getCurrentSession().createQuery(QUERY_COUNT)
                    .setParameter(0, s)
                    .setParameter(1, s)
                    .list().get(0);

        } catch (DataAccessException e) {
            LOG.info("[COUNT] Error queryForObject.");
            throw new DAOException("[COUNT] Error queryForObject.");
        }
        return count;
    }

    @Override
    public int insert(Computer comp) throws DAOException {
        int res = 1;
        sessionFactory.getCurrentSession().save(comp);
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
        /*try {
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
        }*/
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

    @Override
    public int deleteFromCompanyId(String id) throws SQLException, DAOException {
        int res = 0;
        try {
            res = jdbcTemplate.update(QUERY_DELETE_COMP_IDREF, id);
        } catch (DataAccessException e) {
            LOG.error("[DELETE] Error on jdbcTemplate.update.");
            throw new DAOException("[DELETE] Error on jdbcTemplate.update.");
        }
        return res;
    }
}
