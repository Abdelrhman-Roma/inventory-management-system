package main.model;

public class Client {
    // Client data
    private int id;
    private String name;
    private String email;
    private String password;

    // Update data
    public void editData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    // Main constructor
    public Client(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Get id
    public int getId() {
        return id;
    }

    
    public String getName() {
        return name;
    }

    
    public String getEmail() {
        return email;
    }

    
    public String getPassword() {
        return password;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    // CSV format
    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + password;
    }
}