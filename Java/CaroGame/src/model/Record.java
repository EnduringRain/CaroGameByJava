package model;

import java.sql.Date;

public class Record {

    private String id;
    private int time;
    private Date createdDate;
    private String accountId;

    // Cập nhật constructor
    public Record(String id, int time, Date createdDate, String accountId) {
        this.id = id;
        this.time = time;
        this.createdDate = createdDate;
        this.accountId = accountId;
    }

    public java.sql.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.sql.Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
