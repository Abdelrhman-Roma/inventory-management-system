package main.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import main.dao.OrderDAO;
import main.model.Client;
import main.model.Product;
import main.model.order;

public class ClientService {

    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<order> orders = new ArrayList<>();
    private OrderDAO orderDAO = new OrderDAO();
    private Client currentClient;
    private int nextOrderId = 1;
    private int nextClientId = 1;

    public ClientService() {
        loadFromFile();
        this.orders = orderDAO.getAllOrders();

        if (!orders.isEmpty()) {
            this.nextOrderId = orders.get(orders.size() - 1).getOrderId() + 1;
        }
    }

    public boolean registerClient(String name, String email, String password) {
        for (Client c : clients) {
            if (c.getEmail().trim().equalsIgnoreCase(email.trim())) {
                return false;
            }
        }
        Client newClient = new Client(nextClientId++, name, email, password);
        clients.add(newClient);
        saveClientsToFile();
        return true;
    }

    public void createOrder(String productName, int qty) {
        if (currentClient == null) {
            return;
        }

        Product selected = main.Main.productService.findByName(productName);

        if (selected != null) {
            if (qty > selected.getQuantity()) {
                JOptionPane.showMessageDialog(null, "Not enough stock!");
                return;
            }

            order newOrder = new order(nextOrderId, currentClient.getName(), selected, qty);
            orders.add(newOrder);
            orderDAO.addOrder(newOrder);
        }
    }

    public void finalizeOrder() {
        this.nextOrderId++;
    }

    public boolean updateClientSecure(int id, String oldPass, String name, String email, String newPass) {
        for (Client c : clients) {
            if (c.getId() == id && c.getPassword().equals(oldPass)) {
                if (name != null && !name.trim().isEmpty()) {
                    c.setName(name);
                }
                if (email != null && !email.trim().isEmpty()) {
                    c.setEmail(email);
                }
                if (newPass != null && !newPass.trim().isEmpty()) {
                    c.setPassword(newPass);
                }
                saveClientsToFile();
                return true;
            }
        }
        return false;
    }

    public String getInvoiceText() {
        if (currentClient == null || orders.isEmpty()) {
            return "No orders found!";
        }

        int lastId = -1;
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (orders.get(i).getUsername().equals(currentClient.getName())) {
                lastId = orders.get(i).getOrderId();
                break;
            }
        }

        if (lastId == -1) {
            return "No recent orders.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- RECEIPT (Order #").append(lastId).append(") ---\n");
        sb.append("Customer: ").append(currentClient.getName()).append("\n");
        sb.append("--------------------------------\n");

        double grandTotal = 0;
        for (order o : orders) {
            if (o.getOrderId() == lastId) {
                sb.append("- ").append(o.getProduct().getName())
                        .append(" x").append(o.getQuantity())
                        .append(" = ").append(o.getTotalPrice()).append(" EGP\n");
                grandTotal += o.getTotalPrice();
            }
        }

        sb.append("--------------------------------\n");
        sb.append("GRAND TOTAL: ").append(grandTotal).append(" EGP\n");
        sb.append("--------------------------------");

        return sb.toString();
    }

    public String getOrderReportString() {
        if (currentClient == null) {
            return "Please Login First!";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append("                      ORDER REPORT FOR: ").append(currentClient.getName().toUpperCase()).append("\n");
        sb.append("================================================================================\n");
        sb.append(String.format("%-8s %-15s %-6s %-12s %-12s\n", "OrdID", "Product", "Qty", "Total", "Date"));
        sb.append("--------------------------------------------------------------------------------\n");
        boolean hasOrders = false;
        for (order o : orders) {
            if (o.getUsername().equals(currentClient.getName())) {
                sb.append(String.format("%-8s %-15s %-6s %-12s %-12s\n",
                        o.getOrderId(), o.getProduct().getName(),
                        o.getQuantity(), o.getTotalPrice() + " EGP", o.getOrderDate()));
                hasOrders = true;
            }
        }
        return hasOrders ? sb.toString() : "No orders found.";
    }

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
        clients.clear();
        try {


            File file = new File("../Clients.csv");
            if (!file.exists()) {
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 4) {
                        int id = Integer.parseInt(data[0]);
                        clients.add(new Client(id, data[1], data[2], data[3]));
                        if (id >= nextClientId) {
                            nextClientId = id + 1;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void saveClientsToFile() {

        try (PrintWriter pw = new PrintWriter(new FileWriter("../Clients.csv", false))) {
            for (Client c : clients) pw.println(c.getId() + "," + c.getName() + "," + c.getEmail() + "," + c.getPassword());
        } catch (Exception e) { }

        try (PrintWriter pw = new PrintWriter(new FileWriter("Clients.csv", false))) {
            for (Client c : clients) {
                pw.println(c.getId() + "," + c.getName() + "," + c.getEmail() + "," + c.getPassword());
            }
        } catch (Exception e) {
        }
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void logout() {
        this.currentClient = null;
    }

    public void sendEmailNotification() {
        if (currentClient != null) {
            JOptionPane.showMessageDialog(null, "Order Confirmation Email sent to: " + currentClient.getEmail());
        }
    }
}
