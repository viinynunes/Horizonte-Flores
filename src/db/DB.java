package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection conn = null;

    public static Connection getConnection() {

        if (conn == null){
            try {

                Properties props = loadProps();
                String url = props.getProperty("dburl");

                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection(url, props);
                return conn;
            } catch (SQLException | ClassNotFoundException e) {
                throw new DBException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    private static Properties loadProps() {
        try (FileInputStream fs = new FileInputStream("db.properties")){

            Properties props = new Properties();
            props.load(fs);

            return props;
        } catch (IOException e){
            throw new DBException(e.getMessage());
        }

    }
}

