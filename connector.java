import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connector {

    private static final String URL =
        "jdbc:mysql://localhost:3306/groupproj";

    private static final String USER = "root";
    private static final String PASSWORD = "Angelo07!";

    public static Connection connect() {

        try {
            Connection conn =
                DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Connected to database!");
            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}