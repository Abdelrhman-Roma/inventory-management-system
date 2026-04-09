import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SupplierFrame extends Application {

    private SupplierDAO supplierDAO = new SupplierDAO();
    private TableView<Supplier> tableView = new TableView<>();
    private ObservableList<Supplier> supplierList;

    private TextField nameField = new TextField();
    private TextField contactPersonField = new TextField();
    private TextField emailField = new TextField();
    private TextField phoneField = new TextField();

    private Supplier selectedSupplier = null;

    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Supplier Management");

        // Table columns
        TableColumn<Supplier, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Supplier, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Supplier, String> contactCol = new TableColumn<>("Contact Person");
        contactCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));

        TableColumn<Supplier, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Supplier, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));

        tableView.getColumns().addAll(idCol, nameCol, contactCol, emailCol, phoneCol);
        refreshTable();

        // Selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedSupplier = newSelection;
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });

        // Form layout
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Contact Person:"), 0, 1);
        form.add(contactPersonField, 1, 1);
        form.add(new Label("Email:"), 0, 2);
        form.add(emailField, 1, 2);
        form.add(new Label("Phone:"), 0, 3);
        form.add(phoneField, 1, 3);

        // Buttons
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button clearBtn = new Button("Clear");

        addBtn.setOnAction(e -> addSupplier());
        updateBtn.setOnAction(e -> updateSupplier());
        deleteBtn.setOnAction(e -> deleteSupplier());
        clearBtn.setOnAction(e -> clearFields());

        HBox buttons = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        VBox vbox = new VBox(10, tableView, form, buttons);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshTable() {
        supplierList = FXCollections.observableArrayList(supplierDAO.getAllSuppliers());
        tableView.setItems(supplierList);
    }

    private void populateFields(Supplier supplier) {
        nameField.setText(supplier.getName());
        contactPersonField.setText(supplier.getContactPerson());
        emailField.setText(supplier.getEmail());
        phoneField.setText(supplier.getPhone());
    }

    private void clearFields() {
        nameField.clear();
        contactPersonField.clear();
        emailField.clear();
        phoneField.clear();
        tableView.getSelectionModel().clearSelection();
        selectedSupplier = null;
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() || contactPersonField.getText().isEmpty()
                || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill all fields");
            return false;
        }
        return true;
    }

    private void addSupplier() {
        if (!validateFields()) return;
        Supplier supplier = new Supplier(0, nameField.getText(), contactPersonField.getText(), emailField.getText(), phoneField.getText());
        if (supplierDAO.addSupplier(supplier)) {
            refreshTable();
            clearFields();
        }
    }

    private void updateSupplier() {
        if (selectedSupplier == null || !validateFields()) {
            showAlert("Selection Error", "Select a supplier to update");
            return;
        }
        selectedSupplier.setName(nameField.getText());
        selectedSupplier.setContactPerson(contactPersonField.getText());
        selectedSupplier.setEmail(emailField.getText());
        selectedSupplier.setPhone(phoneField.getText());

        if (supplierDAO.updateSupplier(selectedSupplier)) {
            refreshTable();
            clearFields();
        }
    }

    private void deleteSupplier() {
        if (selectedSupplier == null) {
            showAlert("Selection Error", "Select a supplier to delete");
            return;
        }
        if (supplierDAO.deleteSupplier(selectedSupplier.getId())) {
            refreshTable();
            clearFields();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method for other modules (e.g., Abdelrhman Taha) to get all suppliers
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public static void main(String[] args) {
        launch(args);
    }
}