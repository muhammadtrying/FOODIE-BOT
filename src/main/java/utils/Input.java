package utils;

import java.util.Scanner;

public interface Input {
    Scanner scannerInt = new Scanner(System.in);
    Scanner scannerStr = new Scanner(System.in);

    static Integer inputInt(String msg) {
        System.out.print(msg + ": ");
        return scannerInt.hasNextInt() ? scannerInt.nextInt() : inputInt(msg);
    }

    static String inputStr(String msg) {
        return scannerStr.nextLine();
    }
}
