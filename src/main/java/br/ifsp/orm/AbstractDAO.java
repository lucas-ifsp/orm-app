package br.ifsp.orm;

import br.ifsp.orm.mappers.TypeMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO<T, K> implements Registrable<T>, Retrievable<T, K> {
    @SuppressWarnings("unchecked")
    private Class<T> getParameterizedEntityType() {
        // Any concrete class of this class will have AbstractDAO generic super type
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0]; // Get parameterized type of T in [T, K]
    }

    @Override
    public void save(T t) throws SQLException {
        final Class<?> entity = t.getClass();
        final String sql = SQLBuilder.save(entity);
        final EntityManager<T> entityManager = new EntityManager<>(t);

        try (final var stmt = ConnectionFactory.getPreparedStatement(sql)) {
            final Field[] declaredFields = entity.getDeclaredFields();

            for (int i = 0; i < declaredFields.length; i++) {
                final Field field = entity.getDeclaredFields()[i];
                final Object value = entityManager.getFieldValue(field.getName());
                final int sqlType = TypeMapper.toSql(value);
                stmt.setObject(i + 1, value, sqlType);
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<T> findOne(K k) throws SQLException {
        final Class<T> entity = getParameterizedEntityType();
        final EntityManager<T> entityManager = new EntityManager<>(entity);
        final Field idField = entityManager.getEntityId();
        final String sql = SQLBuilder.findOne(entity, idField.getName());

        try (final var stmt = ConnectionFactory.getPreparedStatement(sql)) {

            stmt.setObject(1, k, TypeMapper.toSql(k));
            final ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                for (final Field field : entity.getDeclaredFields()) {
                    final Object value = rs.getObject(field.getName());
                    entityManager.setValue(field, value);
                }

                return Optional.of(entityManager.getEntity());
            }
        }
        return Optional.empty();
    }

    public List<T> findAll() throws SQLException {
        final List<T> entities = new ArrayList<>();
        final Class<T> entity = getParameterizedEntityType();
        final EntityManager<T> entityManager = new EntityManager<>(entity);
        final String sql = SQLBuilder.findAll(entity);

        try (final var stmt = ConnectionFactory.getPreparedStatement(sql)) {
            final ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                for (final Field field : entity.getDeclaredFields()) {
                    final Object value = rs.getObject(field.getName());
                    entityManager.setValue(field, value);
                }

                entities.add(entityManager.getEntity());
            }
        }

        return entities;
    }

    public void deleteByKey(K key) throws SQLException {
        final Class<T> entity = getParameterizedEntityType();
        final EntityManager<T> entityManager = new EntityManager<>(entity);
        final Field idField = entityManager.getEntityId();
        final String sql = SQLBuilder.deleteByKey(entity, idField.getName());

        try (final var stmt = ConnectionFactory.getPreparedStatement(sql)) {
            stmt.setObject(1, key, TypeMapper.toSql(key));
            stmt.executeUpdate();
        }
    }

    public void update(T t) throws SQLException {
        final EntityManager<T> entityManager = new EntityManager<>(t);
        final String sql = SQLBuilder.update(t.getClass(), entityManager.getEntityId().getName());
        try (final var stmt = ConnectionFactory.getPreparedStatement(sql)) {
            final Field[] declaredFields = t.getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                final Field field = declaredFields[i];
                final Object value = entityManager.getFieldValue(field.getName());
                final int sqlType = TypeMapper.toSql(field.getType());
                stmt.setObject(i + 1, value, sqlType);
            }
            stmt.executeUpdate();
        }
    }
}
