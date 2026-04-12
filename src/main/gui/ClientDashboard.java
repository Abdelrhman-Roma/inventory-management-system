package main.gui;

import java.awt.*;
import javax.swing.*;
import main.Main; 

public class ClientDashboard extends JFrame {

    public ClientDashboard() {
        setTitle("Client Dashboard");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // استخدمنا BoxLayout عشان الزراير تترص فوق بعضها بدون مسافات إجبارية
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); 

        // تعريف الزراير
        JButton loginBtn = createStyledButton("Login to My Account");
        JButton registerBtn = createStyledButton("Register New Account");
        JButton editBtn = createStyledButton("Edit Profile");
        JButton myServicesBtn = createStyledButton("My Services (Invoices & Reports)"); 
        JButton orderBtn = createStyledButton("Create New Order");
        JButton logoutBtn = createStyledButton("Logout");
        JButton backBtn = createStyledButton("Back"); 

        // تشيك هل فيه حد عامل Login؟
        boolean isLoggedIn = (Main.clientService.getCurrentClient() != null);

        // إضافة الزراير للبانل بناءً على حالة الدخول
        if (!isLoggedIn) {
            addButton(panel, loginBtn);
            addButton(panel, registerBtn);
        } else {
            addButton(panel, editBtn);
            addButton(panel, myServicesBtn);
            addButton(panel, orderBtn);
            addButton(panel, logoutBtn);
        }
        
        addButton(panel, backBtn); 

        // --- Actions ---
        
        // 1. زرار اللوجين
        loginBtn.addActionListener(e -> { 
            new LoginFrame(); 
            dispose(); 
        });

        // 2. زرار التسجيل
        registerBtn.addActionListener(e -> new ClientFrame());

        // 3. تعديل البروفايل (التعديل اللي طلبته هنا)
        editBtn.addActionListener(e -> {
            new EditClientFrame(); // بينادي الشاشة اللي بتملأ الـ ID تلقائياً
        });

        // 4. الخدمات (الفواتير والتقارير)
        myServicesBtn.addActionListener(e -> {
            new ClientDashboardPart2(Main.clientService);
        });

        // 5. إنشاء طلب شراء
        orderBtn.addActionListener(e -> new CreateOrderFrame());

        // 6. تسجيل الخروج
        logoutBtn.addActionListener(e -> {
            Main.clientService.logout();
            new ClientDashboard(); // ريفريش للشاشة عشان ترجع تخفي الزراير
            dispose();
        });

        // 7. الرجوع لشاشة اختيار الـ Role
        backBtn.addActionListener(e -> { 
            new RoleSelectionFrame(); 
            dispose(); 
        });

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        return btn;
    }

    private void addButton(JPanel panel, JButton btn) {
        panel.add(btn);
        panel.add(Box.createVerticalStrut(10)); 
    }
}