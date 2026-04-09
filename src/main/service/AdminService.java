package main.service;

import java.util.ArrayList;
import main.dao.CategoryDAO;
import main.model.Category;
import main.dao.ProductDAO; // ghayaty for product calls

// Omar Elghayaty //
import main.model.Product;   // so we can use Product 
import main.model.order;     // so we can use Order

import java.util.List;       // list interface
import java.util.ArrayList;  // actual list implementation
// Omar Elghayaty //

public class AdminService {

    private CategoryDAO dao = new CategoryDAO();
    private ProductDAO productDao = new ProductDAO();

  
// list to store orders temporarily ->Ghayaty
private List<order> orders = new ArrayList<>();

    // constructor (runs when object is created) -> Ghayaty
    

    public void addCategory(int id, String name) {

        Category c = new Category(id, name);
        dao.addCategory(c);

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
        
        List<Product> products = productDao.getAllProducts();

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
    List<Product> products = productDao.getAllProducts();
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

        double total = 0;

        for (order o : orders) {
            total += o.getTotalPrice();
        }

        return "Total Profit: " + total;
    }
// =======================//

// Offer -> Ghayaty//
    public void addOffer(int productId, double discount, String start, String end) {

        System.out.println("Offer Added → Product: " + productId +
                " Discount: " + discount +
                "% From: " + start +
                " To: " + end);
    }
// ==================//
}