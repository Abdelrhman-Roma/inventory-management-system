package main.dao;

import main.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("products.csv"));

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                Product p = new Product(
                        Integer.parseInt(data[0]),
                        data[1],
                        Integer.parseInt(data[2]),
                        Double.parseDouble(data[3]),
                        data[4],
                        null,
                        null
                );

                products.add(p);
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}