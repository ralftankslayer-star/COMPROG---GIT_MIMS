import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public void addBeefProduct(BeefProduct beef) throws SQLException {
       
        String sql = "INSERT INTO products (meat_name, price_per_kg, weight_stock, meat_category, beef_grade, marbling_score) VALUES (?, ?, ?, 'Beef', ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, beef.getMeatName());
            stmt.setDouble(2, beef.getPricePerKg());
            stmt.setDouble(3, beef.getWeightStock());
            stmt.setString(4, beef.getBeefGrade());
            stmt.setInt(5, beef.getMarblingScore());
            stmt.executeUpdate();
        }
    }

    public List<MeatProduct> getAllProducts() throws SQLException {
        List<MeatProduct> products = new ArrayList<>();
        
        String sql = "SELECT * FROM products";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                
                int id = rs.getInt("product_id");
                String name = rs.getString("meat_name");
                double price = rs.getDouble("price_per_kg");
                double stock = rs.getDouble("weight_stock");
                String category = rs.getString("meat_category");

                if ("Beef".equals(category)) {
                    products.add(new BeefProduct(id, name, price, stock, rs.getString("beef_grade"), rs.getInt("marbling_score")));
                } else if ("Pork".equals(category)) {
                    products.add(new PorkProduct(id, name, price, stock, rs.getString("cut_type"), rs.getBoolean("has_bone")));
                } else if ("Poultry".equals(category)) {
                    products.add(new PoultryProduct(id, name, price, stock, rs.getString("part_name"), rs.getBoolean("is_organic")));
                }
            }
        }
        return products;
    }
}