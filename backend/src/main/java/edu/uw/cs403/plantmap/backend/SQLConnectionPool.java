package edu.uw.cs403.plantmap.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SQLConnectionPool {
    private String hostName;
    private String dbName;
    private String userName;
    private String password;
    private int maxPoolSize;
    private int connNum = 0;

    private static final String SQL_VERIFYCONN = "select 1";

    Stack<Connection> freePool = new Stack<>();
    Set<Connection> occupiedPool = new HashSet<>();

    public SQLConnectionPool(String hostName, String dbName, String userName, String password) {
        this(hostName, dbName, userName, password, 10);
    }

    /**
     * Constructor
     *
     * @param hostName
     *            The connection hostname
     * @param dbName
     *            The connection database name
     * @param userName
     *            user name
     * @param password
     *            password
     * @param maxSize
     *            max size of the connection pool
     */
    public SQLConnectionPool(String hostName, String dbName, String userName, String password, int maxSize) {
        this.hostName = hostName;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
        this.maxPoolSize = maxSize;
    }

    /**
     * Get an available connection
     *
     * @return An available connection
     * @throws SQLException
     *             Fail to get an available connection
     */
    public synchronized Connection getConnection() throws SQLException {
        Connection conn = null;

        if (isFull()) {
            throw new SQLException("The connection pool is full.");
        }

        conn = getConnectionFromPool();

        // If there is no free connection, create a new one.
        if (conn == null) {
            conn = createNewConnectionForPool();
        }

        // For Azure Database for MySQL, if there is no action on one connection for some
        // time, the connection is lost. By this, make sure the connection is
        // active. Otherwise reconnect it.
        makeAvailable(conn);
        return conn;
    }

    /**
     * Return a connection to the pool
     *
     * @param conn
     *            The connection
     * @throws SQLException
     *             When the connection is returned already or it isn't gotten
     *             from the pool.
     */
    public synchronized void returnConnection(Connection conn)
            throws SQLException {
        if (conn == null) {
            throw new NullPointerException();
        }
        if (!occupiedPool.remove(conn)) {
            throw new SQLException(
                    "The connection is returned already or it isn't for this pool");
        }
        freePool.push(conn);
    }

    public synchronized void returnConnectionSafe(Connection conn) {
        try {
            returnConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();

            reset();
        }
    }

    public synchronized void reset()
    {
        // Empty the pool so new connections are made
        freePool.clear();
        occupiedPool.clear();
        connNum = 0;
    }
    /**
     * Verify if the connection is full.
     *
     * @return if the connection is full
     */
    private synchronized boolean isFull() {
        return ((freePool.size() == 0) && (connNum >= maxPoolSize));
    }

    /**
     * Create a connection for the pool
     *
     * @return the new created connection
     * @throws SQLException
     *             When fail to create a new connection.
     */
    private Connection createNewConnectionForPool() throws SQLException {
        Connection conn = createNewConnection();
        connNum++;
        occupiedPool.add(conn);
        return conn;
    }

    /**
     * Crate a new connection
     *
     * @return the new created connection
     * @throws SQLException
     *             When fail to create a new connection.
     */
    private Connection createNewConnection() throws SQLException {
        Connection conn = null;
        try {
            String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                    + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, userName,
                    password);

            conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new SQLException("Could not create new connection");
        }
        return conn;
    }

    /**
     * Get a connection from the pool. If there is no free connection, return
     * null
     *
     * @return the connection.
     */
    private Connection getConnectionFromPool() {
        Connection conn = null;
        if (freePool.size() > 0) {
            conn = freePool.pop();
            occupiedPool.add(conn);
        }
        return conn;
    }

    /**
     * Make sure the connection is available now. Otherwise, reconnect it.
     *
     * @param conn
     *            The connection for verification.
     * @throws SQLException
     *             Fail to get an available connection
     */
    private void makeAvailable(Connection conn) throws SQLException {
        if (isConnectionAvailable(conn)) {
            return;
        }

        // If the connection is't available, reconnect it.
        occupiedPool.remove(conn);
        connNum--;
        conn.close();

        conn = createNewConnection();
        occupiedPool.add(conn);
        connNum++;
    }

    /**
     * By running a sql to verify if the connection is available
     *
     * @param conn
     *            The connection for verification
     * @return if the connection is available for now.
     */
    private boolean isConnectionAvailable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.executeQuery(SQL_VERIFYCONN);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}