package fr.ebiz.computerdatabase.dao;

import java.sql.SQLException;
import java.util.List;

import fr.ebiz.computerdatabase.mapper.CompanyMapper;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.exception.ConnectionException;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.model.Company;
import fr.ebiz.computerdatabase.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAO implements ICompanyDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private static final String QUERY_FIND_ALL = "SELECT * FROM " + Utils.COMPANY_TABLE;

    private static final String QUERY_FIND_BY_PAGE = "SELECT * FROM " + Utils.COMPANY_TABLE + " LIMIT ?, ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private SessionFactory sessionFactory;

    /**
     * Constructor companyDAO.
     * @param sessionFactory .
     * @throws ConnectionException Error on co to db.
     */
    @Autowired
    public CompanyDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Company find(Long id) throws DAOException {
        return sessionFactory.getCurrentSession().get(Company.class, id);
    }

    @Override
    public List<Company> findAll() throws DAOException {
        return sessionFactory.getCurrentSession().createQuery("from Company").list();
    }

    @Override
    public List<CompanyDTO> findByPage(int numPage, int nbLine) throws DAOException {
        List<Company> list = sessionFactory.getCurrentSession().createQuery("from Company").list();
        return new CompanyMapper().toDTO(list);
    }

    @Override
    public int delete(String id) throws SQLException, DAOException {
        int res = 1;

        try {
            //jdbcTemplate.update(QUERY_DELETE, id);
            res = sessionFactory.getCurrentSession().createQuery("delete from Company where id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
        } catch (DataAccessException e) {
            LOG.error("[DELETE] error on deleting company.");
            res = 0;
        }

        return res;
    }
}
