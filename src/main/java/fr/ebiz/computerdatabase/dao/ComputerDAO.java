package fr.ebiz.computerdatabase.dao;

import java.util.Iterator;
import java.util.List;

import fr.ebiz.computerdatabase.model.utils.SearchFilter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.model.Computer;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ComputerDAO implements IComputerDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

    private static final String QUERY_FIND = "from Computer WHERE id = ?";

    private static final String QUERY_FIND_BY_PAGE = "select new Computer(c.id, c.name, c.introduced, c.discontinued, c.company)"
            + " from Computer as c left join c.company";

    private static final String QUERY_COUNT = "select count(*) from Computer as c left join c.company" +
            " where c.name like ? or c.company.name like ?";

    private static final String QUERY_UPDATE = "UPDATE Computer SET name = ?,  introduced = ?," +
            " discontinued = ?, company = ? WHERE id = ?";

    private static final String QUERY_DELETE = "DELETE FROM Computer WHERE id = ?";

    private SessionFactory sessionFactory;

    /**
     * Constructor ComputerDAO.
     * @param sessionFactory handler for connections and creation of query.
     */
    @Autowired
    public ComputerDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Computer find(Long idComp) throws DAOException {
        TypedQuery<Computer> query = sessionFactory.getCurrentSession().createQuery(QUERY_FIND, Computer.class);
        try {
            return query.setParameter(0, idComp).getResultList().get(0);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Computer> findByPage(PaginationFilters filters, int numPage, int nbLine) throws DAOException {
        StringBuilder query = new StringBuilder();
        query.append(QUERY_FIND_BY_PAGE);
        try {
            // If we have at least one column filtered
            Iterator<String> it = filters.getFilteredColumns().iterator();
            if (it.hasNext()) {
                query.append(" where ");
                while (it.hasNext()) {
                    String col = it.next();
                    query.append(col)
                            .append(" ")
                            .append(filters.getFilterValue(col).getOperator())
                            .append(" ? ");
                    if (it.hasNext()) {
                        query.append(" or ");
                    }
                }
            }

            if (filters.getOrderBy() != null) {
                query.append("order by ")
                        .append(filters.getOrderBy())
                        .append(" ")
                        .append(filters.isAsc() ? " asc " : " desc ");
            }

            // By default, createQuery return raw type, so we have to cast to Computer type.
            TypedQuery<Computer> queryDB = sessionFactory.getCurrentSession().createQuery(query.toString(), Computer.class);

            // Following the query, we have to set each parameter
            // depending the filters that have been set.
            int paramCount = 0;
            for (SearchFilter op : filters.getFilterValues()) {
                queryDB.setParameter(paramCount++, op.getValue());
            }

            // query Limit, explicit methods.
            queryDB.setFirstResult(numPage).setMaxResults(nbLine);

            return queryDB.getResultList();
        } catch (Exception e) {
            LOG.info(e.getMessage());
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public int count(String search) throws DAOException {
        try {
            String s = '%' + search + '%';
            return (int) (long) sessionFactory.getCurrentSession().createQuery(QUERY_COUNT)
                    .setParameter(0, s)
                    .setParameter(1, s)
                    .list().get(0);
        } catch (RuntimeException e) {
            LOG.info("[COUNT] " + e.getMessage());
            throw new DAOException("[COUNT]" + e.getMessage());
        }
    }

    @Override
    public int insert(Computer comp) throws DAOException {
        return (int) (long) sessionFactory.getCurrentSession().save(comp);
    }

    @Override
    public int update(Computer comp) throws DAOException {
        try {
            return (int) (long) sessionFactory.getCurrentSession().createQuery(QUERY_UPDATE)
                    .setParameter(0, comp.getName()).setParameter(1, comp.getIntroduced())
                    .setParameter(2, comp.getDiscontinued()).setParameter(3, comp.getCompany())
                    .setParameter(4, comp.getId()).executeUpdate();
        } catch (Exception e) {
            LOG.error("[UPDATE] " + e.getMessage());
            throw new DAOException("[UPDATE] " + e.getMessage());
        }
    }

    @Override
    public int delete(String id) {
        int res = 0;
        try {
            res = (int) (long) sessionFactory.getCurrentSession().createQuery(QUERY_DELETE)
                    .setParameter(0, Long.parseLong(id))
                    .executeUpdate();
        } catch (Exception e) {
            LOG.error("[DELETE] Error on HQL delete.");
        }
        return res;
    }

    @Override
    public int delete(String... computersId) {
        int res = 1;
        for (String id : computersId) {
            if (delete(id) < 1) {
                res = -1;
                LOG.error("[DELETE] computerId: " + id + " not deleted");
            }
        }
        return res;
    }
}
