package main.dao;

import main.model.Product;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductDAO {

    private final String FILE_NAME = "C:/Users/Ghayaty/Desktop/Adv project/inventory-management-system/products.csv";

    public void save(List<Product> products) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> load() {
        List<Product> products = new ArrayList<>();

        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) return products;

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

        return products;
    }
}