package bdb226;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Database has all our logic for connecting to and interacting with a database.
 */
public class Database {

    /** Connection to db. An open connection if non-null, null otherwise */
    private Connection mConnection;

    /** Cache service for handling file caching */
    private final FileCacheService fileCacheService;

    /** Object to operate on Users table */
    public UsersTable users;

    /**
     * The Database constructor is private: we only create Database objects
     * through one or more static `getDatabase()` methods.
     */
    private Database(FileCacheService fileCacheService) {
        this.fileCacheService = fileCacheService;
    }

    private Database(String dbUri, FileCacheService fileCacheService) throws SQLException {
        this(fileCacheService);
        System.out.println("Attempting to use provided DATABASE_URI env var.");
        try {
            this.mConnection = DriverManager.getConnection(dbUri);
            initializeTables();
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database using dbUri", e);
        }
    }

    private Database(String dbUrl, String user, String pass, FileCacheService fileCacheService) throws SQLException {
        this(fileCacheService);
        System.out.println("Attempting to use provided POSTGRES_{IP, PORT, USER, PASS, DBNAME} env var.");
        try {
            this.mConnection = DriverManager.getConnection(dbUrl, user, pass);
            initializeTables();
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database using dbUrl, user, and pass", e);
        }
    }

    /**
     * Initializes table objects after establishing a connection.
     */
    private void initializeTables() {
        this.users = new UsersTable(mConnection);
    }

    /**
     * Uses dbUri to create a connection to a database, and stores it in the
     * returned Database object.
     * 
     * @param dbUri            the connection string for the database
     * @param fileCacheService the file cache service for caching files
     * @return null upon connection failure, otherwise a valid Database with open
     *         connection
     */
    public static Database getDatabase(String dbUri, FileCacheService fileCacheService) {
        if (dbUri != null && dbUri.length() > 0) {
            try {
                Database database = new Database(dbUri, fileCacheService);

                if (database.mConnection == null) {
                    System.err.println("Error: DriverManager.getConnection() returned a null object");
                    return null;
                } else {
                    System.out.println(" ... successfully connected");
                    return database;
                }
            } catch (SQLException e) {
                System.err.println("Error: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Uses params to manually construct string for connecting to a database,
     * and stores it in the returned Database object.
     * 
     * @param ip               database host IP or hostname
     * @param port             database port
     * @param dbname           database name
     * @param user             database username
     * @param pass             database password
     * @param fileCacheService the file cache service for caching files
     * @return null upon connection failure, otherwise a valid Database with open
     *         connection
     */
    public static Database getDatabase(String ip, String port, String dbname, String user, String pass,
            FileCacheService fileCacheService) {
        if (ip != null && port != null && dbname != null && user != null && pass != null) {
            String dbUrl = "jdbc:postgresql://" + ip + ":" + port + "/" + dbname;
            System.out.println("Connecting to " + ip + ":" + port);
            try {
                Database database = new Database(dbUrl, user, pass, fileCacheService);

                if (database.mConnection == null) {
                    System.out.println("\n\tError: DriverManager.getConnection() returned a null object");
                    return null;
                } else {
                    System.out.println(" ... successfully connected");
                    return database;
                }
            } catch (SQLException e) {
                System.out.println("\n\tError: DriverManager.getConnection() threw a SQLException");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Uses the presence of environment variables to invoke the correct
     * overloaded `getDatabase` method.
     * 
     * @param fileCacheService the file cache service for caching files
     * @return a valid Database object with active connection on success, null
     *         otherwise
     */
    public static Database getDatabase(FileCacheService fileCacheService) {
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        String dbname = env.get("POSTGRES_DBNAME");
        String dbUri = env.get("DATABASE_URI");

        if (dbUri != null && dbUri.length() > 0) {
            return Database.getDatabase(dbUri, fileCacheService);
        } else if (ip != null && port != null && dbname != null && user != null && pass != null) {
            return Database.getDatabase(ip, port, dbname, user, pass, fileCacheService);
        }
        System.err.println("Insufficient information to connect to database.");
        return null;
    }

    ////////////////////////// CREATE TABLE //////////////////////////
    /** precompiled SQL statement for creating the table in our database */
    private PreparedStatement mCreateTable;
    /** the SQL for creating the table in our database */
    private static final String SQL_CREATE_TABLE = "CREATE TABLE tblData (" +
            " id SERIAL PRIMARY KEY," +
            " likes INT NOT NULL," +
            " message VARCHAR(500) NOT NULL)";
    // NB: we can easily get ourselves in trouble here by typing the
    // SQL incorrectly. We really should have things like "tblData"
    // as constants, and then build the strings for the statements
    // from those constants.

    /**
     * safely performs mCreateTable =
     * mConnection.prepareStatement(SQL_CREATE_TABLE);
     */
    private boolean init_mCreateTable() {
        // return true on success, false otherwise
        try {
            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            System.err.println("Using SQL: " + SQL_CREATE_TABLE);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createTable() {
        if (mCreateTable == null) // not yet initialized, do lazy init
            init_mCreateTable(); // lazy init
        try {
            System.out.println("Database operation: createTable()");
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////// DROP TABLE //////////////////////////
    /** ps to delete table tbldata from the database */
    private PreparedStatement mDropTable;
    /** the SQL for mDropTable */
    private static final String SQL_DROP_TABLE_TBLDATA = "DROP TABLE tblData";

    /**
     * safely performs mDropTable = mConnection.prepareStatement("DROP TABLE
     * tblData");
     */
    private boolean init_mDropTable() {
        // return true on success, false otherwise
        try {
            mDropTable = mConnection.prepareStatement(SQL_DROP_TABLE_TBLDATA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE_TBLDATA);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        if (mDropTable == null) // not yet initialized, do lazy init
            init_mDropTable(); // lazy init
        try {
            System.out.println("Database operation: dropTable()");
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////// CONNECT & DISCONNECT //////////////////////////

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return true if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection != null) {
            // for this simple example, we disconnect from the db when done
            System.out.print("Disconnecting from database.");
            try {
                mConnection.close();
            } catch (SQLException e) {
                System.err.println("\n\tError: close() threw a SQLException");
                e.printStackTrace();
                mConnection = null; // set it to null rather than leave broken
                return false;
            }
            System.out.println(" ...  connection successfully closed");
            mConnection = null; // connection is gone, so null this out
            return true;
        }
        // else connection was already null
        System.err.print("Unable to close connection: Connection was null");
        return false;
    }
}