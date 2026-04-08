//=========================================================================

//                  This File Complete No one Edit on it   (Abdelrhman Taha)

//=========================================================================
package main.service;

import main.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private static List<Product> products = new ArrayList<>();

    public String addProduct(Product product) {

        for (Product p : products) {
            if (p.getId() == product.getId()) {

                return "Product with this ID already exists";
            }
        }

        products.add(product);
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

                
                return "Product updated successfully";
            }
        }

        return "Product not found";
    }

    public String deleteProduct(int id) {

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.remove(i);
               
                return  "Product deleted successfully";
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