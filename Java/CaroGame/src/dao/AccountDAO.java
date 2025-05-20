/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.DBHelper;
import model.Account;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Vu
 */
public class AccountDAO {

    private final DBHelper db;

    public AccountDAO() throws ClassNotFoundException {
        this.db = DBHelper.getInstance();
    }

    public List<Account> getALLAccount() throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        List<Account> lst = new ArrayList<>();

        con = db.getConnection();
        call = con.prepareCall("{call GET_ALL_ACCOUNTS}");
        rs = call.executeQuery();

        while (rs.next()) {
            lst.add(new Account(rs.getString(1), rs.getString(2), rs.getString(3)));
        }
        closeResources(con, rs, call);
        return lst;
    }

    public Account getAccount(String tk, String mk) throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        Account ac = null;

        con = db.getConnection();
        call = con.prepareCall("{call GET_ACCOUNT (?, ?)}");
        call.setString(1, tk);
        call.setString(2, mk);

        rs = call.executeQuery();

        if (rs.next()) {
            ac = new Account(rs.getString(1), rs.getString(2), rs.getString(3));
        }

        closeResources(con, rs, call);
        return ac;
    }

    public Account getAccountByUsername(String tk) throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        Account ac = null;

        con = db.getConnection();
        call = con.prepareCall("{call GET_ACCOUNTBYUSER (?)}");
        call.setString(1, tk);

        rs = call.executeQuery();

        if (rs.next()) {
            ac = new Account(rs.getString(1), rs.getString(2), rs.getString(3));
        }

        closeResources(con, rs, call);
        return ac;
    }

    public int insertAccount(Account tk) throws SQLException {
        Connection con = null;
        CallableStatement call = null;

        con = db.getConnection();
        call = con.prepareCall("{call INSERT_ACCOUNT (?, ?, ?)}");
        call.setString(1, tk.getMaTK());
        call.setString(2, tk.getTk());
        call.setString(3, tk.getMk());

        int n = call.executeUpdate();

        closeResources(con, null, call);
        return n;
    }

    public int updateAcoount(Account tk) throws SQLException {
        Connection con = null;
        CallableStatement call = null;

        con = db.getConnection();
        call = con.prepareCall("{call UPDATE_ACCOUNT (?, ?, ?)}");
        call.setString(1, tk.getMaTK());
        call.setString(2, tk.getTk());
        call.setString(3, tk.getMk());

        int n = call.executeUpdate();

        closeResources(con, null, call);
        return n;
    }

    public int deleteAcoount(Account tk) throws SQLException {
        Connection con = null;
        CallableStatement call = null;

        con = db.getConnection();
        call = con.prepareCall("{call DELETE_ACCOUNT (?, ?, ?)}");
        call.setString(1, tk.getMaTK());
        call.setString(2, tk.getTk());
        call.setString(3, tk.getMk());

        int n = call.executeUpdate();

        closeResources(con, null, call);
        return n;
    }

    public void closeResources(Connection con, ResultSet rs, CallableStatement call) throws SQLException {
        if (call != null) {
            call.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (con != null) {
            db.closeConnection(con);
        }
    }
}
