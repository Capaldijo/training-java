package fr.ebiz.computerdatabase.persistence;

import java.sql.Connection;
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

    private static ConnectionDB instance;

    static final Logger LOG = LoggerFactory.getLogger(ConnectionDB.class);

    private HikariDataSource hikariDS = null;

    private static final String JDBC_URL_CONF = "jdbcUrl";

    private static final String HIKARICP_CONF_FILE = "/hikari.properties";

    private static final ThreadLocal<Connection> CONNECTION = new ThreadLocal<>();

    /**
     * Contructor for connection to mysql db.
     * @throws ConnectionException if error on co to db
     */
    private ConnectionDB() {
        super();

        try {

            HikariConfig cfg = new HikariConfig(HIKARICP_CONF_FILE);

            cfg.setJdbcUrl(InitialContext.doLookup("java:comp/env/" + JDBC_URL_CONF));

            hikariDS = new HikariDataSource(cfg);
        } catch (NamingException e) {
            LOG.error("[CONNECTION] no configuration file found.");
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
     * @return Connection.
     */
    public Connection getConnection() {
        Connection connection = CONNECTION.get();
        if (connection == null) {
            try {
                connection = hikariDS.getConnection();
                CONNECTION.set(connection);
            } catch (SQLException e) {
                LOG.error("Error Unable to Connect to Database");
                throw new IllegalStateException(e);
            }
        }
        return connection;
    }

    /**
     *
     */
    public void startTransaction() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOG.error("Error while starting transaction");
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @return true if is in transaction
     */
    public boolean isTransactional() {
        try {
            return !getConnection().getAutoCommit();
        } catch (SQLException e) {
            LOG.error("Error while getting transaction state");
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     */
    public void rollback() {
        Connection connection = CONNECTION.get();
        if (connection == null) {
            LOG.error("Cannot rollback non-existent transaction");
            throw new IllegalStateException("Cannot rollback non-existent transaction");
        }
        try {
            connection.rollback();
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            LOG.error("Error while rolling back transaction");
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     */
    public void commit() {
        Connection connection = CONNECTION.get();
        if (connection == null) {
            LOG.error("Cannot commit non-existent transaction");
            throw new IllegalStateException("Cannot commit non-existent transaction");
        }
        try {
            connection.commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            LOG.error("Error while committing transaction");
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     */
    public void closeConnection() {
        Connection connection = CONNECTION.get();
        if (connection == null) {
            LOG.error("Cannot close non-existent transaction");
            throw new IllegalStateException("Cannot close non-existent transaction");
        }
        try {
            connection.close();
        } catch (SQLException e) {
            LOG.error("Error while closing connection");
            throw new IllegalStateException(e);
        } finally {
            CONNECTION.remove();
        }
    }

    /**
     *
     * @param rs resultSet to close.
     * @throws DAOException exception to throw
     */
    public static void closeObjects(ResultSet rs) throws DAOException {
        closeObjects(null, rs, null);
    }

    /**
     *
     * @param st statement to close.
     * @param rs resultSet to close.
     * @param co Connection to close.
     * @throws DAOException exception to throw
     */
    public static void closeObjects(Statement st, ResultSet rs, Connection co) throws DAOException {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        if (co != null) {
            try {
                co.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
    }

    /**
     * @throws ConnectionException if error to co to db
     */
    public void closeHikari() throws ConnectionException {
        hikariDS.close();
    }
}