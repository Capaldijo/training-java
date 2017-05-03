package fr.ebiz.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;

public final class ConnectionDB {

    private static ConnectionDB instance = new ConnectionDB();

    static final Logger LOG = LoggerFactory.getLogger(ConnectionDB.class);

    private Connection connexion = null;

    private HikariDataSource ds = null;

    private static final int POOL_SIZE = 100;

    private static final int CONNECTION_TIMEOUT = 10000;

    /**
     * Contructor for connection to mysql db.
     * @throws ConnectionException if error on co to db
     */
    private ConnectionDB() {
        super();


        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("com.mysql.jdbc.Driver");
        cfg.setJdbcUrl("jdbc:mysql://localhost:3306/computer-database-db?useSSL=false&zeroDateTimeBehavior=convertToNull");
        cfg.setMaximumPoolSize(POOL_SIZE);
        cfg.setAutoCommit(false);
        cfg.setUsername("admincdb");
        cfg.setPassword("qwerty1234");
        cfg.setConnectionTimeout(CONNECTION_TIMEOUT);
        ds = new HikariDataSource(cfg);
    }

    /**
     * Get instance of co to db.
     * @return connectionMysql instance
     * @throws ConnectionException if error on co to db
     */
    public static ConnectionDB getInstance() throws ConnectionException {
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
    public Connection getConnection() throws ConnectionException {
        try {
            connexion = ds.getConnection();
        } catch (SQLException e) {
            LOG.error("[CONNECTION] Error connection to DB.");
            throw new ConnectionException("[CONNECTION] Error connection to DB.");
        }
        return connexion;
    }

    /**
     * @throws ConnectionException if error to co to db
     */
    public void closeAll() throws ConnectionException {
        try {
            connexion.close();
            ds.close();
        } catch (SQLException e) {
            LOG.error("[CLOSING] Error closing DB.");
            throw new ConnectionException("[CLOSING] Error closing DB");
        }
    }
}
