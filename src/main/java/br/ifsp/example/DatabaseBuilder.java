package br.ifsp.example;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseBuilder {
    public static void main(String[] args) throws SQLException {
        DatabaseBuilder builder = new DatabaseBuilder();
        builder.createTable();
    }

    private void createTable() throws SQLException {
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS CAR (
                        plate TEXT PRIMARY KEY,
                        brand TEXT,
                        year INTEGER
                    )
                """;

        var conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        var stmt = conn.createStatement();
        stmt.execute(createTableSQL);

    }
}
