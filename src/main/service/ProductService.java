//=========================================================================

//                  This File Complete No one Edit on it   (Abdelrhman Taha)

//=========================================================================

package main.service;

import main.model.Product;
import java.util.ArrayList;
import java.util.List;

import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;

public class ProductService {

    private static List<Product> products = new ArrayList<>();

    // ================= SAVE & LOAD =================

    public void saveToFile() {
        try {
            FileWriter writer = new FileWriter("products.csv");

            for (Product p : products) {
                writer.write(
                    p.getId() + "," +
                    p.getName() + "," +
                    p.getQuantity() + "," +
                    p.getPrice() + "," +
                    p.getCategory() + "," +
                    p.getProductionDate() + "," +
                    p.getExpiryDate() + "\n"
                );
            }

            writer.close();
            System.out.println("Data saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        try {
            File file = new File("products.csv");

            if (!file.exists()) return;

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                Product p = new Product(
                    Integer.parseInt(data[0]),
                    data[1],
                    Integer.parseInt(data[2]),
                    Double.parseDouble(data[3]),
                    data[4],
                    java.time.LocalDate.parse(data[5]),
                    java.time.LocalDate.parse(data[6])
                );

                products.add(p);
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ORIGINAL CODE (WITH AUTO SAVE) =================

    public String addProduct(Product product) {

        for (Product p : products) {
            if (p.getId() == product.getId()) {
                return "Product with this ID already exists";
            }
        }

        products.add(product);
        saveToFile();   // AUTO SAVE

        return "Product added successfully";
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public String updateProduct(int id, String name, int quantity, double price) {

        for (Product p : products) {
            if (p.getId() == id) {

                p.setName(name);
                p.setQuantity(quantity);
                p.setPrice(price);

                saveToFile();   //  AUTO SAVE
                return "Product updated successfully";
            }
        }

        return "Product not found";
    }

    public String deleteProduct(int id) {

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.remove(i);

                saveToFile();   //  AUTO SAVE

                return "Product deleted successfully";
            }
        }

        return "Product not found";
    }

    public List<Product> searchByName(String name) {

        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> searchByCategory(String category) {

        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> searchByExpiryDate(java.time.LocalDate date) {

        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getExpiryDate().isBefore(date)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> searchByProductionDate(java.time.LocalDate date) {

        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getProductionDate().isBefore(date)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> checkExpiry() {

        List<Product> result = new ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();

        for (Product p : products) {
            if (p.getExpiryDate().isBefore(today.plusDays(7))) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> checkLowStock() {

        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getQuantity() <= 5) {
                result.add(p);
            }
        }
        return result;
    }
}