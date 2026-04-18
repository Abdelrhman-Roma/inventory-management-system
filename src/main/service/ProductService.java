package main.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.dao.ProductDAO;
import main.model.Product;

public class ProductService {

    private List<Product> products;
    private ProductDAO dao;

    public ProductService() {
        dao = new ProductDAO();
        products = dao.load();
        System.out.println("Loaded products = " + products.size());
    }

    public void reduceQuantity(String productName, int qty) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                p.setQuantity(p.getQuantity() - qty);
                break;
            }
        }

        dao.save(products);
    }

    public void loadFromFile() {
        products = dao.load();
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product findByName(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public String addProduct(Product product) {
        String validationMessage = validateProduct(product);
        if (validationMessage != null) {
            return validationMessage;
        }

        for (Product p : products) {
            if (p.getId() == product.getId()) {
                return "Product with this ID already exists";
            }
        }

        products.add(product);
        dao.save(products);
        return "Product added successfully";
    }

    public String updateProduct(Product updatedProduct) {
        String validationMessage = validateProduct(updatedProduct);
        if (validationMessage != null) {
            return validationMessage;
        }

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

    public List<Product> searchByExpiryDate(LocalDate date) {
        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getExpiryDate() != null && p.getExpiryDate().equals(date)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> searchByProductionDate(LocalDate date) {
        List<Product> result = new ArrayList<>();

        for (Product p : products) {
            if (p.getProductionDate() != null && p.getProductionDate().equals(date)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Product> checkExpiry() {
        List<Product> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate alertLimit = today.plusDays(7);

        for (Product p : products) {
            if (p.getExpiryDate() != null
                    && !p.getExpiryDate().isBefore(today)
                    && !p.getExpiryDate().isAfter(alertLimit)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> getExpiredProducts() {
        List<Product> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Product p : products) {
            if (p.getExpiryDate() != null && p.getExpiryDate().isBefore(today)) {
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

    private String validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return "Product name is required";
        }

        if (product.getQuantity() < 0) {
            return "Quantity cannot be negative";
        }

        if (product.getPrice() < 0) {
            return "Price cannot be negative";
        }

        if (product.getProductionDate() != null
                && product.getExpiryDate() != null
                && product.getExpiryDate().isBefore(product.getProductionDate())) {
            return "Expiry date cannot be before production date";
        }

        return null;
    }
}
