package main.model; 

import java.time.LocalDate; 

public class order { 

    private int orderId; 
    private String username; 
    private Product product; 
    private int quantity; 
    private double totalPrice; 
    private LocalDate orderDate; 

    public order(int orderId, String username, Product product, int quantity) {
    this.orderId = orderId;
    this.username = username;
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = product.getPrice() * quantity;
    this.orderDate = LocalDate.now();
}

    public int getOrderId() { 
        return orderId;
    }

    public String getUsername() { 
        return username;
    }

    public Product getProduct() { 
        return product;
    }

    public int getQuantity() { 
        return quantity;
    }

    public double getTotalPrice() { 
        return totalPrice;
    }

    public LocalDate getOrderDate() { 
        return orderDate;
    }

    @Override
    public String toString() { 
        return "Order{" +
                "id=" + orderId +
                ", user='" + username + '\'' +
                ", product='" + product.getName() + '\'' +
                ", quantity=" + quantity +
                ", total=" + totalPrice +
                ", date=" + orderDate +
                '}';
    }
}