package com.company.repository;

import com.company.ATM;

import java.sql.*;

public class BaseOperationCRUD implements RepositoryBaseOperation {

    Connection connection;  // JDBC-соединение для работы с таблицей
    String tableName;       // Имя таблицы

    public BaseOperationCRUD(String tableName) throws SQLException { // Для реальной таблицы передадим в конструктор её имя
        this.tableName = tableName;
        this.connection = getConnection(); // Установим соединение с СУБД для дальнейшей работы
    }

    @Override
    public ResultSet readBD(String sql, String description) throws SQLException {
        ResultSet rs;
        reopenConnection(); // переоткрываем (если оно неактивно) соединение с СУБД
        Statement statement = connection.createStatement();  // Создаем statement для выполнения sql-команд
        rs = statement.executeQuery(sql); // Выполняем statement - sql команду
        //statement.close();      // Закрываем statement для фиксации изменений в СУБД
        if (description != null)
            System.out.println(description);
        return rs;

    }

    @Override
    public void createBD(String sql, String description) throws SQLException {
        reopenConnection(); // переоткрываем (если оно неактивно) соединение с СУБД
        Statement statement = connection.createStatement();  // Создаем statement для выполнения sql-команд
        statement.execute(sql); // Выполняем statement - sql команду
        statement.close();      // Закрываем statement для фиксации изменений в СУБД
        if (description != null) {
            System.out.println(description);
        }
    }

    @Override
    public int updateBD(String sql, String description) throws SQLException {
        int rs;
        reopenConnection(); // переоткрываем (если оно неактивно) соединение с СУБД
        Statement statement = connection.createStatement();  // Создаем statement для выполнения sql-команд
        rs = statement.executeUpdate(sql); // Выполняем statement - sql команду
        //statement.close();      // Закрываем statement для фиксации изменений в СУБД
        if (description != null)
            System.out.println(description);
        return rs;
    }

    // Активизация соединения с СУБД, если оно не активно.
    void reopenConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = getConnection();
        }
    }

    // Получить новое соединение с БД
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ATM.DB_URL);
    }
}
