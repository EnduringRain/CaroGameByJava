package service;

import dao.AccountDAO;
import dao.RecordDAO;
import model.Record;
import java.sql.SQLException;
import java.util.List;
import model.Account;

public class RecordService {

    private final RecordDAO recordDAO;
    private final AccountDAO accountDAO;

    public RecordService() throws ClassNotFoundException {
        this.recordDAO = new RecordDAO();
        this.accountDAO = new AccountDAO();
    }

    public int addRecord(int time, String accountName) throws SQLException {
        List<Record> allRecords = recordDAO.getAllRecords();
        Account account = accountDAO.getAccountByUsername(accountName);

        int recordCount = allRecords.size() + 1;
        String id = String.format("REC%03d", recordCount);

        java.sql.Date createdDate = new java.sql.Date(System.currentTimeMillis());

        Record record = new Record(id, time, createdDate, account.getMaTK());

        return recordDAO.insertRecord(record);
    }

    public List<Record> get5Record() throws SQLException {
        return recordDAO.getTop5Records();
    }

    public List<Record> getAllRecord() throws SQLException {
        return recordDAO.getAllRecords();
    }

    public List<Record> getRecordByUser(String accountId) throws SQLException {
        return recordDAO.getRecordsByUser(accountId);
    }
}
