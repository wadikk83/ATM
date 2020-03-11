package com.company;

import com.company.repository.DBBankCards;
import com.company.repository.DBMoneys;

import java.io.IOException;
import java.sql.*;

public class ATM {
    public static final String DB_URL = "jdbc:h2:/d:/db/test";
    public static final String DB_DRIVER = "org.h2.Driver";
    public static DBMoneys DBMoneys;
    public static DBBankCards DBBankCards;

    public static void main(String[] args) throws SQLException, Exception, IOException {

        try {
            Class.forName(DB_DRIVER); //Проверяем наличие JDBC драйвера для работы с БД
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        }

        // Инициализируем таблицы
        DBMoneys = new DBMoneys();
        DBBankCards = new DBBankCards();

        DBMoneys.createTable();
        DBBankCards.createTable();

        MenuATM menuAtm = new MenuATM();
        menuAtm.run();

    }

}