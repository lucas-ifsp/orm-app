package br.ifsp.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseBuilder {
    private final Connection connection;

    public DatabaseBuilder() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");;
    }

    public static void main(String[] args) throws SQLException {
        DatabaseBuilder builder = new DatabaseBuilder();
        builder.createCarTable();
        builder.createSushiTable();
    }

    private void createCarTable() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS CAR (
                        plate TEXT PRIMARY KEY,
                        brand TEXT,
                        year INTEGER
                    )
                """;

        var stmt = connection.createStatement();
        stmt.execute(sql);
    }

    private void createSushiTable() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS SUSHI (
            name TEXT PRIMARY KEY,
            price REAL,
            quantity INTEGER
        );
        """;

        var stmt = connection.createStatement();
        stmt.execute(sql);
    }
}
