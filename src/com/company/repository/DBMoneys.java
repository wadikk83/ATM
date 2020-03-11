package com.company.repository;

import java.sql.SQLException;

public class DBMoneys extends BaseOperationCRUD {
    public DBMoneys() throws SQLException {
        super("Money");
    }

    public void createTable() throws SQLException {
        super.createBD("CREATE TABLE IF NOT EXISTS money(" +
                "id TINYINT AUTO_INCREMENT PRIMARY KEY," +
                        "banknote INTEGER NOT NULL DEFAULT 0," +
                        "number INTEGER NOT NULL DEFAULT 0)", "Создана таблица " + tableName);
    }
}

