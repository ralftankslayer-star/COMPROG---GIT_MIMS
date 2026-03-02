import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProductDAO {

    public static void addProduct(
        String name,
        double price,
        double weight,
        String category) {

        String sql =
        "INSERT INTO products(meat_name, price_per_kg, weight_stock, meat_category) VALUES (?,?,?,?)";

        try {
            Connection conn = connector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setDouble(3, weight);
            stmt.setString(4, category);

            stmt.executeUpdate();

            System.out.println("Product added!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}