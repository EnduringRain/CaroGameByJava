/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vu
 */
public class DBHelper {

    private static DBHelper instance = null;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLGAME;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "123";

    public static synchronized DBHelper getInstance() throws ClassNotFoundException {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    public DBHelper() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public void closeConnection(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }
}
