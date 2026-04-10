package main.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import main.model.Client;
import main.model.Product;
import main.model.order;
import java.util.List;

public class ClientService {

    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<order> orders = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();

    private Scanner input = new Scanner(System.in);
    private Client currentClient;

    private int nextOrderId = 1;
    private int nextClientId = 1;

    public ClientService() {
         loadFromFile();
        products.add(new Product(1, "Milk", 10, 20.0, "Dairy",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1)));

        products.add(new Product(2, "Bread", 15, 5.0, "Food",
                LocalDate.of(2024, 1, 5),
                LocalDate.of(2024, 1, 20)));

        products.add(new Product(3, "yogurt", 50, 15, "Dairy",
                LocalDate.of(2024, 1, 5),
                LocalDate.of(2024, 1, 20)));
    }

    // ================= REGISTER CLIENT =================
    
public boolean registerClient(String name, String email, String password){

    // ✅ الأول نعمل check
    for (Client c : clients) {

        if (c.getEmail().trim().equalsIgnoreCase(email.trim())) {
            return false; // الإيميل موجود
        }
    }

    Client newClient = new Client(nextClientId++, name, email, password);
    clients.add(newClient);

    try {
        java.io.FileWriter writer = new java.io.FileWriter("Clients.csv", true);
        writer.write(newClient.getId() + "," + name + "," + email + "," + password + "\n");
        writer.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return true; 
}
    // ================= LOGIN CHECK =================
  public boolean loginCheck(String username, String password) {

        for (Client c : clients) {

            if (c.getName().equals(username) && c.getPassword().equals(password)) {
                currentClient = c;
                return true;
            }
        }

        return false;
    }

    // ================= LOAD FROM FILE =================
     public void loadFromFile() {

    try {
        java.io.File file = new java.io.File("Clients.csv");

        if (!file.exists()) return;

        java.util.Scanner scanner = new java.util.Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] data = line.split(",");

            int id = Integer.parseInt(data[0]);
            String name = data[1];
            String email = data[2];
            String password = data[3];

            
            clients.add(new Client(id, name, email, password));

            if (id >= nextClientId) {
                nextClientId = id + 1;
            }
        }

        scanner.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

  // ================= UPDATE CLIENT =================
public boolean updateClientById(int id, String name, String email, String password) {

    Client target = null;

    // 🔹 نجيب الشخص بالـ ID
    for (Client c : clients) {
        if (c.getId() == id) {
            target = c;
            break;
        }
    }

    if (target == null) {
        return false;
    }

    // check email
    for (Client c : clients) {
        if (c.getEmail().equalsIgnoreCase(email) && c.getId() != id) {
            return false;
        }
    }

    target.editData(name, email, password);

    try {
        java.io.FileWriter writer = new java.io.FileWriter("Clients.csv", false);

        for (Client c : clients) {
            writer.write(c.getId() + "," + c.getName() + "," + c.getEmail() + "," + c.getPassword() + "\n");
        }

        writer.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return true;
}


// ================= SECURE UPDATE CLIENT =================
public boolean updateClientSecure(int id, String oldPassword, String name, String email, String newPassword) {

    Client target = null;

    for (Client c : clients) {
        if (c.getId() == id) {
            target = c;
            break;
        }
    }

    if (target == null) return false;

    // تحقق من الباسورد القديم
    if (!target.getPassword().equals(oldPassword)) return false;

    // check email
    for (Client c : clients) {
        if (c.getEmail().equalsIgnoreCase(email) && c.getId() != id) {
            return false;
        }
    }

    if (!name.isEmpty()) {
    target.setName(name);
    }

    if (!email.isEmpty()) {
    target.setEmail(email);
    }

    if (!newPassword.isEmpty()) {
    target.setPassword(newPassword);
    }

    try {
        java.io.FileWriter writer = new java.io.FileWriter("Clients.csv", false);

        for (Client c : clients) {
            writer.write(c.getId() + "," + c.getName() + "," + c.getEmail() + "," + c.getPassword() + "\n");
        }

        writer.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return true;
}

    // ================= CREATE ORDER =================
    public void createOrder(String productName, int qty) {

        if (currentClient == null) {
            System.out.println("Login first!");
            return;
        }

        Product selected = null;

        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                selected = p;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Product not found!");
            return;
        }

        if (qty > selected.getQuantity()) {
            System.out.println("Not enough quantity!");
            return;
        }

        selected.setQuantity(selected.getQuantity() - qty);

        order newOrder = new order(nextOrderId++, currentClient.getName(), selected, qty);

        orders.add(newOrder);

        System.out.println("Order created!");
    }
}