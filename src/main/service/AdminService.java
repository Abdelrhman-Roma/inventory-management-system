package main.service;

import java.util.ArrayList;
import main.dao.CategoryDAO;
import main.model.Category;

public class AdminService {

    private CategoryDAO dao = new CategoryDAO();

    public boolean addCategory(int id, String name) {

        Category c = new Category(id, name);

        return dao.addCategory(c);

    }

    public void updateCategory(int id, String name) {

        dao.updateCategory(id, name);

    }

    public void deleteCategory(int id) {

        dao.deleteCategory(id);

    }

    public ArrayList<Category> getAllCategories() {

        return dao.getAllCategories();

    }

}