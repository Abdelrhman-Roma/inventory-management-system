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
        
        // Use Set for unique order IDs
        java.util.Set<Integer> uniqueOrderIds = new java.util.HashSet<>();
        
        // Maps for product-level aggregation
        java.util.Map<String, Double> productProfit = new java.util.HashMap<>();
        java.util.Map<String, Integer> productQuantity = new java.util.HashMap<>();
        java.util.Map<String, Double> productTotalPrice = new java.util.HashMap<>();
        
        double totalProfit = 0.0;
        int totalUnits = 0;
        
        for (order o : orders) {
            uniqueOrderIds.add(o.getOrderId());  // Add to unique set
            
            String productName = o.getProduct().getName();
            double orderProfit = o.getTotalPrice();
            int qty = o.getQuantity();
            
            // Aggregate per product
            productProfit.put(productName, productProfit.getOrDefault(productName, 0.0) + orderProfit);
            productQuantity.put(productName, productQuantity.getOrDefault(productName, 0) + qty);
            productTotalPrice.put(productName, productTotalPrice.getOrDefault(productName, 0.0) + orderProfit);
            
            totalProfit += orderProfit;
            totalUnits += qty;
        }
        
        // Build table output with better formatting
        StringBuilder report = new StringBuilder();
        report.append("\n");
        report.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
        report.append("║                           📊 PROFIT REPORT 📊                                 ║\n");
        report.append("╚════════════════════════════════════════════════════════════════════════════════╝\n");
        report.append("\n");
        
        // Summary Statistics
        report.append("📈 SUMMARY STATISTICS\n");
        report.append("├─ Total Orders: ").append(uniqueOrderIds.size()).append("\n");
        report.append("├─ Total Order Lines: ").append(orders.size()).append("\n");
        report.append("├─ Total Units Sold: ").append(totalUnits).append("\n");
        report.append("└─ Total Revenue: ").append(String.format("%.2f EGP", totalProfit)).append("\n");
        report.append("\n");
        
        // Product Details Table
        report.append("📦 PRODUCT SALES BREAKDOWN\n");
        report.append("┌────────────────────────┬───────────┬────────────┬──────────────┐\n");
        report.append(String.format("│ %-22s │ %9s │ %10s │ %12s │\n", "Product Name", "Qty Sold", "Profit", "Avg Price"));
        report.append("├────────────────────────┼───────────┼────────────┼──────────────┤\n");
        
        for (String product : productProfit.keySet()) {
            double profit = productProfit.get(product);
            int qty = productQuantity.get(product);
            double avgPrice = qty > 0 ? productTotalPrice.get(product) / qty : 0.0;
            report.append(String.format("│ %-22s │ %9d │ %10.2f │ %12.2f │\n", 
                product.substring(0, Math.min(22, product.length())), qty, profit, avgPrice));
        }
        
        report.append("└────────────────────────┴───────────┴────────────┴──────────────┘\n");
        report.append("\n");
        
        // Footer
        report.append("═══════════════════════════════════════════════════════════════════════════════════\n");
        report.append(String.format("💰 TOTAL PROFIT: %.2f EGP\n", totalProfit));
        report.append("═══════════════════════════════════════════════════════════════════════════════════\n");
        
        return report.toString();
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
