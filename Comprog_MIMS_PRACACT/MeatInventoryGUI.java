import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class MeatInventoryGUI extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private InventoryDAO inventoryDAO;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MeatInventoryGUI frame = new MeatInventoryGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MeatInventoryGUI() {
        inventoryDAO = new InventoryDAO();
        
        setTitle("Meat Inventory Management System (WindowBuilder)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 740, 280);
        contentPane.add(scrollPane);
        
        table = new JTable();
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "ID", "Name", "Category", "Price/Kg", "Stock (kg)", "Specifics"
            }
        );
        table.setModel(tableModel);
        scrollPane.setViewportView(table);
        
        JButton btnRefresh = new JButton("Refresh Database");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTableData();
            }
        });
        btnRefresh.setBounds(20, 350, 150, 40);
        contentPane.add(btnRefresh);
        
        JButton btnAddDemo = new JButton("Add Demo Beef");
        btnAddDemo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    inventoryDAO.addBeefProduct(new BeefProduct(0, "Wagyu Ribeye", 150.0, 10.5, "A5", 9));
                    refreshTableData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPane, "Database Error: Make sure XAMPP MySQL is running.\n" + ex.getMessage());
                }
            }
        });
        btnAddDemo.setBounds(190, 350, 150, 40);
        contentPane.add(btnAddDemo);
        
        JLabel lblTitle = new JLabel("Meat Product Inventory System");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(20, 15, 300, 25);
        contentPane.add(lblTitle);
        
        
        refreshTableData();
    }
    
    private void refreshTableData() {
        try {
            tableModel.setRowCount(0); 
            List<MeatProduct> products = inventoryDAO.getAllProducts();
            for (MeatProduct p : products) {
                String specifics = "";
                if (p instanceof BeefProduct) specifics = "Grade: " + ((BeefProduct)p).getBeefGrade();
                else if (p instanceof PorkProduct) specifics = "Cut: " + ((PorkProduct)p).getCutType();
                else if (p instanceof PoultryProduct) specifics = "Part: " + ((PoultryProduct)p).getPartName();

                Object[] row = {p.getProductId(), p.getMeatName(), p.getMeatCategory(), p.getPricePerKg(), p.getWeightStock(), specifics};
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            System.out.println("No database connected yet. Please set up XAMPP database 'meat_inventory'.");
        }
    }
}
