package main.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import main.model.Category;

public class CategoryDAO {

    private final String FILE = "categories.csv";

    public boolean addCategory(Category c) {
        ArrayList<Category> list = getAllCategories();

        for (Category cat : list) {
            if (cat.getId() == c.getId()) {
                return false;
            }
        }

        list.add(c);
        list.sort((a, b) -> a.getId() - b.getId());
        saveAll(list);
        return true;
    }

    public void updateCategory(int id, String name) {
        ArrayList<Category> list = getAllCategories();

        for (Category c : list) {
            if (c.getId() == id) {
                c.setName(name);
            }
        }

        saveAll(list);
    }

    public void deleteCategory(int id) {
        ArrayList<Category> list = getAllCategories();
        list.removeIf(c -> c.getId() == id);
        saveAll(list);
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];

                Category c = new Category(id, name);
                list.add(c);
            }

        } catch (Exception e) {
        }

        list.sort((a, b) -> a.getId() - b.getId());
        return list;
    }

    private void saveAll(ArrayList<Category> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {
            for (Category c : list) {
                writer.println(c.getId() + "," + c.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
