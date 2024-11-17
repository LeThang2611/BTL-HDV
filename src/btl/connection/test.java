package btl.connection;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class test {

    private static func reservationFunc;

    public static void main(String[] args) {
        // Khởi tạo đối tượng func để sử dụng các phương thức
        reservationFunc = new func();
        Scanner scanner = new Scanner(System.in);

        // Menu cho phép người dùng chọn hành động
        while (true) {
            System.out.println("==== Menu Đặt Bàn ====");
            System.out.println("1. Đặt bàn");
            System.out.println("2. Thoát");
            System.out.print("Chọn lựa chọn của bạn (1 hoặc 2): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Đọc ký tự xuống dòng còn sót lại

            if (choice == 1) {
                // Nhập thông tin khách hàng
                System.out.print("Nhập tên khách hàng: ");
                String fullName = scanner.nextLine();

                System.out.print("Nhập email: ");
                String email = scanner.nextLine();

                System.out.print("Nhập số điện thoại: ");
                String phoneNumber = scanner.nextLine();

                System.out.print("Nhập số người: ");
                int numberOfPeople = scanner.nextInt();
                scanner.nextLine();  // Đọc ký tự xuống dòng còn sót lại

                System.out.print("Nhập thời gian đặt bàn (yyyy-MM-dd HH:mm): ");
                String reservationTimeStr = scanner.nextLine();
                LocalDateTime reservationTime = LocalDateTime.parse(reservationTimeStr.replace(" ", "T"));

                // Đặt bàn
                try {
                    boolean result = reservationFunc.addReservation(fullName, email, phoneNumber, numberOfPeople, reservationTime);
                    if (result) {
                        System.out.println("Đặt bàn thành công cho khách hàng: " + fullName);
                    } else {
                        System.out.println("Đặt bàn thất bại cho khách hàng: " + fullName);
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage());
                }

            } else if (choice == 2) {
                System.out.println("Cảm ơn bạn đã sử dụng dịch vụ.");
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }

        scanner.close();  // Đóng scanner sau khi sử dụng
    }
}