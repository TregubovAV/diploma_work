package db;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtils {

    private static final String URL = "jdbc:mysql://localhost:3306/app";
    private static final String USER = "app";
    private static final String PASSWORD = "pass";

    private DbUtils() {
    }

    @SneakyThrows
    public static void clearTables() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            runner.update(conn, "DELETE FROM credit_request_entity;");
            runner.update(conn, "DELETE FROM payment_entity;");
            runner.update(conn, "DELETE FROM order_entity;");
        }
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return runner.query(conn, "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;", new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return runner.query(conn, "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;", new ScalarHandler<>());
        }
    }
}