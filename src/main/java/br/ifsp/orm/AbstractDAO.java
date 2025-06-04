package br.ifsp.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractDAO<T extends Entity, K> implements Registrable<T>, Retrievable<T, K> {

    @Override
    public void save(T t) {
        Class<T> entity = (Class<T>) t.getClass();
        final String sql = buildSaveSql(entity);

        try (final Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
             final PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < entity.getDeclaredFields().length; i++) {
                final Field field = entity.getDeclaredFields()[i];
                final String value = getFieldValue(t, field.getName());
                final int sqlType = toSqlType(field.getType());
                stmt.setObject(i + 1, value, sqlType);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String buildSaveSql(Class<T> entity) {
        final String tableName = entity.getSimpleName().toUpperCase();

        final List<String> fieldNames = Arrays.stream(entity.getDeclaredFields()).map(Field::getName).toList();
        final String fields = String.join(", ", fieldNames);
        final String values = fieldNames.stream().map(_ -> "?").collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fields, values);
    }

    private String getFieldValue(T entity, String fieldName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            final Method method = entity.getClass().getMethod(methodName);
            return method.invoke(entity).toString();
        } catch (Exception e) {
            throw new MyORMException(e.getMessage());
        }
    }

    public int toSqlType(Object obj) {
        return switch (obj) {
            case null -> Types.NULL;
            case String _ -> Types.VARCHAR;
            case Integer _ -> Types.INTEGER;
            case Long _ -> Types.BIGINT;
            case Double _ -> Types.DOUBLE;
            case Float _ -> Types.REAL;
            case Boolean _ -> Types.BOOLEAN;
            case Date _ -> Types.DATE;
            default -> Types.JAVA_OBJECT;
        };
    }

    @Override
    public Optional<T> findOne(K k) {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            Class<T> entity = (Class<T>) parameterizedType.getActualTypeArguments()[0];// Gets the type of T
            T instance = entity.getDeclaredConstructor().newInstance();

            String sql = buildFindOneSql(entity, instance.idFieldName());

            try (var conn = DriverManager.getConnection("jdbc:sqlite:database.db");
                 var stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, k, toSqlType(k));
                final ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    for (int i = 0; i < entity.getDeclaredFields().length; i++) {
                        final Field field = entity.getDeclaredFields()[i];
                        final Object value = rs.getObject(field.getName());
                        setValue(instance, field, value);
                    }
                    return Optional.of(instance);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new MyORMException(e.getMessage());
        }
        return Optional.empty();
    }

    private String buildFindOneSql(Class<T> entity, String idFieldName) {
        final String tableName = entity.getSimpleName().toUpperCase();
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idFieldName);
    }

    private void setValue(T entity, Field field, Object fieldValue) {
        try {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            final Method method = entity.getClass().getMethod(methodName, fieldType);
            method.invoke(entity, fieldValue);
        } catch (Exception e) {
            throw new MyORMException(e.getMessage());
        }
    }
}
