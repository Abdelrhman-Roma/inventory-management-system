//=========================================================================

//                  This File Complete No one Edit on it  (Abdelrhman Taha)

//=========================================================================
package main.model;

import java.time.LocalDate;

public class Product {

    private int id;
    private String name;
    private int quantity;
    private double price;
    private String category;
    private LocalDate productionDate;
    private LocalDate expiryDate;

    public Product(int id, String name, int quantity, double price, String category, LocalDate productionDate,
            LocalDate expiryDate) {

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.productionDate = productionDate;
        this.expiryDate = expiryDate;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", quantity=" + quantity + ", price=" + price
                + ", category='" + category + '\'' + ", productionDate=" + productionDate + ", expiryDate=" + expiryDate
                + '}';
    }

}