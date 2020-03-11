package com.company;

import com.company.service.BaseBankCardOperation;

import java.util.Scanner;

public class MenuATM {
    static Scanner inputKeyboard = new Scanner(System.in);
    private static int idCard;
    private static BaseBankCardOperation baseBankCardOperation = new BaseBankCardOperation();



    public static void mainMenu() {
        byte choose = 0;
        boolean isQuit = false; //крутим меню пока false
        String inputLine = "";

        String menu = "\nChoose one of the following:\n" + "1.Display Balance\n" + "2.Put money\n" + "3.Get money\n" + "4.Log Out";

        while (!isQuit) {
            System.out.println(menu);
            inputLine = inputKeyboard.next();

            try {
                choose = Byte.parseByte(inputLine);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect");
                continue;
            }


            switch (choose) {
                case 1:// 1. Display Balance
                    System.out.println("Display balance");
                    //card.viewBalance(card.getId());
                    System.out.println(baseBankCardOperation.getBalance(idCard));
                    break;

                case 2:// 2. Put money
                    System.out.println("Put money");
                    if (baseBankCardOperation.putMoney(idCard)) {
                        System.out.println("Operation successful");
                    } else {
                        System.out.println("Operation failed");
                    }
                    break;

                case 3:// 3. Get money
                    System.out.println("Get money");
                    if (baseBankCardOperation.getMoney(idCard)) {
                        System.out.println("Operation successful");
                    } else {
                        System.out.println("Operation failed");
                    }
                    break;

                case 4:// 4. Log out
                    System.out.println("You are logged out.");
                    isQuit = true;
                    break;

                case 5://Get banknotes
                    baseBankCardOperation.getBanknotes();
                    break;

                default:
                    System.out.println("No such menu");
                    break;
            }
        }
        System.out.println("Bye-Bye");
    }

    public void run() {
        while (true) {
            System.out.println("Please, enter id card or print \"0\" to exit");
            idCard = inputKeyboard.nextInt();
            if (idCard == 0) {
                System.out.println("You are logged out.");
                return;
            }
            if (baseBankCardOperation.findBankCardByID(idCard)) {
                System.out.println("You ID is correct.");
                break;
            } else {
                System.out.println("You ID is incorrect. Please try again ");
            }
        }

            mainMenu();


    }
}
