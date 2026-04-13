package main.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import main.service.ClientService;

public class ClientDashboardPart2 extends JFrame {

    private ClientService clientService;

    public ClientDashboardPart2(ClientService service) {
        this.clientService = service;

        setTitle("My Services - Invoices & Reports");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnInvoice = new JButton(" Generate My Last Invoice");
        btnInvoice.addActionListener(e -> {
            String invoice = clientService.getInvoiceText();
            JOptionPane.showMessageDialog(this, invoice, "Your Last Invoice", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnReport = new JButton(" Show My Order Report");
        btnReport.addActionListener(e -> {
            String report = clientService.getOrderReportString();
            JTextArea textArea = new JTextArea(report);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 200));

            JOptionPane.showMessageDialog(this, scrollPane, "Full Order Report", JOptionPane.PLAIN_MESSAGE);
        });

        JButton btnEmail = new JButton(" Send Order Confirmation Email");
        btnEmail.addActionListener(e -> {
            if (clientService.getCurrentClient() != null) {
                clientService.sendEmailNotification();
                JOptionPane.showMessageDialog(this,
                        "Confirmation Email Sent to: " + clientService.getCurrentClient().getEmail(),
                        "Email Sent", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());

        mainPanel.add(btnInvoice);
        mainPanel.add(btnReport);
        mainPanel.add(btnEmail);
        mainPanel.add(btnBack);

        add(mainPanel);
        setVisible(true);
    }
}
