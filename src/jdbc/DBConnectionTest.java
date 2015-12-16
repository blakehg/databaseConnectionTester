package jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnectionTest {

    private static String newLine = System.getProperty("line.separator");
    private static FileInputStream fis = null;
    private static Properties props = new Properties();

    public static void main(String[] args) throws IOException {

        fis = new FileInputStream("db.properties");
        props.load(fis);

        // load the test query from properties
        String QUERY = props.getProperty("TEST_SQL_QUERY");

    if (testConnection()) {
        // Run Test SQL Query to connected Database
        System.out.println(newLine + " Executing Test SQL Query for Response... " + newLine + newLine);
            try (Connection con = getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(QUERY)) {

                while (rs.next()) {

                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    while (rs.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = rs.getString(i);
                            System.out.print(columnValue + " " + rsmd.getColumnName(i));
                        }
                        System.out.println("");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to connect to database
    private static Connection getConnection() {
        Connection con = null;

        try {
            fis = new FileInputStream("db.properties");
            props.load(fis);

            // load Cache Driver Class
            Class.forName(props.getProperty("DB_DRIVER_CLASS"));

            // create connection
                    con = DriverManager.getConnection(props.getProperty("DB_URL"),
                    props.getProperty("DB_USERNAME"),
                    props.getProperty("DB_PASSWORD"));
        } catch (IOException | ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
        return con;
    }

    // Method to test connection to database
    private static boolean testConnection() {

        try {
            System.out.println( newLine + " Testing Connection to Database...");
            // print connection details
            System.out.println(newLine + " DB URL = " + props.getProperty("DB_URL") + newLine + " Username = " + props.getProperty("DB_USERNAME") + newLine + " Password = " + props.getProperty("DB_PASSWORD") + newLine);
            Connection con = getConnection();
            con.close();
            boolean status = con.isClosed();

            if (status) {
                System.out.println(" Connection to Database was Successful! ");
                return true;
            }

        } catch (Exception err) {
            System.out.println(newLine + " Failed to connect to Database...");
        }
        return false;
    }


}