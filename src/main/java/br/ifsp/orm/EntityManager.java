package br.ifsp.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class EntityManager<T> {
    private final T entity;

    EntityManager(Class<T> entityClass) {
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new MyORMException(e.getMessage());
        }
    }

    public EntityManager(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public Field getEntityId() {
        final Class<?> entityClass = entity.getClass();

        final List<Field> annotatedFields = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(EntityId.class))
                .toList();

        if (annotatedFields.isEmpty())
            throw new MyORMException(entityClass.getName() + " has no @EntityId annotation");

        if (annotatedFields.size() > 1)
            throw new MyORMException(entityClass.getName() + " has more than one @EntityId annotation");

        return annotatedFields.getFirst();
    }

    public Object getFieldValue(String fieldName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            final Method method = entity.getClass().getMethod(methodName);
            return method.invoke(entity);
        } catch (Exception e) {
            throw new MyORMException(e.getMessage());
        }
    }

    void setValue(Field field, Object fieldValue) {
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

    public static int compareFieldWithEntityId(Field field) {
        return field.isAnnotationPresent(EntityId.class) ? 1 : 0;
    }
}
