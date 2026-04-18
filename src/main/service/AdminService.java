package main.service;

import java.util.ArrayList;
import java.util.List;
import main.dao.CategoryDAO;
import main.dao.OrderDAO;
import main.dao.ProductDAO; // ghayaty for product calls
import main.model.Category;
import main.model.Product;   // so we can use Product 
import main.model.order;     // so we can use Order

public class AdminService {

    private CategoryDAO dao = new CategoryDAO();
    private ProductDAO productDao = new ProductDAO();
    private OrderDAO orderDao = new OrderDAO();

    // constructor (runs when object is created) -> Ghayaty
    public boolean addCategory(int id, String name) {
        Category c = new Category(id, name);

        return dao.addCategory(c);
    }

    public void updateCategory(int id, String name) {

        dao.updateCategory(id, name);

    }

    public void deleteCategory(int id) {

        dao.deleteCategory(id);

    }

    public ArrayList<Category> getAllCategories() {

        return dao.getAllCategories();

    }
// Product report function -> Ghayaty //
        public String generateProductReport() {

        StringBuilder report = new StringBuilder();

        report.append("=== Product Report ===\n");
        
        List<Product> products = productDao.load();

        for (Product p : products) {
            report.append("ID: ").append(p.getId())
                  .append(" | Name: ").append(p.getName())
                  .append(" | Price: ").append(p.getPrice())
                  .append(" | Qty: ").append(p.getQuantity())
                  .append(" | Category: ").append(p.getCategory())
                  .append("\n");
        }

        return report.toString();
    }
  // ==============================================//

// Gategory Report function -> Ghayaty //
  public String generateCategoryReport() {

    StringBuilder report = new StringBuilder();

    report.append("=== Category Report ===\n");

    // Map to count products per category
    java.util.HashMap<String, Integer> categoryCount = new java.util.HashMap<>();

    // loop through products
    List<Product> products = productDao.load();
    for (Product p : products)
   {

        String category = p.getCategory();

        // if category exists → increase count
        if (categoryCount.containsKey(category)) {
            categoryCount.put(category, categoryCount.get(category) + 1);
        } else {
            categoryCount.put(category, 1);
        }
    }

    // print results
    for (String cat : categoryCount.keySet()) {
        report.append(cat)
              .append(": ")
              .append(categoryCount.get(cat))
              .append(" products\n");
    }

    return report.toString();
}
// ===================================//

// Profit Report -> Ghayaty //
    public String generateProfitReport() {
        List<order> orders = orderDao.getAllOrders();
        double total = 0;

        for (order o : orders) {
            total += o.getTotalPrice();
        }

        return "Total Profit: " + total + "\nOrder Count: " + orders.size();
    }
// =======================//

// Offer -> Ghayaty//
    public void addOffer(int productId, double discount, String start, String end) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive integer.");
        }
        if (discount < 1 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 1 and 100.");
        }

        List<Product> products = productDao.load();
        boolean found = false;

        for (Product p : products) {
            if (p.getId() == productId) {
                found = true;
                double oldPrice = p.getPrice();
                double newPrice = oldPrice - (oldPrice * discount / 100);
                p.setPrice(newPrice);
                System.out.println("Offer Applied on " + p.getName() +
                        " Old Price: " + oldPrice +
                        " New Price: " + newPrice);
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("No product found with ID " + productId + ".");
        }

        // save updated products back to CSV
        productDao.save(products);
    }

    public boolean productExists(int productId) {
        if (productId <= 0) return false;
        List<Product> products = productDao.load();
        for (Product p : products) {
            if (p.getId() == productId) {
                return true;
            }
        }
        return false;
    }
}
