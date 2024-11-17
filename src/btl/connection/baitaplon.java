package btl.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class baitaplon {

    public static Connection getConnection() throws SQLException {
        
        String url = "jdbc:mysql://localhost:3306/btllhdv"; 
        String username = "root";  
        String password = "Lethang123@"; 

        // Kết nối đến cơ sở dữ liệu MySQL
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("Kết nối thành công!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
