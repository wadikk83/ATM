package com.company.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RepositoryBaseOperation {
    public void createBD(String sql, String description) throws SQLException; //Создаем базу данных
    public ResultSet readBD(String sql, String description) throws SQLException; //читаем базу данных
    public int updateBD(String sql, String description) throws SQLException; //обновляем записи в БД
}
