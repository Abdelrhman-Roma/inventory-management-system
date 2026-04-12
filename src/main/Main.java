package main;

import main.gui.RoleSelectionFrame;
import main.service.ClientService;
import main.service.ProductService;

public class Main {
    public static ProductService productService = new ProductService();
    public static ClientService clientService = new ClientService();

    public static void main(String[] args) {

        ProductService productService = new ProductService();
        new RoleSelectionFrame();
    }
}
