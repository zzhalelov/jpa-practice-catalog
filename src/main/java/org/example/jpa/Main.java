package org.example.jpa;

import jakarta.persistence.*;
import org.example.jpa.entity.Category;
import org.example.jpa.entity.Option;
import org.example.jpa.entity.Product;
import org.example.jpa.entity.Value;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> create();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> createCategory();
                case "0" -> {
                    System.out.println("Exit");
                    return;
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("""
                - Create [1]
                - Update [2]
                - Delete [3]
                - Create Category [4]
                Select an action: \
                """);
        System.out.println("0. Exit");
    }

    private static void create() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            // Select categories to display as list
            TypedQuery<Category> categoryTypedQuery = manager.createQuery(
                    "SELECT c FROM Category c ORDER BY c.name", Category.class
            );
            List<Category> categories = categoryTypedQuery.getResultList();
            for (int i = 0; i < categories.size(); i++) {
                System.out.println(categories.get(i).getName());
            }

            // Input category number
            System.out.print("Select category by number: ");
            String categoryNumString = scanner.nextLine();
            int categoryNum = Integer.parseInt(categoryNumString);
            Category category = categories.get(categoryNum);

            // Input product name
            System.out.print("Input product's name: ");
            String productNameString = scanner.nextLine();

            // Input product price
            System.out.print("Input product's price: ");
            String productPriceString = scanner.nextLine();
            double productPrice = Double.parseDouble(productPriceString);

            // Create product entity
            Product product = new Product();
            product.setCategory(category);
            product.setName(productNameString);
            product.setPrice(productPrice);

            // Persist product entity to database
            manager.persist(product);

            // Iterate category options
            for (Option option : category.getOptions()) {
                // Input option value
                System.out.print(option.getName() + ": ");
                String valueString = scanner.nextLine();

                // Create value entity
                Value value = new Value();
                value.setProduct(product);
                value.setOption(option);
                value.setValue(valueString);

                // Persist value entity to database
                manager.persist(value);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    private static void createCategory() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            //Input category name
            System.out.print("Input category's name: ");
            String categoryName = scanner.nextLine();

            //Create category entity
            Category category = new Category();
            category.setName(categoryName);

            //Persist category entity to database
            manager.persist(category);
            System.out.print("Insert option's name for category [%s]: ");
            String optionString = scanner.nextLine();

            //Create option entity
            Option option = new Option();
            option.setName(optionString);

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private static void update() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            // Input product id
            System.out.print("Input product id: ");
            String productIdString = scanner.nextLine();
            int productId = Integer.parseInt(productIdString);
            Product product = manager.find(Product.class, productId);

            // Input new product name
            System.out.println("Input new product name" + product.getName() + ": ");
            String newProductNameString = scanner.nextLine();
            if (!newProductNameString.isEmpty()) {
                product.setName(newProductNameString);
            }

            // Input new product price
            System.out.println("Input new product price" + product.getPrice() + ": ");
            String newProductPriceString = scanner.nextLine();
            if (!newProductPriceString.isEmpty()) {
                int newProductPrice = Integer.parseInt(newProductPriceString);
                product.setPrice(newProductPrice);
            }
            for (Option option : product.getCategory().getOptions()) {
                TypedQuery<Value> valueTypedQuery = manager.createQuery(
                        "SELECT v FROM Value v WHERE v.product = ?1 AND v.option = ?2", Value.class
                );
                valueTypedQuery.setParameter(1, product);
                valueTypedQuery.setParameter(2, option);
                try {
                    Value value = valueTypedQuery.getSingleResult();
                    System.out.printf("%s [%s]: ", option.getName(), value.getValue());
                    String newValueString = scanner.nextLine();
                    if (!newValueString.isEmpty()) {
                        value.setValue(newValueString);
                    }
                } catch (NoResultException e) {
                    System.out.printf("%s: ", option.getName());
                    String valueString = scanner.nextLine();
                    Value value = new Value();
                    value.setProduct(product);
                    value.setOption(option);
                    value.setValue(valueString);
                    manager.persist(value);
                }
                valueTypedQuery.getSingleResult();
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    private static void delete() {
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();

            // Input product id
            System.out.print("Input product id: ");
            String productIdString = scanner.nextLine();
            int productId = Integer.parseInt(productIdString);
            Product product = manager.find(Product.class, productId);

            // Remove entity from database
            manager.remove(product);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}