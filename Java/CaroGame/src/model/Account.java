/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Vu
 */
public class Account {
    private String maTK;
    private String tk;
    private String mk;

    public Account() {
    }

    public Account(String maTK, String tk, String mk) {
        this.maTK = maTK;
        this.tk = tk;
        this.mk = mk;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }

    public String getMaTK() {
        return maTK;
    }

    public String getTk() {
        return tk;
    }

    public String getMk() {
        return mk;
    }
    
    

    @Override
    public String toString() {
        return "TaiKhoan{" + "maTK=" + maTK + ", tk=" + tk + ", mk=" + mk + '}';
    }
}
