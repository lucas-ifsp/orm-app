package br.ifsp.orm.mappers;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class PostgresClassMapper {
    private static final Map<Class<?>, String> CLASS_TO_POSTGRES_TYPES = new HashMap<>();

    private PostgresClassMapper() {}

    static {
        CLASS_TO_POSTGRES_TYPES.put(String.class, "VARCHAR(255)");
        CLASS_TO_POSTGRES_TYPES.put(Integer.class, "INTEGER");
        CLASS_TO_POSTGRES_TYPES.put(int.class, "INTEGER");
        CLASS_TO_POSTGRES_TYPES.put(Long.class, "BIGINT");
        CLASS_TO_POSTGRES_TYPES.put(long.class, "BIGINT");
        CLASS_TO_POSTGRES_TYPES.put(Double.class, "DOUBLE PRECISION");
        CLASS_TO_POSTGRES_TYPES.put(double.class, "DOUBLE PRECISION");
        CLASS_TO_POSTGRES_TYPES.put(Float.class, "FLOAT");
        CLASS_TO_POSTGRES_TYPES.put(float.class, "FLOAT");
        CLASS_TO_POSTGRES_TYPES.put(Boolean.class, "BOOLEAN");
        CLASS_TO_POSTGRES_TYPES.put(boolean.class, "BOOLEAN");
        CLASS_TO_POSTGRES_TYPES.put(Date.class, "DATE");
    }

    public static Map<Class<?>, String> getClassToPostgresTypes() {
        return new HashMap<>(CLASS_TO_POSTGRES_TYPES);
    }
}
