package main.gui;
import java.awt.*;
import javax.swing.*;
import main.Main;
import main.service.ClientService;

public class ClientFrame extends JFrame {

    public ClientFrame() {
       setTitle("Register Client");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(4, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JButton registerBtn = new JButton("Register");
    JButton backBtn = new JButton("Back");

    panel.add(new JLabel("Name:"));
    panel.add(nameField);

    panel.add(new JLabel("Email:"));
    panel.add(emailField);

    panel.add(new JLabel("Password:"));
    panel.add(passwordField);

    panel.add(registerBtn);
    panel.add(backBtn);

    add(panel);

    setVisible(true);
    
       registerBtn.addActionListener(e -> {

    String name = nameField.getText();
    String email = emailField.getText();
    String password = new String(passwordField.getPassword());

    ClientService service = Main.clientService; 

    boolean success = service.registerClient(name, email, password);

    if (success) {
        JOptionPane.showMessageDialog(this, "Registered Successfully!");
    } else {
        JOptionPane.showMessageDialog(this, "Email already exists!");
    }
});

        setVisible(true);
    }
}