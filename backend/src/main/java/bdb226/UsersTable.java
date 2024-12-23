package bdb226;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides access to database operations involving the Users table
 */
public class UsersTable {
    private Connection mConnection;

    /**
     * Holds data about a user
     * 
     * @param mId       The ID of the user
     * @param mUserName The name of the user
     * @param mEmail    The email of the user
     */
    public static record UserData(int mId, String mUserName, String mEmail) {
    }

    /**
     * Create a UsersTable object which allows operations on the Users table through
     * a database connection
     * 
     * @param mConnection The connection to the database
     */
    public UsersTable(Connection mConnection) {
        this.mConnection = mConnection;
    }

    ////////////////////////// SELECT ONE //////////////////////////
    /** PS to select one user by their ID */
    private PreparedStatement mSelectOne;
    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE = "SELECT *" +
            " FROM \"Users\"" +
            " WHERE id=? ;";

    /**
     * safely performs mSelectOne = mConnection.prepareStatement(
     * "SELECT *
     * FROM "Users"
     * WHERE id=? ;"
     * );
     * 
     * @return Whether or not the prepared statement was created successfully
     */
    private boolean init_mSelectOne() {
        // return true on success, false otherwise
        try {
            mSelectOne = mConnection.prepareStatement(SQL_SELECT_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific user, by ID
     * 
     * @param id The id of the user/row being requested
     * @return The data for the requested user, or null if the ID was invalid
     */
    UserData selectOne(int row_id) {
        if (mSelectOne == null) // not yet initialized, do lazy init
            init_mSelectOne(); // lazy init
        UserData data = null;
        try {
            System.out.println("Database operation: UsersTable.selectOne(int row_id)");
            mSelectOne.setInt(1, row_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("user_name");
                String email = rs.getString("email");

                data = new UserData(id, userName, email);
            }
            rs.close(); // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    ////////////////////////// SELECT ONE //////////////////////////
    /** PS to select one user by their email */
    private PreparedStatement mSelectOneWithEmail;
    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE_WITH_EMAIL = "SELECT *" +
            " FROM \"Users\"" +
            " WHERE email=? ;";

    /**
     * safely performs mSelectOne = mConnection.prepareStatement(
     * "SELECT *
     * FROM "Users"
     * WHERE email=? ;"
     * );
     * 
     * @return Whether or not the prepared statement was created successfully
     */
    private boolean init_mSelectOneWithEmail() {
        // return true on success, false otherwise
        try {
            mSelectOneWithEmail = mConnection.prepareStatement(SQL_SELECT_ONE_WITH_EMAIL);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOneWithEmail");
            System.err.println("Using SQL: " + SQL_SELECT_ONE_WITH_EMAIL);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific user, by email
     * 
     * @param email The email of the user/row being requested
     * @return The data for the requested user, or null if the email was invalid
     */
    UserData selectOne(String email) {
        if (mSelectOneWithEmail == null) // not yet initialized, do lazy init
            init_mSelectOneWithEmail(); // lazy init
        UserData data = null;
        try {
            System.out.println("Database operation: UsersTable.selectOne(String email)");
            mSelectOneWithEmail.setString(1, email);
            ResultSet rs = mSelectOneWithEmail.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("user_name");

                data = new UserData(id, userName, email);
            }
            rs.close(); // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    ////////////////////////// UPDATE ONE //////////////////////////
    /**
     * PS to replace the username, email, gender identity, sexual orientation, and
     * personal note of a user
     */
    private PreparedStatement mUpdateOne;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_ONE = "UPDATE \"Users\"" +
            " SET user_name = ?, email = ? " +
            " WHERE id = ?";

    /**
     * safely performs mUpdateOne = mConnection.prepareStatement(
     * "UPDATE "Users"
     * SET user_name = ?, email = ?
     * WHERE id = ? ;"
     * );
     * 
     * @return Whether or not the prepared statement was created successfully
     */
    private boolean init_mUpdateOne() {
        // return true on success, false otherwise
        try {
            mUpdateOne = mConnection.prepareStatement(SQL_UPDATE_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            System.err.println("Using SQL: " + SQL_UPDATE_ONE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Update the information about a user
     * 
     * @param id       The ID of the user to update
     * @param userName The new name of the user
     * @param email    The new email of the user
     */
    int updateOne(int id, String userName, String email) {
        if (mUpdateOne == null) // not yet initialized, do lazy init
            init_mUpdateOne(); // lazy init
        int res = -1;
        try {
            System.out.println(
                    "Database operation: UsersTable.updateOne(int id, String userName, String email)");
            mUpdateOne.setString(1, userName);
            mUpdateOne.setString(2, email);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ////////////////////////// INSERT //////////////////////////
    /**
     * ps to insert into tbldata a new row with next auto-gen id and the two given
     * values
     */
    private PreparedStatement mInsertOne;
    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE = "INSERT INTO \"Users\" (id, user_name, email)"
            +
            " VALUES (default, ?, ?) "
            + ";";

    /**
     * safely performs mInsertOne = mConnection.prepareStatement(
     * "INSERT INTO "Users" (id, user_name, email)
     * VALUES (default, ?, ?) ;"
     * );
     * 
     * @return Whether or not the prepared statement was created successfully
     */
    private boolean init_mInsertOne() {
        // return true on success, false otherwise
        try {
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE, PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Insert a user into the database
     * 
     * @param userName The name of the new user
     * @param email    The email of the new user
     * @return The ID of the new user
     */
    int insertRow(String userName, String email) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int count = 0;
        try {
            System.out.println(
                    "Database operation: UsersTable.insertRow(String userName, String email)");
            mInsertOne.setString(1, userName);
            mInsertOne.setString(2, email);
            count += mInsertOne.executeUpdate();
            ResultSet newKeys = mInsertOne.getGeneratedKeys();

            if (count > 0) {
                System.out.println("Inserted " + count + " rows");
                int newId = -1;
                if (newKeys.next()) {
                    newId = newKeys.getInt(1);
                    System.out.println("Inserted row ID: " + newId);
                }
                return newId;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
