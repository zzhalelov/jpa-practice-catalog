package org.example.jpa;

import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    private static void printMenu() {
        System.out.println();
        System.out.println("""
                - Create [1]
                - Update [2]
                - Delete [3]
                Select an action: \
                """);
        System.out.println("0. Exit");
    }

    private static void printCreateMenu() {
        System.out.println();
        System.out.println("""
                - Create product [1]
                - Create option [2]
                - Back to main menu [3]
                - Exit [0]
                Select an action: \
                """);
    }

    public static void runCreateMenu() {
        while (true) {
            printCreateMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> CatalogService.create();
                case "2" -> CatalogService.createCategory();
                case "3" -> {
                    return;
                }
                case "0" -> {
                    System.out.println("Exit");
                    return;
                }
            }
        }
    }

    public static void run() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> runCreateMenu();
                case "2" -> CatalogService.update();
                case "3" -> CatalogService.delete();
                case "0" -> {
                    System.out.println("Exit");
                    return;
                }
            }
        }
    }
}