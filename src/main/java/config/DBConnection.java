package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://34.176.161.147:3306/sistema_pedidos",
                "lucio",
                "Lucio123!"
        );
    }
}