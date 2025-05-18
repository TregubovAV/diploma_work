package db;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtils {

    private DbUtils() {}

    // Получаем параметры из systemProperty, с дефолтами
    private static final String DB_URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/app");
    private static final String DB_USER = System.getProperty("db.user", "app");
    private static final String DB_PASSWORD = System.getProperty("db.password", "pass");

    @SneakyThrows
    public static void clearTables() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            runner.update(conn, "DELETE FROM credit_request_entity;");
            runner.update(conn, "DELETE FROM payment_entity;");
            runner.update(conn, "DELETE FROM order_entity;");
        }
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return runner.query(conn, "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;", new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return runner.query(conn, "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;", new ScalarHandler<>());
        }
    }
}