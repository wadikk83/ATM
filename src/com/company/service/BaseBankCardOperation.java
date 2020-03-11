package com.company.service;

import com.company.repository.DBBankCards;
import com.company.repository.DBMoneys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

//Базовый родительский класс, куда вынесены основные методы для карт
public class BaseBankCardOperation implements BankCardOperation {
    private DBBankCards dbBankCards;
    private DBMoneys dbMoneys;

    public BaseBankCardOperation() {
        try {
            dbBankCards = new DBBankCards();
            dbMoneys = new DBMoneys();
        } catch (SQLException e) {
            e.getSQLState(); // обработка ошибки
            System.out.println("Что-то пошло не так");
            System.out.println(e.getMessage());
        }
    }


    @Override
    public boolean findBankCardByID(int id) {
        ResultSet rs;
        try {
            rs = dbBankCards.readBD("SELECT * FROM BANKCARD WHERE ID = " + id, "Ищем карту по ID");//Проверяем поиск карточки по ID
            rs.next();
            id = rs.getInt("ID");
        } catch (SQLException e) {
            e.getSQLState(); // обработка ошибки
            System.out.println("Что-то пошло не так");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public int getBalance(int id) {
        ResultSet rs;
        int balans;
        try {
            rs = dbBankCards.readBD("SELECT * FROM BANKCARD WHERE ID = " + id, "Смотрим баланс по ID");//Проверяем поиск карточки по ID
            rs.next();
            balans = rs.getInt("BALANS");
        } catch (SQLException e) {
            e.getSQLState(); // обработка ошибки
            System.out.println("Что-то пошло не так");
            System.out.println(e);
            return 0;
        }
        return balans;
    }

    @Override
    public void getBanknotes() {
        ResultSet rs;
        try {
            rs = dbMoneys.readBD("SELECT * FROM MONEY", "Ищем номинал купюр");//Проверяем поиск карточки по ID
            while (rs.next()) {
                System.out.println(rs.getInt("BANKNOTE") + " - " + rs.getString("NUMBER"));
            }
        } catch (SQLException e) {
            e.getSQLState(); // обработка ошибки
            System.out.println("Что-то пошло не так");
            System.out.println(e.getSQLState());
        }

    }

    @Override
    public boolean putMoney(int id) {
        Scanner inputKeyboard = new Scanner(System.in);
        int summa = 0;
        int number = 0;
        int banknote = 0;
        int newSumma = 0;
        ResultSet rs;
        try {
            rs = dbMoneys.readBD("SELECT * FROM MONEY", "Ищем номинал купюр");//Проверяем поиск карточки по ID
            while (rs.next()) {
                banknote = rs.getInt("BANKNOTE"); //номинал банкноты
                System.out.println("Please, enter number of banknots " + banknote);
                number = inputKeyboard.nextInt(); //какое кол-во банкнот вносим
                summa += banknote * number;  //общая сумма взноса
                newSumma = number + rs.getInt("NUMBER"); //новое кол-во банкнот в банкомате
                //обновляем кол-во банкнот в базе
                dbMoneys.updateBD("UPDATE money SET NUMBER = " + newSumma + " WHERE BANKNOTE =" + banknote, null);
            }

        } catch (SQLException e) {
            e.getSQLState(); // обработка ошибки
            System.out.println("Что-то пошло не так");
            System.out.println(e.getSQLState());
            return false;
        }
        System.out.println("Итого внесено " + summa);

        //Обновляем сумму на карте
        updateBalance(id,summa);

        return true;
    }

    @Override
    public boolean updateBalance(int id, int amountOfMoney) {
        //Обновляем сумму на карте
        try {
            int newSumma = getBalance(id) + amountOfMoney;
            dbBankCards.updateBD("UPDATE bankcard SET BALANS = " + newSumma + " WHERE ID=" + id, "Обновляем баланс в карте");
        } catch (SQLException e) {
            System.out.println("Что-то пошло не так");
            System.out.println(e.getSQLState());
            return false;
        }
        return true;
    }

    @Override
    public boolean getMoney(int id) {
        Scanner inputKeyboard = new Scanner(System.in);

        System.out.println("Please, enter the amount: ");
        int amountOfMoney = inputKeyboard.nextInt();
        if (amountOfMoney > getBalance(id)) {
            System.out.println("Sorry, not enough money on your card");
            return false;
        }
        final int MAX_VALUE = 100000; // constant value unreal
        if (amountOfMoney >= MAX_VALUE) {
            System.out.println("System error");//вдруг введенная сумма больше чем наша константа
            return false;
        }

        int[] arrayNumberOFBanknotes = new int[amountOfMoney + 1];
        //Значения массива - минимальное количество банкнот, которым можно заплатить сумму в n рублей
        arrayNumberOFBanknotes[0] = 0;

        //Делаем массив доступных купюр и перегоняем туда их из базы данных
        LinkedHashMap<Integer, Integer> avaliableArrayBanknotes = new LinkedHashMap<Integer, Integer>();
        try {
            ResultSet rs = dbMoneys.readBD("SELECT * FROM MONEY", null);
            while (rs.next()) {
                avaliableArrayBanknotes.put(rs.getInt("BANKNOTE"), rs.getInt("NUMBER"));
            }
        } catch (SQLException e) {
            e.getSQLState(); // обработка ошибки
            System.out.println("Что-то пошло не так");
            System.out.println(e.getSQLState());
        }


        for (int counter = 1; counter <= amountOfMoney; counter++) {
            arrayNumberOFBanknotes[counter] = MAX_VALUE; //помечаем что сумму counter выдать нельзя
            for (HashMap.Entry entryBanknot : avaliableArrayBanknotes.entrySet()) { //iterator по номиналам купюр
                if ((counter >= (int) entryBanknot.getKey()) &&
                        (arrayNumberOFBanknotes[counter - (int) entryBanknot.getKey()] + 1 < arrayNumberOFBanknotes[counter])) {
                    arrayNumberOFBanknotes[counter] = arrayNumberOFBanknotes[counter - (int) entryBanknot.getKey()] + 1;
                }
            }
        }

        if (arrayNumberOFBanknotes[amountOfMoney] == MAX_VALUE) {
            System.out.println("Требуемую сумму выдать невозможно");
            return false;
        } else {
            //Обновляем сумму на карте
            updateBalance(id, -amountOfMoney);

            while (amountOfMoney > 0)
                for (HashMap.Entry entryBanknot1 : avaliableArrayBanknotes.entrySet()) { //iterator по номиналам купюр
                    if (arrayNumberOFBanknotes[amountOfMoney - (int) entryBanknot1.getKey()] == arrayNumberOFBanknotes[amountOfMoney] - 1) {
                        System.out.println(entryBanknot1.getKey() + " $");
                        amountOfMoney -= (int) entryBanknot1.getKey();

                        break;
                    }
                }
        }


        return true;
    }
}
