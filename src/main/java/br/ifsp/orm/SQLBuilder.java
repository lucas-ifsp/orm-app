package br.ifsp.orm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SQLBuilder {
    private SQLBuilder() {}

    public static String save(Class<?> entity) {
        final String tableName = entity.getSimpleName().toUpperCase();

        final String fields =
                Arrays.stream(entity.getDeclaredFields())
                        .map(Field::getName)
                        .collect(Collectors.joining(", "));

        final String values = Arrays.stream(fields.split(", "))
                .map(_ -> "?").collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fields, values);
    }

    public static String findOne(Class<?> entity, String idFieldName) {
        final String tableName = entity.getSimpleName().toUpperCase();
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idFieldName);
    }

    public static String findAll(Class<?> entity) {
        final String tableName = entity.getSimpleName().toUpperCase();
        return String.format("SELECT * FROM %s", tableName);
    }

    public static String deleteByKey(Class<?> entity, String idFieldName) {
        final String tableName = entity.getSimpleName().toUpperCase();
        return String.format("DELETE FROM %s WHERE %s = ?", tableName, idFieldName);
    }

    public static String update(Class<?> entity, String idFieldName) {
        final String tableName = entity.getSimpleName().toUpperCase();
        final String fields = Arrays.stream(entity.getDeclaredFields())
                .filter(field -> !field.getName().equals(idFieldName))
                .map(field -> String.format("%s = ?", field.getName()))
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?", tableName, fields, idFieldName);
    }
}
