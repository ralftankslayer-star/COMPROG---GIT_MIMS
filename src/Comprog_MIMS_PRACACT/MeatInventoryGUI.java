package Comprog_MIMS_PRACACT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class MeatInventoryGUI extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private InventoryDAO inventoryDAO;
    private JLabel lblConnectionStatus, lblLastUpdated;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MeatInventoryGUI frame = new MeatInventoryGUI();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    public MeatInventoryGUI() {
        inventoryDAO = new InventoryDAO();
        
        setTitle("Customer View - Meat Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 500);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JCheckBoxMenuItem themeToggle = new JCheckBoxMenuItem("Dark Mode");
        themeToggle.setSelected(true);
        themeToggle.addActionListener(e -> toggleTheme(themeToggle.isSelected()));
        optionsMenu.add(themeToggle);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblTitle = new JLabel("Available Meat Products");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 300, 25);
        contentPane.add(lblTitle);

        lblConnectionStatus = new JLabel("Status: Checking...");
        lblConnectionStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblConnectionStatus.setBounds(350, 20, 150, 20);
        contentPane.add(lblConnectionStatus);

        lblLastUpdated = new JLabel("Updated stock: Just now");
        lblLastUpdated.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblLastUpdated.setBounds(520, 20, 200, 20);
        contentPane.add(lblLastUpdated);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 810, 350);
        contentPane.add(scrollPane);
        
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Name", "Category", "Price/Kg", "Stock (kg)", "Specifics" }) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        scrollPane.setViewportView(table);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(20, 420, 100, 30);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> refreshTableData());
        contentPane.add(btnRefresh);

        JButton btnQuit = new JButton("Quit");
        btnQuit.setBounds(730, 420, 100, 30);
        btnQuit.setFocusPainted(false);
        btnQuit.addActionListener(e -> System.exit(0));
        contentPane.add(btnQuit);
        
        toggleTheme(true);
        refreshTableData();
        Timer timer = new Timer(5000, e -> refreshTableData());
        timer.start();
    }

    private void toggleTheme(boolean isDark) {
        Color bg = isDark ? new Color(30, 30, 30) : new Color(240, 240, 240);
        Color fg = isDark ? new Color(220, 220, 220) : Color.BLACK;
        Color tableBg = isDark ? new Color(45, 45, 45) : Color.WHITE;
        Color btnBg = isDark ? new Color(60, 60, 60) : new Color(200, 200, 200);

        applyThemeColors(contentPane, bg, fg, btnBg, tableBg);
    }

    private void applyThemeColors(Container container, Color bg, Color fg, Color btnBg, Color tableBg) {
        container.setBackground(bg);
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel) {
                if (c != lblConnectionStatus) {
                    c.setForeground(fg);
                }
            } else if (c instanceof JButton) {
                c.setBackground(btnBg);
                c.setForeground(fg);
            } else if (c instanceof JScrollPane) {
                c.setBackground(bg);
                ((JScrollPane) c).getViewport().setBackground(bg);
                applyThemeColors((Container) c, bg, fg, btnBg, tableBg);
            } else if (c instanceof JTable) {
                c.setBackground(tableBg);
                c.setForeground(fg);
                ((JTable) c).getTableHeader().setBackground(btnBg);
                ((JTable) c).getTableHeader().setForeground(fg);
            } else if (c instanceof Container) {
                applyThemeColors((Container) c, bg, fg, btnBg, tableBg);
            }
        }
    }
    
    private void refreshTableData() {
        if (!inventoryDAO.testConnection()) {
            lblConnectionStatus.setText("Status: DISCONNECTED");
            lblConnectionStatus.setForeground(Color.ORANGE);
            return;
        }
        lblConnectionStatus.setText("Status: CONNECTED");
        lblConnectionStatus.setForeground(new Color(50, 205, 50));

        try {
            tableModel.setRowCount(0); 
            List<MeatProduct> products = inventoryDAO.getAllProducts();
            Timestamp latestUpdate = null;
            Instant now = Instant.now();

            for (MeatProduct p : products) {
                if (p.getUpdatedAt() != null) {
                    if (latestUpdate == null || p.getUpdatedAt().after(latestUpdate)) {
                        latestUpdate = p.getUpdatedAt();
                    }
                }

                String newTag = "";
                if (p.isShowAsNew() && p.getAddedAt() != null) {
                    Duration timeSinceAdded = Duration.between(p.getAddedAt().toInstant(), now);
                    if (timeSinceAdded.toHours() <= 12) {
                        newTag = " [NEW!]";
                    }
                }

                String nameDisplay = p.getMeatName() + newTag;
                String specifics = "";
                if (p instanceof BeefProduct) specifics = "Grade: " + ((BeefProduct)p).getBeefGrade() + " | Marbling: " + ((BeefProduct)p).getMarblingScore();
                else if (p instanceof PorkProduct) specifics = "Cut: " + ((PorkProduct)p).getCutType() + " | Bone: " + (((PorkProduct)p).isHasBone() ? "Yes" : "No");
                else if (p instanceof PoultryProduct) specifics = "Part: " + ((PoultryProduct)p).getPartName() + " | Organic: " + (((PoultryProduct)p).isOrganic() ? "Yes" : "No");

                tableModel.addRow(new Object[]{p.getProductId(), nameDisplay, p.getMeatCategory(), p.getPricePerKg(), p.getWeightStock(), specifics});
            }

            if (latestUpdate != null) {
                Duration diff = Duration.between(latestUpdate.toInstant(), now);
                if (diff.toMinutes() < 1) lblLastUpdated.setText("Updated stock: Just now");
                else if (diff.toHours() < 1) lblLastUpdated.setText("Updated stock: " + diff.toMinutes() + " mins ago");
                else if (diff.toDays() < 1) lblLastUpdated.setText("Updated stock: " + diff.toHours() + " hours ago");
                else lblLastUpdated.setText("Updated stock: " + diff.toDays() + " days ago");
            }

        } catch (Exception e) {
            lblConnectionStatus.setText("Status: ERROR");
        }
    }
}