package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getLocal() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sistema_pedidos",
                "root",
                "Lucio123!"
        );
    }

    public static Connection getRemote() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://34.176.161.147:3306/sistema_pedidos",
                "lucio",
                "Lucio123!"
        );
    }
}