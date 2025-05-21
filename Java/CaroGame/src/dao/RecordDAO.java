package dao;

import model.Record;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO {

    private final DBHelper db;

    public RecordDAO() throws ClassNotFoundException {
        this.db = DBHelper.getInstance();
    }

    public List<Record> getTop5Records() throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        List<Record> lst = new ArrayList<>();

        con = db.getConnection();
        call = con.prepareCall("{call GET_TOP_5_RECORDS}");
        rs = call.executeQuery();

        while (rs.next()) {
            lst.add(new Record(
                    rs.getString("ID"),
                    rs.getInt("TIME"),
                    rs.getDate("CREATED_DATE"),
                    rs.getString("ACCOUNT_ID")
            ));
        }
        closeResources(con, rs, call);
        return lst;
    }

    public List<Record> getAllRecords() throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        List<Record> lst = new ArrayList<>();

        con = db.getConnection();
        call = con.prepareCall("{call GET_ALL_RECORDS}");
        rs = call.executeQuery();

        while (rs.next()) {
            lst.add(new Record(
                    rs.getString("ID"),
                    rs.getInt("TIME"),
                    rs.getDate("CREATED_DATE"),
                    rs.getString("ACCOUNT_ID")
            ));
        }
        closeResources(con, rs, call);
        return lst;
    }

    public int insertRecord(Record record) throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        int result = 0;

        try {
            con = db.getConnection();
            call = con.prepareCall("{call INSERT_RECORD(?, ?, ?, ?)}");
            call.setString(1, record.getId());
            call.setInt(2, record.getTime());
            call.setDate(3, record.getCreatedDate());
            call.setString(4, record.getAccountId());

            result = call.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm kỷ lục: " + e.getMessage());
            throw e;
        } finally {
            closeResources(con, null, call);
        }

        return result;
    }

    public List<Record> getRecordsByUser(String accountId) throws SQLException {
        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        List<Record> lst = new ArrayList<>();

        con = db.getConnection();
        call = con.prepareCall("{call GET_RECORDS_BY_USER (?)}");
        call.setString(1, accountId);
        rs = call.executeQuery();

        while (rs.next()) {
            lst.add(new Record(
                    rs.getString("ID"),
                    rs.getInt("TIME"),
                    rs.getDate("CREATED_DATE"),
                    rs.getString("ACCOUNT_ID")
            ));
        }
        closeResources(con, rs, call);
        return lst;
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
