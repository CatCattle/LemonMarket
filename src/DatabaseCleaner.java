import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCleaner {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/lemon_market";
        String user = "root";
        String password = "15818530035";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // 假设我们有一个表的名称列表
            String[] tables = {"buyers", "cars_for_sale", "dealers", "inventory", "sellers"};

            for (String table : tables) {
                // 使用 DELETE 语句
                String deleteSql = "DELETE FROM " + table;
                stmt.executeUpdate(deleteSql);

//                // 或者使用 TRUNCATE 语句
//                String truncateSql = "TRUNCATE TABLE " + table;
//                stmt.executeUpdate(truncateSql);
            }

            System.out.println("所有表的数据已清空");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}