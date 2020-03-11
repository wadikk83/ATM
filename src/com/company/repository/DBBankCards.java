package com.company.repository;

import com.company.ATM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBBankCards extends BaseOperationCRUD {
    // База данный банковских карт
    // Bank card database

    public DBBankCards() throws SQLException {
        super("BankCard");
    }

    public void createTable() throws SQLException {
        super.createBD ("CREATE TABLE IF NOT EXISTS bankCard(" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                "pin INTEGER NOT NULL DEFAULT 1111," +
                "name VARCHAR(50) NOT NULL," +
                "balans INTEGER NOT NULL DEFAULT 0)", "Создана таблица " + tableName);
    }
}
