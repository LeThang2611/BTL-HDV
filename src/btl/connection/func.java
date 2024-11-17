package btl.connection;
import java.sql.*;
import java.time.LocalDateTime;

import com.mysql.cj.xdevapi.Table;

public class func {

    private static final int MAX_TABLES = 20;
    private static final int MAX_PEOPLE_PER_TABLE = 4;

    // Kiểm tra xem có bàn trống không
    public boolean isTableAvailable() throws SQLException {
        try (Connection conn = baitaplon.getConnection()) {
            String sql = "SELECT COUNT(*) FROM reservations";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int reservedCount = rs.getInt(1);
                    return reservedCount < MAX_TABLES;  // Chỉ cho phép tối đa 20 bàn
                }
            }
        }
        return false;
    }

    // Kiểm tra số người không quá 4
    public boolean isValidNumberOfPeople(int numberOfPeople) {
        return numberOfPeople <= MAX_PEOPLE_PER_TABLE;
    }

    // Thêm khách hàng
    public int addCustomer(String fullName, String email, String phoneNumber) throws SQLException {
        try (Connection conn = baitaplon.getConnection()) {
            String sql = "INSERT INTO customers (full_name, email, phone_number) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, fullName);
                stmt.setString(2, email);
                stmt.setString(3, phoneNumber);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // Thêm đặt bàn
    public boolean addReservation(String fullName, String email, String phoneNumber, int numberOfPeople, LocalDateTime reservationTime) throws SQLException {
        // Kiểm tra số người
        if (!isValidNumberOfPeople(numberOfPeople)) {
            System.out.println("Số người vượt quá giới hạn (tối đa 4 người mỗi bàn).");
            return false;
        }

        // Kiểm tra có bàn trống không
        if (!isTableAvailable()) {
            System.out.println("Không còn bàn trống.");
            return false;
        }

        // Lấy bàn trống đầu tiên
        table availableTable = getAvailableTable();
        if (availableTable == null) {
            System.out.println("Không có bàn trống.");
            return false;
        }

        // Thêm khách hàng vào CSDL
        int customerId = addCustomer(fullName, email, phoneNumber);
        if (customerId == -1) {
            System.out.println("Không thể thêm khách hàng vào cơ sở dữ liệu.");
            return false;
        }

        // Thêm đặt bàn vào CSDL
        try (Connection conn = baitaplon.getConnection()) {
            String sql = "INSERT INTO reservations (customer_id, table_id, number_of_people, reservation_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, availableTable.getTableId());
                stmt.setInt(3, numberOfPeople);
                stmt.setTimestamp(4, Timestamp.valueOf(reservationTime));
                stmt.executeUpdate();
            }

            // Cập nhật trạng thái bàn đã đặt
            updateTableStatus(availableTable.getTableId(), true);
        }

        System.out.println("Đặt bàn thành công cho khách hàng: " + fullName);
        return true;
    }

    // Lấy bàn trống đầu tiên
    private table getAvailableTable() throws SQLException {
        try (Connection conn = baitaplon.getConnection()) {
            String sql = "SELECT * FROM tables WHERE is_booked = false LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    table table = new table();
                    table.setTableId(rs.getInt("table_id"));
                    table.setTableNumber(rs.getInt("table_number"));
                    table.setBooked(rs.getBoolean("is_booked"));  
                    return table;
                }
            }
        }
        return null;
    }

    // Cập nhật trạng thái bàn đã đặt
    private void updateTableStatus(int tableId, boolean isBooked) throws SQLException {
        try (Connection conn = baitaplon.getConnection()) {
            String sql = "UPDATE tables SET is_booked = ? WHERE table_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setBoolean(1, isBooked); 
                stmt.setInt(2, tableId);      
                stmt.executeUpdate();       
            }
        }
    }
}