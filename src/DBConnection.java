import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Đúng chuẩn SQL Server
    private static final String URL = "jdbc:sqlserver://NGUYENHUUANH\\SQLEXPRESS;databaseName=quanlysinhvienjava;encrypt=false";
    private static final String USER = "nguyenhuuanhdev"; // tên user trong SQL Server
    private static final String PASSWORD = "170206";       // mật khẩu của bạn

    public static Connection getConnection() {
        try {
            // ✅ Đổi driver cho SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
