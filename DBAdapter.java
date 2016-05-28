import java.sql.*;

public class DBAdapter {
    /**
     * Hosts the database connection.
     */
    private Connection myConnection;

    private Statement myStatement;

    /**
     * Establishes a connection to the database.
     *
     * @parameter url is the path to access the database.
     * @parameter username is the username of a database user.
     * @parameter password is the password associated with the user.
     * @return a boolean representing the success of establishing a connection.
     */
    public  DBAdapter(String url, String username, String password) {
        try {
            myConnection = DriverManager.getConnection(url, username, password);
            myStatement = myConnection.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ResultSet DML_ResultSet(String theQuery) {
        try {
            return myStatement.executeQuery(theQuery);
        } catch(SQLException e) {}
        return null;
    }

    public void DML_Statement(String theQuery) {
        try {
            myConnection.prepareStatement(theQuery).executeUpdate();
        } catch(SQLException e) {}
    }
}
