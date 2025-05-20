/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DBHelper;
import dao.AccountDAO;
import model.Account;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Vu
 */
public class AccountService {

    private final AccountDAO tko = new AccountDAO();
    private final DBHelper db;

    public AccountService() throws ClassNotFoundException {
        this.db = DBHelper.getInstance();
    }

    public int taoTaiKhoan(String tk, String mk) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String matk = "TK" + LocalDateTime.now().format(formatter);

        Account tk1 = new Account(matk, tk, mk);
        return tko.insertAccount(tk1);
    }

    public boolean kiemTraTaiKhoan(String tk, String mk) throws SQLException {
        Account ac = tko.getAccount(tk, mk);
        return ac != null;
    }

}
