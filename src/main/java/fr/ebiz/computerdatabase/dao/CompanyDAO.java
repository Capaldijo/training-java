package fr.ebiz.computerdatabase.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.mapper.CompanyMapper;
import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.model.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class CompanyDAO implements ICompanyDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private static final String QUERY_FIND = "from Company where Company.id = ?";

    private static final String QUERY_FIND_ALL = "from Company";

    private static final String QUERY_DELETE = "delete from Company where Company.id = ?";

    private SessionFactory sessionFactory;

    /**
     * Constructor companyDAO.
     * @param sessionFactory handle the connections and creation of query.
     */
    @Autowired
    public CompanyDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Company find(Long id) throws DAOException {
        TypedQuery<Company> queryDB = sessionFactory.getCurrentSession().createQuery(QUERY_FIND, Company.class);
        Company company = null;
        try {
            company = queryDB.setParameter(0, id).getResultList().get(0);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        return company;
    }

    @Override
    public List<Company> findAll() throws DAOException {
        TypedQuery<Company> queryDB = sessionFactory.getCurrentSession().createQuery(QUERY_FIND_ALL, Company.class);
        List<Company> list = null;
        try {
            list = queryDB.getResultList();
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        return list;
    }

    @Override
    public List<CompanyDTO> findByPage(int numPage, int nbLine) throws DAOException {
        TypedQuery<Company> queryDB = sessionFactory.getCurrentSession().createQuery(QUERY_FIND_ALL, Company.class);
        List<Company> list = null;
        try {
            list = queryDB.setFirstResult(numPage).setMaxResults(nbLine).getResultList();
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        return new CompanyMapper().toDTO(list);
    }

    @Override
    public int delete(String id) {
        int res = 1;

        try {
            res = sessionFactory.getCurrentSession().createQuery(QUERY_DELETE)
                    .setParameter(0, id)
                    .executeUpdate();
        } catch (RuntimeException e) {
            LOG.error("[DELETE] error on deleting company.");
            res = 0;
        }

        return res;
    }
}
