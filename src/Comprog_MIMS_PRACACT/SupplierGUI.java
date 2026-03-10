package Comprog_MIMS_PRACACT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class SupplierGUI extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private InventoryDAO inventoryDAO;

    private JTextField txtName, txtPrice, txtStock, txtSpecific2, txtSearch;
    private JComboBox<String> cbCategory, cbSpecific1;
    private JCheckBox chkSpecific2, chkShowAsNew;
    private JLabel lblSpecific1, lblSpecific2, lblConnectionStatus;
    private TableRowSorter<DefaultTableModel> sorter;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SupplierGUI frame = new SupplierGUI();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    public SupplierGUI() {
        inventoryDAO = new InventoryDAO();
        setTitle("Supplier View - Inventory Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 920, 600);
        
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

        JLabel lblTitle = new JLabel("Supplier Control Panel");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(20, 15, 300, 25);
        contentPane.add(lblTitle);

        lblConnectionStatus = new JLabel("Status: Checking...");
        lblConnectionStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblConnectionStatus.setBounds(350, 20, 150, 20);
        contentPane.add(lblConnectionStatus);

        JButton btnQuit = new JButton("Quit");
        btnQuit.setBounds(790, 15, 80, 25);
        btnQuit.setFocusPainted(false);
        btnQuit.addActionListener(e -> System.exit(0));
        contentPane.add(btnQuit);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSearch.setBounds(20, 50, 50, 20);
        contentPane.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(70, 50, 200, 22);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        contentPane.add(txtSearch);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 85, 870, 255);
        contentPane.add(scrollPane);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price/Kg", "Stock", "Specifics", "Show New"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        scrollPane.setViewportView(table);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFieldsFromSelection();
        });

        int yOff = 360;
        
        addLabel("Name:", 20, yOff);
        txtName = addTextField(70, yOff, 120);

        addLabel("Price/Kg:", 200, yOff);
        txtPrice = addTextField(260, yOff, 80);

        addLabel("Stock:", 350, yOff);
        txtStock = addTextField(400, yOff, 80);

        addLabel("Category:", 500, yOff);
        cbCategory = new JComboBox<>(new String[]{"Beef", "Pork", "Poultry"});
        cbCategory.setBounds(570, yOff, 100, 22);
        cbCategory.addActionListener(e -> updateSpecificFields());
        contentPane.add(cbCategory);

        chkShowAsNew = new JCheckBox("Tag 'New' (12h)");
        chkShowAsNew.setBounds(700, yOff, 150, 20);
        chkShowAsNew.setSelected(true);
        contentPane.add(chkShowAsNew);

        yOff += 40;

        lblSpecific1 = addLabel("Grade:", 20, yOff);
        cbSpecific1 = new JComboBox<>();
        cbSpecific1.setBounds(100, yOff, 120, 22);
        contentPane.add(cbSpecific1);

        lblSpecific2 = addLabel("Marbling (1-10):", 240, yOff);
        txtSpecific2 = addTextField(350, yOff, 80);
        
        chkSpecific2 = new JCheckBox();
        chkSpecific2.setBounds(350, yOff, 150, 20);
        chkSpecific2.setVisible(false);
        contentPane.add(chkSpecific2);

        yOff += 60;

        JButton btnAdd = new JButton("Add Product");
        btnAdd.setBounds(20, yOff, 120, 35);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> addProduct());
        contentPane.add(btnAdd);

        JButton btnUpdate = new JButton("Update Selected");
        btnUpdate.setBounds(150, yOff, 140, 35);
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(e -> updateProduct());
        contentPane.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBounds(300, yOff, 140, 35);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteProduct());
        contentPane.add(btnDelete);

        JButton btnClear = new JButton("Clear Fields");
        btnClear.setBounds(450, yOff, 120, 35);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> clearFields());
        contentPane.add(btnClear);

        updateSpecificFields();
        toggleTheme(true);
        refreshTable();
    }

    private void filter() {
        String text = txtSearch.getText();
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 100, 20);
        contentPane.add(lbl);
        return lbl;
    }

    private JTextField addTextField(int x, int y, int width) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, width, 22);
        contentPane.add(txt);
        return txt;
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
            } else if (c instanceof JTextField || c instanceof JComboBox || c instanceof JCheckBox) {
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

    private void updateSpecificFields() {
        String cat = (String) cbCategory.getSelectedItem();
        cbSpecific1.removeAllItems();
        txtSpecific2.setVisible(false);
        chkSpecific2.setVisible(false);

        if ("Beef".equals(cat)) {
            lblSpecific1.setText("Beef Grade:");
            cbSpecific1.addItem("Prime"); cbSpecific1.addItem("Choice"); cbSpecific1.addItem("Select"); cbSpecific1.addItem("Wagyu");
            lblSpecific2.setText("Marbling Score:");
            txtSpecific2.setVisible(true);
        } else if ("Pork".equals(cat)) {
            lblSpecific1.setText("Cut Type:");
            cbSpecific1.addItem("Belly"); cbSpecific1.addItem("Chop"); cbSpecific1.addItem("Tenderloin"); cbSpecific1.addItem("Ribs");
            lblSpecific2.setText("Has Bone:");
            chkSpecific2.setText("Yes"); chkSpecific2.setVisible(true);
        } else if ("Poultry".equals(cat)) {
            lblSpecific1.setText("Part Name:");
            cbSpecific1.addItem("Breast"); cbSpecific1.addItem("Thigh"); cbSpecific1.addItem("Wings"); cbSpecific1.addItem("Whole");
            lblSpecific2.setText("Is Organic:");
            chkSpecific2.setText("Yes"); chkSpecific2.setVisible(true);
        }
    }

    private void refreshTable() {
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
            for (MeatProduct p : products) {
                String specifics = "";
                if (p instanceof BeefProduct) specifics = "Grade: " + ((BeefProduct)p).getBeefGrade() + " | Marbling: " + ((BeefProduct)p).getMarblingScore();
                else if (p instanceof PorkProduct) specifics = "Cut: " + ((PorkProduct)p).getCutType() + " | Bone: " + (((PorkProduct)p).isHasBone() ? "Yes" : "No");
                else if (p instanceof PoultryProduct) specifics = "Part: " + ((PoultryProduct)p).getPartName() + " | Organic: " + (((PoultryProduct)p).isOrganic() ? "Yes" : "No");

                tableModel.addRow(new Object[]{p.getProductId(), p.getMeatName(), p.getMeatCategory(), p.getPricePerKg(), p.getWeightStock(), specifics, p.isShowAsNew() ? "Yes" : "No"});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFieldsFromSelection() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        try {
            int id = (int) tableModel.getValueAt(modelRow, 0);
            List<MeatProduct> products = inventoryDAO.getAllProducts();
            MeatProduct selected = products.stream().filter(p -> p.getProductId() == id).findFirst().orElse(null);
            
            if (selected != null) {
                txtName.setText(selected.getMeatName());
                txtPrice.setText(String.valueOf(selected.getPricePerKg()));
                txtStock.setText(String.valueOf(selected.getWeightStock()));
                cbCategory.setSelectedItem(selected.getMeatCategory());
                chkShowAsNew.setSelected(selected.isShowAsNew());
                
                if (selected instanceof BeefProduct) {
                    cbSpecific1.setSelectedItem(((BeefProduct) selected).getBeefGrade());
                    txtSpecific2.setText(String.valueOf(((BeefProduct) selected).getMarblingScore()));
                } else if (selected instanceof PorkProduct) {
                    cbSpecific1.setSelectedItem(((PorkProduct) selected).getCutType());
                    chkSpecific2.setSelected(((PorkProduct) selected).isHasBone());
                } else if (selected instanceof PoultryProduct) {
                    cbSpecific1.setSelectedItem(((PoultryProduct) selected).getPartName());
                    chkSpecific2.setSelected(((PoultryProduct) selected).isOrganic());
                }
            }
        } catch (Exception e) {}
    }

    private MeatProduct createProductFromFields(int id) {
        try {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Product name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            double price = Double.parseDouble(txtPrice.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Price cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            double stock = Double.parseDouble(txtStock.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "Stock cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            String cat = (String) cbCategory.getSelectedItem();
            String spec1 = (String) cbSpecific1.getSelectedItem();

            MeatProduct mp = null;
            if ("Beef".equals(cat)) {
                int marbling = Integer.parseInt(txtSpecific2.getText().trim());
                if (marbling < 1 || marbling > 10) {
                    JOptionPane.showMessageDialog(this, "Marbling score must be between 1 and 10.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return null;
                }
                mp = new BeefProduct(id, name, price, stock, spec1, marbling);
            } else if ("Pork".equals(cat)) {
                mp = new PorkProduct(id, name, price, stock, spec1, chkSpecific2.isSelected());
            } else if ("Poultry".equals(cat)) {
                mp = new PoultryProduct(id, name, price, stock, spec1, chkSpecific2.isSelected());
            }
            if (mp != null) mp.setShowAsNew(chkShowAsNew.isSelected());
            return mp;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format in Price, Stock, or Marbling.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void addProduct() {
        MeatProduct product = createProductFromFields(0);
        if (product != null) {
            try {
                inventoryDAO.addProduct(product);
                refreshTable();
                clearFields();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void updateProduct() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        MeatProduct product = createProductFromFields((int) tableModel.getValueAt(modelRow, 0));
        if (product != null) {
            try {
                inventoryDAO.updateProduct(product);
                refreshTable();
            } catch (Exception e) {}
        }
    }

    private void deleteProduct() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = table.convertRowIndexToModel(viewRow);
        try {
            inventoryDAO.deleteProduct((int) tableModel.getValueAt(modelRow, 0));
            refreshTable();
            clearFields();
        } catch (Exception e) {}
    }

    private void clearFields() {
        txtName.setText(""); txtPrice.setText(""); txtStock.setText(""); txtSpecific2.setText(""); txtSearch.setText("");
        chkSpecific2.setSelected(false); chkShowAsNew.setSelected(true); table.clearSelection();
    }
}