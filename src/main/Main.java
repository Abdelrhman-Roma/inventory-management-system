package main;

import main.gui.RoleSelectionFrame;
import main.service.ProductService;

public class Main {

    public static void main(String[] args) {

        ProductService productService = new ProductService();
        productService.loadFromFile(); // load data at start

        new RoleSelectionFrame();

    }
}