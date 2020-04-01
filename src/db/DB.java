package db;

import java.sql.*;

public class DB {

    private static Connection conn = null;

    public static Connection getConnection() {

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursejdbc", "Dev", "nunes123");

            return conn;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }

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

    public static void closeResultSet(ResultSet rs){
        if (rs != null){
            try{
                rs.close();
            }catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
    }
}

