package com.company.service;

public interface BankCardOperation { //базовые операции с картой

    public boolean findBankCardByID(int id); //Найти банковскку карту по ID

    public int getBalance(int id); //Вернуть баланс по ID карты

    public boolean putMoney(int id); //Положить деньги на карту

    void getBanknotes(); //Показать сколько банкнот

    public boolean getMoney(int id); //Снять деньги с банкомата

    public boolean updateBalance(int id, int AmountOfMoney); //обновить баланс


}
