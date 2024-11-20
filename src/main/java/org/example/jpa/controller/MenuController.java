package org.example.jpa.controller;

import org.example.jpa.service.CatalogService;
import org.example.jpa.service.UserService;

import java.util.Scanner;

public class MenuController {
    private static final Scanner scanner = new Scanner(System.in);
    static String role = UserService.getRole();

    private static void printMenu() {
        System.out.println();
        switch (role) {
            case "admin" -> {
                System.out.println("""
                        - Create    [1]
                        - Update    [2]
                        - Delete    [3]
                        - Show All  [4]
                        Select an action: \
                        """);
                System.out.println("0. Exit");
            }
            case "manager" -> {
                System.out.println("""
                        - Create    [1]
                        - Update    [2]
                        - Show All  [3]
                        Select an action: \
                        """);
                System.out.println("0. Exit");
            }
            case "client" -> {
                System.out.println("""
                        - Show All  [1]
                        Select an action: \
                        """);
                System.out.println("0. Exit");
            }
        }
    }

    private static void printCreateMenu() {
        System.out.println();
        System.out.println("""
                - Create product [1]
                - Create option [2]
                - Create user [3]
                - Exit [0]
                Select an action: \
                """);
    }

    public static void authorization() {
        String role = UserService.getRole();
        switch (role) {
            case "admin" -> run();
            case "manager" -> run();
            case "client" -> run();
        }
    }

    public static void run() {
        switch (role) {
            case "admin" -> {
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
            case "manager" -> {
                while (true) {
                    printMenu();
                    String command = scanner.nextLine();
                    switch (command) {
                        case "1" -> runCreateMenu();
                        case "2" -> CatalogService.update();
                        case "0" -> {
                            System.out.println("Exit");
                            return;
                        }
                    }
                }
            }
            case "client" -> {
                while (true) {
                    printMenu();
                    String command = scanner.nextLine();
                    switch (command) {
                        case "1" -> runCreateMenu(); //show all list of products
                        case "0" -> {
                            System.out.println("Exit");
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void runCreateMenu() {
        while (true) {
            printCreateMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> CatalogService.create();
                case "2" -> CatalogService.createCategory();
                case "3" -> UserService.createUser();
                case "0" -> {
                    System.out.println("Exit");
                    return;
                }
            }
        }
    }
}