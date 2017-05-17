package fr.ebiz.computerdatabase.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;

public final class ConnectionDB {

    private static ConnectionDB instance = new ConnectionDB();

    static final Logger LOG = LoggerFactory.getLogger(ConnectionDB.class);

    private HikariDataSource hikariDS = null;

    private static final String JDBCURL_CONF = "jdbcUrl";

    /**
     * Contructor for connection to mysql db.
     * @throws ConnectionException if error on co to db
     */
    private ConnectionDB() {
        super();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            HikariConfig cfg = new HikariConfig("/hikari.properties");

            cfg.setJdbcUrl(InitialContext.doLookup("java:comp/env/" + JDBCURL_CONF));

            hikariDS = new HikariDataSource(cfg);
        } catch (NamingException e) {
            LOG.error("[CONNECTION] no configuration file found.");
        } catch (ClassNotFoundException e) {
            LOG.error("[CONNECTION] error on loading driver jdbc.");
            throw new RuntimeException("[CONNECTION] error on loading driver jdbc.");
        }
    }

    /**
     * Get instance of co to db.
     * @return connectionMysql instance
     */
    public static ConnectionDB getInstance() {
        if (ConnectionDB.instance == null) {
            ConnectionDB.instance = new ConnectionDB();
        }
        return ConnectionDB.instance;
    }

    /**
     * Get connection.
     * @return Connection with hikari
     * @throws ConnectionException Error on co to db
     */
    public HikariDataSource getHikariDS() {
        return hikariDS;
    }

    /**
     *
     * @param st statement to close
     * @throws DAOException exception to throw
     */
    public void closeObjects(Statement st) throws DAOException {
        closeObjects(st, null);
    }

    /**
     *
     * @param st statement to close
     * @param rs resultSet to close
     * @throws DAOException exception to throw
     */
    public void closeObjects(Statement st, ResultSet rs) throws DAOException {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
    }

    /**
     * @throws ConnectionException if error to co to db
     */
    public void closeAll() throws ConnectionException {
        hikariDS.close();
    }
}
