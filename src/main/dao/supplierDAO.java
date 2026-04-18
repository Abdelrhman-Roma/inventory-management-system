package main.dao;

import main.model.Supplier;
import java.io.*;
import java.util.*;

public class SupplierDAO {

    private final String FILE_NAME = "../suppliers.csv";

    public void save(List<Supplier> suppliers) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME);

            for (Supplier s : suppliers) {
                writer.write(
                        s.getId() + "," +
                                s.getName() + "," +
                                s.getPhone() + "," +
                                s.getAddress() + "\n");
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Supplier> load() {
        List<Supplier> suppliers = new ArrayList<>();

        try {
            File file = new File(FILE_NAME);
            if (!file.exists())
                return suppliers;

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                Supplier s = new Supplier(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        data[3]);

                suppliers.add(s);
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    // ADD
    public boolean addSupplier(Supplier newSupplier) {
        List<Supplier> suppliers = load();

        for (Supplier s : suppliers) {
            if (s.getId() == newSupplier.getId()) {
                return false; // ID already exists
            }
        }

        suppliers.add(newSupplier);
        save(suppliers);
        return true; // Successfully added
    }

    // DELETE
    public void deleteSupplier(int id) {
        List<Supplier> suppliers = load();
        suppliers.removeIf(s -> s.getId() == id);
        save(suppliers);
    }

    // UPDATE
    public void updateSupplier(Supplier updatedSupplier) {
        List<Supplier> suppliers = load();

        for (Supplier s : suppliers) {
            if (s.getId() == updatedSupplier.getId()) {
                s.setName(updatedSupplier.getName());
                s.setPhone(updatedSupplier.getPhone());
                s.setAddress(updatedSupplier.getAddress());
            }
        }

        save(suppliers);
    }

    // GET ALL (for table)
    public List<Supplier> getAllSuppliers() {
        return load();
    }
}