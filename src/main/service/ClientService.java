package main.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import main.model.Client;
import main.model.Product;
import main.model.order;
import java.io.*;
import javax.swing.JOptionPane;

public class ClientService {

    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<order> orders = new ArrayList<>();
    private Client currentClient;
    private int nextOrderId = 1;
    private int nextClientId = 1;

    public ClientService() {
        loadFromFile();
        loadOrdersFromFile(); 
    }

    // ================= 1. REGISTER CLIENT =================
    public boolean registerClient(String name, String email, String password) {
        for (Client c : clients) {
            if (c.getEmail().trim().equalsIgnoreCase(email.trim())) return false; 
        }
        Client newClient = new Client(nextClientId++, name, email, password);
        clients.add(newClient);
        saveClientsToFile();
        return true; 
    }

    // ================= 2. CREATE ORDER =================
    public void createOrder(String productName, int qty) {
        if (currentClient == null) return;
        Product selected = main.Main.productService.findByName(productName);

        if (selected != null) {
            double totalPrice = qty * selected.getPrice();
            String orderDate = LocalDate.now().toString();

            // إضافة الطلب للرامات لاستخدامه في الفاتورة الفورية
            order newOrder = new order(nextOrderId, currentClient.getName(), selected, qty);
            orders.add(newOrder);
            
            saveOrderToCSV(newOrder, totalPrice, orderDate); 
        }
    }

    public void incrementOrderId() {
        this.nextOrderId++;
    }

    private void saveOrderToCSV(order o, double totalPrice, String date) {
        try (FileWriter fw = new FileWriter("orders.csv", true);
             PrintWriter pw = new PrintWriter(fw)) {
            // حفظ 6 أعمدة: OrderID, ClientID, Product, Qty, Price, Date
            pw.println(o.getOrderId() + "," + currentClient.getId() + "," + 
                       o.getProduct().getName() + "," + o.getQuantity() + "," + 
                       totalPrice + "," + date);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ================= 3. INVOICE (آخر شروة كاملة) =================
    public String getInvoiceText() {
        if (currentClient == null || orders.isEmpty()) return "No orders found!";
        
        int lastId = -1;
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (orders.get(i).getUsername().equals(currentClient.getName()) || 
                orders.get(i).getUsername().equals("Client_" + currentClient.getId())) {
                lastId = orders.get(i).getOrderId();
                break;
            }
        }

        if (lastId == -1) return "No recent orders.";

        StringBuilder sb = new StringBuilder();
        sb.append("--- RECEIPT (Order #").append(lastId).append(") ---\n");
        sb.append("Customer: ").append(currentClient.getName()).append("\n");
        sb.append("--------------------------------\n");

        double grandTotal = 0;
        for (order o : orders) {
            if (o.getOrderId() == lastId) {
                double itemTotal = o.getQuantity() * o.getProduct().getPrice();
                sb.append("- ").append(o.getProduct().getName())
                  .append(" x").append(o.getQuantity())
                  .append(" = ").append(itemTotal).append(" EGP\n");
                grandTotal += itemTotal;
            }
        }

        sb.append("--------------------------------\n");
        sb.append("GRAND TOTAL: ").append(grandTotal).append(" EGP\n");
        sb.append("--------------------------------");

        return sb.toString();
    }
//=========================================================================
    public String getOrderReportString() {
    if (currentClient == null) return "Please Login First!";
    
    StringBuilder sb = new StringBuilder();
    sb.append("================================================================================\n");
    sb.append("                      ORDER REPORT FOR: ").append(currentClient.getName().toUpperCase()).append("\n");
    sb.append("================================================================================\n");
    sb.append(String.format("%-8s %-10s %-15s %-6s %-12s %-12s\n", "OrdID", "ClientID", "Product", "Qty", "Total", "Date"));
    sb.append("--------------------------------------------------------------------------------\n");
    
    boolean hasOrders = false;
    File file = new File("orders.csv");

    try (Scanner sc = new Scanner(file)) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            
            String[] data = line.split(",");
            if (data.length >= 6) {
                // استخدام trim() هنا ضروري عشان لو فيه مسافة زيادة في الملف ميبوظش الـ ID
                String clientIdStr = data[1].trim(); 
                int currentId = currentClient.getId();
                
                if (clientIdStr.equals(String.valueOf(currentId))) {
                    sb.append(String.format("%-8s %-10s %-15s %-6s %-12s %-12s\n", 
                              data[0].trim(), data[1].trim(), data[2].trim(), 
                              data[3].trim(), data[4].trim() + " EGP", data[5].trim()));
                    hasOrders = true;
                }
            }
        }
    } catch (Exception e) {
        return "Report Error: " + e.getMessage();
    }

    return hasOrders ? sb.toString() : "No orders found for your ID: " + currentClient.getId();
}

    // ================= 5. LOAD ORDERS (لضمان بقاء البيانات القديمة) =================
    private void loadOrdersFromFile() {
        orders.clear(); 
        File file = new File("orders.csv");
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 6) {
                    try {
                        int orderId = Integer.parseInt(data[0]);
                        String prodName = data[2];
                        int qty = Integer.parseInt(data[3]);
                        
                        Product p = main.Main.productService.findByName(prodName);
                        if (p != null) {
                            // بنخزن الاسم عشان الفاتورة تعرف ترجع للداتا
                            orders.add(new order(orderId, "Client_" + data[1], p, qty));
                        }
                        
                        if (orderId >= nextOrderId) nextOrderId = orderId + 1;
                    } catch (Exception e) {}
                }
            }
        } catch (Exception e) { }
    }

    // --- باقي الدوال (Login, Update, الخ) ---
    public boolean loginCheck(String username, String password) {
        for (Client c : clients) {
            if (c.getName().equals(username) && c.getPassword().equals(password)) {
                currentClient = c;
                return true;
            }
        }
        return false;
    }

    public void loadFromFile() {
        try {
            File file = new File("Clients.csv");
            if (!file.exists()) return;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length >= 4) {
                    int id = Integer.parseInt(data[0]);
                    clients.add(new Client(id, data[1], data[2], data[3]));
                    if (id >= nextClientId) nextClientId = id + 1;
                }
            }
            scanner.close();
        } catch (Exception e) { }
    }

    private void saveClientsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("Clients.csv", false))) {
            for (Client c : clients) pw.println(c.getId() + "," + c.getName() + "," + c.getEmail() + "," + c.getPassword());
        } catch (Exception e) { }
    }

    public Client getCurrentClient() { return currentClient; }
    public void logout() { this.currentClient = null; }
    
    public void sendEmailNotification() {
        if (currentClient != null) {
            JOptionPane.showMessageDialog(null, "Order Confirmation Email sent to: " + currentClient.getEmail());
            System.out.println("Email sent to: " + currentClient.getEmail());
        }
    }
    
    public boolean updateClientSecure(int id, String oldPass, String name, String email, String newPass) {
        for (Client c : clients) {
            if (c.getId() == id && c.getPassword().equals(oldPass)) {
                if(!name.isEmpty()) c.setName(name);
                if(!email.isEmpty()) c.setEmail(email);
                if(!newPass.isEmpty()) c.setPassword(newPass);
                saveClientsToFile();
                return true;
            }
        }
        return false;
    }
}