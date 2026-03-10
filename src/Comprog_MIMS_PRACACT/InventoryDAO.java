package Comprog_MIMS_PRACACT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public boolean testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public int getTotalProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public void addProduct(MeatProduct product) throws SQLException, InvalidDataException {
        if (getTotalProductCount() >= 100) {
            throw new InvalidDataException("Database threshold reached. Maximum 100 items allowed.");
        }

        String sql = "INSERT INTO products (meat_name, price_per_kg, weight_stock, meat_category, beef_grade, marbling_score, cut_type, has_bone, part_name, is_organic, show_as_new) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getMeatName());
            stmt.setDouble(2, product.getPricePerKg());
            stmt.setDouble(3, product.getWeightStock());
            stmt.setString(4, product.getMeatCategory());

            stmt.setNull(5, Types.VARCHAR);
            stmt.setNull(6, Types.INTEGER);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.BOOLEAN);
            stmt.setNull(9, Types.VARCHAR);
            stmt.setNull(10, Types.BOOLEAN);

            if (product instanceof BeefProduct) {
                stmt.setString(5, ((BeefProduct) product).getBeefGrade());
                stmt.setInt(6, ((BeefProduct) product).getMarblingScore());
            } else if (product instanceof PorkProduct) {
                stmt.setString(7, ((PorkProduct) product).getCutType());
                stmt.setBoolean(8, ((PorkProduct) product).isHasBone());
            } else if (product instanceof PoultryProduct) {
                stmt.setString(9, ((PoultryProduct) product).getPartName());
                stmt.setBoolean(10, ((PoultryProduct) product).isOrganic());
            }
            stmt.setBoolean(11, product.isShowAsNew());
            stmt.executeUpdate();
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }

    public void updateProduct(MeatProduct product) throws SQLException {
        String sql = "UPDATE products SET meat_name=?, price_per_kg=?, weight_stock=?, meat_category=?, beef_grade=?, marbling_score=?, cut_type=?, has_bone=?, part_name=?, is_organic=?, show_as_new=? WHERE product_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getMeatName());
            stmt.setDouble(2, product.getPricePerKg());
            stmt.setDouble(3, product.getWeightStock());
            stmt.setString(4, product.getMeatCategory());

            stmt.setNull(5, Types.VARCHAR);
            stmt.setNull(6, Types.INTEGER);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.BOOLEAN);
            stmt.setNull(9, Types.VARCHAR);
            stmt.setNull(10, Types.BOOLEAN);

            if (product instanceof BeefProduct) {
                stmt.setString(5, ((BeefProduct) product).getBeefGrade());
                stmt.setInt(6, ((BeefProduct) product).getMarblingScore());
            } else if (product instanceof PorkProduct) {
                stmt.setString(7, ((PorkProduct) product).getCutType());
                stmt.setBoolean(8, ((PorkProduct) product).isHasBone());
            } else if (product instanceof PoultryProduct) {
                stmt.setString(9, ((PoultryProduct) product).getPartName());
                stmt.setBoolean(10, ((PoultryProduct) product).isOrganic());
            }
            
            stmt.setBoolean(11, product.isShowAsNew());
            stmt.setInt(12, product.getProductId());
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

                MeatProduct p = null;
                if ("Beef".equals(category)) {
                    p = new BeefProduct(id, name, price, stock, rs.getString("beef_grade"), rs.getInt("marbling_score"));
                } else if ("Pork".equals(category)) {
                    p = new PorkProduct(id, name, price, stock, rs.getString("cut_type"), rs.getBoolean("has_bone"));
                } else if ("Poultry".equals(category)) {
                    p = new PoultryProduct(id, name, price, stock, rs.getString("part_name"), rs.getBoolean("is_organic"));
                }

                if (p != null) {
                    p.setShowAsNew(rs.getBoolean("show_as_new"));
                    p.setAddedAt(rs.getTimestamp("added_at"));
                    p.setUpdatedAt(rs.getTimestamp("updated_at"));
                    products.add(p);
                }
            }
        }
        return products;
    }
}