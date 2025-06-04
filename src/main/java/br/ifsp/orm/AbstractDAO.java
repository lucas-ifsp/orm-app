package br.ifsp.orm;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractDAO<T, K> implements Registrable<T>, Retrievable<T, K> {

    @SuppressWarnings("unchecked")
    private Class<T> getParameterizedEntityType() {
        // Any concrete class of this class will have AbstractDAO generic super type
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0]; // Get parameterized type of T in [T, K]
    }

    @Override
    public void save(T t) throws SQLException {
        Class<?> entity = t.getClass();
        final String sql = buildSaveSql(entity);

        try (final Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
             final PreparedStatement stmt = conn.prepareStatement(sql)) {

            EntityManager<T> entityManager = new EntityManager<>(t);

            for (int i = 0; i < entity.getDeclaredFields().length; i++) {
                final Field field = entity.getDeclaredFields()[i];
                final String value = entityManager.getFieldValue(field.getName());
                final int sqlType = TypeMapper.toSql(field.getType());
                stmt.setObject(i + 1, value, sqlType);
            }
            stmt.executeUpdate();
        }
    }

    private String buildSaveSql(Class<?> entity) {
        final String tableName = entity.getSimpleName().toUpperCase();

        final List<String> fieldNames = Arrays.stream(entity.getDeclaredFields()).map(Field::getName).toList();
        final String fields = String.join(", ", fieldNames);
        final String values = fieldNames.stream().map(_ -> "?").collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fields, values);
    }

    @Override
    public Optional<T> findOne(K k) throws SQLException {
        Class<T> entity = getParameterizedEntityType();
        EntityManager<T> entityManager = new EntityManager<>(entity);

        Field idField = entityManager.getEntityId();
        String sql = buildFindOneSql(entity, idField.getName());

        try (var conn = DriverManager.getConnection("jdbc:sqlite:database.db");
             var stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, k, TypeMapper.toSql(k));
            final ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                for (int i = 0; i < entity.getDeclaredFields().length; i++) {
                    final Field field = entity.getDeclaredFields()[i];
                    final Object value = rs.getObject(field.getName());
                    entityManager.setValue(field, value);
                }
                return Optional.of(entityManager.getEntity());
            }
        }
        return Optional.empty();
    }

    private String buildFindOneSql(Class<T> entity, String idFieldName) {
        final String tableName = entity.getSimpleName().toUpperCase();
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idFieldName);
    }
}
