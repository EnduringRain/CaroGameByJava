CREATE DATABASE QLGAME
GO

USE QLGAME
GO

-- TABLES

CREATE TABLE ACCOUNT (
    ID NVARCHAR(20) NOT NULL PRIMARY KEY,
    USERNAME NVARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(50)
)
GO

CREATE TABLE RECORD (
    ID NVARCHAR(20) NOT NULL PRIMARY KEY,
    TIME INT,
    CREATED_DATE DATE,
    ACCOUNT_ID NVARCHAR(20) NOT NULL
)
GO

ALTER TABLE RECORD
ADD FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID)
ON DELETE CASCADE
GO

-- Thêm dữ liệu mẫu vào bảng ACCOUNT
INSERT INTO ACCOUNT VALUES ('ACC001', 'user1', 'password123');
INSERT INTO ACCOUNT VALUES ('ACC002', 'user2', 'securepass456');
INSERT INTO ACCOUNT VALUES ('ACC003', 'player3', 'gameon789');
INSERT INTO ACCOUNT VALUES ('ACC004', 'caroplayer', 'caro2023');
INSERT INTO ACCOUNT VALUES ('ACC005', 'champion', 'win2023');

-- Thêm dữ liệu mẫu vào bảng RECORD
INSERT INTO RECORD VALUES ('REC001', 120, '2023-10-15', 'ACC001');
INSERT INTO RECORD VALUES ('REC002', 180, '2023-10-16', 'ACC002');
INSERT INTO RECORD VALUES ('REC003', 90, '2023-10-17', 'ACC003');
INSERT INTO RECORD VALUES ('REC004', 200, '2023-10-18', 'ACC004');
INSERT INTO RECORD VALUES ('REC005', 150, '2023-10-19', 'ACC005');
GO

-- STORED PROCEDURES

CREATE PROCEDURE GET_ALL_ACCOUNTS
AS
BEGIN
    SELECT * FROM ACCOUNT;
END
GO

CREATE PROCEDURE GET_ACCOUNT
    @USERNAME VARCHAR(50),
    @PASSWORD VARCHAR(50)
AS
BEGIN
    SELECT ID, USERNAME, PASSWORD 
    FROM ACCOUNT 
    WHERE USERNAME = @USERNAME AND PASSWORD = @PASSWORD;
END
GO

CREATE PROCEDURE INSERT_ACCOUNT
    @ID VARCHAR(50),
    @USERNAME VARCHAR(50),
    @PASSWORD VARCHAR(50)
AS
BEGIN
    INSERT INTO ACCOUNT VALUES (@ID, @USERNAME, @PASSWORD);
END
GO

CREATE PROCEDURE UPDATE_ACCOUNT
    @ID VARCHAR(50),
    @USERNAME VARCHAR(50),
    @PASSWORD VARCHAR(50)
AS
BEGIN
    UPDATE ACCOUNT 
    SET USERNAME = @USERNAME, PASSWORD = @PASSWORD 
    WHERE ID = @ID;
END
GO

CREATE PROCEDURE DELETE_ACCOUNT
    @ID VARCHAR(50)
AS
BEGIN
    DELETE FROM ACCOUNT WHERE ID = @ID;
END
GO

CREATE PROCEDURE GET_ACCOUNT_BY_USER
    @Username NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT *
    FROM ACCOUNT 
    WHERE USERNAME = @Username;
END
GO

CREATE PROCEDURE GET_TOP_5_RECORDS
AS
BEGIN
    SELECT TOP(5) * FROM RECORD ORDER BY TIME DESC;
END
GO

CREATE PROCEDURE GET_ALL_RECORDS
AS
BEGIN
    SELECT * FROM RECORD ORDER BY TIME DESC;
END
GO

CREATE PROCEDURE INSERT_RECORD
    @ID NVARCHAR(20),
    @TIME INT,
    @CREATED_DATE DATE,
    @ACCOUNT_ID NVARCHAR(20)
AS
BEGIN
    INSERT INTO RECORD VALUES (@ID, @TIME, @CREATED_DATE, @ACCOUNT_ID);
END
GO

CREATE PROCEDURE GET_RECORDS_BY_USER
    @ACCOUNT_ID NVARCHAR(20)
AS
BEGIN
    SELECT * FROM RECORD WHERE ACCOUNT_ID = @ACCOUNT_ID ORDER BY TIME DESC;
END
GO

SELECT * FROM ACCOUNT
SELECT * FROM RECORD