package main.dao;

import java.util.ArrayList;
import main.model.Category;

public class CategoryDAO {

    private ArrayList<Category> categories = new ArrayList<>();

    public void addCategory(Category c) {
        categories.add(c);
    }

    public void updateCategory(int id, String name) {

        for (Category c : categories) {

            if (c.getId() == id) {
                c.setName(name);
            }

        }

    }

    public void deleteCategory(int id) {

        categories.removeIf(c -> c.getId() == id);

    }

    public ArrayList<Category> getAllCategories() {
        return categories;
    }

}