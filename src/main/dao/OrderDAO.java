package main.dao;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import main.model.order;
import main.model.Product;

public class OrderDAO {

    private final String FILE = "orders.csv";

    public void addOrder(order o) {
        try (FileWriter writer = new FileWriter(FILE, true)) {
            writer.write(o.getOrderId() + "," + 
                         o.getUsername() + "," + 
                         o.getProduct().getName() + "," + 
                         o.getQuantity() + "," + 
                         o.getTotalPrice() + "," + 
                         o.getOrderDate() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<order> getAllOrders() {
        ArrayList<order> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line = reader.readLine(); 

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                double totalPrice = Double.parseDouble(data[4]);
                int quantity = Integer.parseInt(data[3]);
                double pricePerUnit = totalPrice / quantity;

                // بنعمل البرودكت "صوريّاً" عشان الـ order يوافق يشتغل
                Product p = new Product(0, data[2], quantity, pricePerUnit, "Unknown", null, null);

                order o = new order(
                        Integer.parseInt(data[0]), 
                        data[1],
                        p,
                        quantity
                );
                list.add(o);
            }
        } catch (Exception e) {}
        return list;
    }


}