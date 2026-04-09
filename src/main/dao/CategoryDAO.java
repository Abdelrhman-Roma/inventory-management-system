package main.dao;

import java.util.ArrayList;
import java.io.*;
import main.model.Category;

public class CategoryDAO {

    private final String FILE = "categories.csv";

    public void addCategory(Category c) {

        try (FileWriter writer = new FileWriter(FILE, true)) {

            writer.write(c.getId() + "," + c.getName() + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

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

                Category c = new Category(
                        Integer.parseInt(data[0]),
                        data[1]
                );

                list.add(c);
            }

        } catch (Exception e) {

        }

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