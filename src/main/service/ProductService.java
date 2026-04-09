package main.service;

import main.model.Product;
import main.dao.ProductDAO;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private List<Product> products;
    private ProductDAO dao;

    public ProductService() {
        dao = new ProductDAO();
        products = dao.load();
        System.out.println("Loaded products = " + products.size());
    }

    public String addProduct(Product product) {

        for (Product p : products) {
            if (p.getId() == product.getId()) {
                return "Product with this ID already exists";
            }
        }

        products.add(product);
        dao.save(products);

        return "Product added successfully";
    }

    public List<Product> getAllProducts() {
        return products;
    }

    // ✅ FIXED HERE
    public String updateProduct(Product updatedProduct) {

        for (Product p : products) {
            if (p.getId() == updatedProduct.getId()) {

                p.setName(updatedProduct.getName());
                p.setQuantity(updatedProduct.getQuantity());
                p.setPrice(updatedProduct.getPrice());
                p.setCategory(updatedProduct.getCategory());
                p.setProductionDate(updatedProduct.getProductionDate());
                p.setExpiryDate(updatedProduct.getExpiryDate());

                dao.save(products);
                return "Product updated successfully";
            }
        }

        return "Product not found";
    }

    public String deleteProduct(int id) {

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.remove(i);

                dao.save(products);
                return "Product deleted successfully";
            }
        }

        return "Product not found";
    }

    // ================= SEARCH =================

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

    // ✅ ADD THESE (YOU WERE MISSING THEM)
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