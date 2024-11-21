package org.example.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.jpa.entity.User;

import java.util.Scanner;

public class UserService {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EntityManagerFactory factory = Persistence
            .createEntityManagerFactory("main");

    public static void createUser() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            System.out.print("Input first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Input last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Input login: ");
            String login = scanner.nextLine();
            System.out.print("Input password: ");
            String password = scanner.nextLine();
            System.out.print("Input role: ");
            String role = scanner.nextLine();

            //Create user entity
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setLogin(login);
            user.setPassword(password);
            user.setRole(role);

            //Persist user entity to database
            manager.persist(user);
            manager.getTransaction().commit();
            System.out.println("User added!");
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    public static String getRole() {
        EntityManager manager = factory.createEntityManager();
        while (true) {
            System.out.print("Input login: ");
            String login = scanner.nextLine();

            System.out.print("Input password: ");
            String password = scanner.nextLine();

            // Запрос для поиска пользователя по логину и паролю
            TypedQuery<User> query = manager.createQuery(
                    "SELECT u FROM User u WHERE u.login = :login AND u.password = :password", User.class
            );
            query.setParameter("login", login);
            query.setParameter("password", password);

            try {
                User user = query.getSingleResult();
                return user.getRole();
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }
}