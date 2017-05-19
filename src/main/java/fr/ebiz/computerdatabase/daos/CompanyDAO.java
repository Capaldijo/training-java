package fr.ebiz.computerdatabase.daos;

import java.sql.SQLException;
import java.util.List;

import fr.ebiz.computerdatabase.interfaces.ICompanyDAO;
import fr.ebiz.computerdatabase.mappers.CompanyMapper;
import fr.ebiz.computerdatabase.mappers.CompanyRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.models.Company;
import fr.ebiz.computerdatabase.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAO implements ICompanyDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private static final String QUERY_FIND = "SELECT * FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private static final String QUERY_FINDALL = "SELECT * FROM " + Utils.COMPANY_TABLE;

    private static final String QUERY_FINDBYPAGE = "SELECT * FROM " + Utils.COMPANY_TABLE + " LIMIT ?, ?";

    private static final String QUERY_DELETE = "DELETE FROM " + Utils.COMPANY_TABLE + " WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor companyDAO.
     * @param jdbcTemplate .
     * @throws ConnectionException Error on co to db.
     */
    @Autowired
    public CompanyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Company find(int id) throws DAOException {
        Company company = null;

        try {
            company = jdbcTemplate.queryForObject(QUERY_FIND, new Object[] {id}, new CompanyRowMapper());
        } catch (DataAccessException e) {
            LOG.error("[FIND] Error queryForObject.");
            throw new DAOException("[FIND] Error queryForObject.");
        }

        return company;
    }

    @Override
    public List<Company> findAll() throws DAOException {
        return jdbcTemplate.query(QUERY_FINDALL, new CompanyRowMapper());
    }

    @Override
    public List<CompanyDTO> findByPage(int numPage, int nbLine) throws DAOException {
        List<Company> list = jdbcTemplate.query(QUERY_FINDBYPAGE, new CompanyRowMapper());
        return new CompanyMapper().toDTO(list);
    }

    @Override
    public int delete(String id) throws SQLException, DAOException {
        int res = 1;

        try {
            jdbcTemplate.update(QUERY_DELETE, id);
        } catch (DataAccessException e) {
            LOG.error("[DELETE] error on deleting company.");
            res = 0;
        }

        return res;
    }
}
