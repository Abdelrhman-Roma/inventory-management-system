package main.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import main.model.Category;

public class CategoryDAO {

    private String file = "../categories.csv";

    public String addCategory(Category c) {

        ArrayList<Category> list = getAllCategories();

        for (int i = 0; i < list.size(); i++) {

            Category cat = list.get(i);

            if (cat.getId() == c.getId()) {
                return "id";
            }

            if (cat.getName().equalsIgnoreCase(c.getName())) {
                return "name";
            }
        }

        list.add(c);
        sortById(list);
        saveAll(list);

        return "ok";
    }

    public void updateCategory(int id, String name) {

        ArrayList<Category> list = getAllCategories();

        for (int i = 0; i < list.size(); i++) {

            Category c = list.get(i);

            if (c.getId() == id) {
                c.setName(name);
            }
        }

        sortById(list);
        saveAll(list);
    }

    public void deleteCategory(int id) {

        ArrayList<Category> list = getAllCategories();

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getId() == id) {
                list.remove(i);
                break;
            }
        }

        saveAll(list);
    }

    public ArrayList<Category> getAllCategories() {

        ArrayList<Category> list = new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];

                Category c = new Category(id, name);
                list.add(c);
            }

            reader.close();

        } catch (Exception e) {
        }

        sortById(list);
        return list;
    }

    private void saveAll(ArrayList<Category> list) {

        try {

            PrintWriter writer = new PrintWriter(new FileWriter(file));

            for (int i = 0; i < list.size(); i++) {

                Category c = list.get(i);
                writer.println(c.getId() + "," + c.getName());
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortById(ArrayList<Category> list) {

        for (int i = 0; i < list.size(); i++) {

            for (int j = i + 1; j < list.size(); j++) {

                if (list.get(i).getId() > list.get(j).getId()) {

                    Category temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }
}