package br.ifsp.orm.mappers;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class SQLiteClassMapper {
    private static final Map<Class<?>, String> CLASS_TO_SQLITE_TYPES = new HashMap<>();

    private SQLiteClassMapper() {}

    static {
        CLASS_TO_SQLITE_TYPES.put(String.class, "TEXT");
        CLASS_TO_SQLITE_TYPES.put(Integer.class, "INTEGER");
        CLASS_TO_SQLITE_TYPES.put(int.class, "INTEGER");
        CLASS_TO_SQLITE_TYPES.put(Long.class, "REAL");
        CLASS_TO_SQLITE_TYPES.put(long.class, "REAL");
        CLASS_TO_SQLITE_TYPES.put(Double.class, "REAL");
        CLASS_TO_SQLITE_TYPES.put(double.class, "REAL");
        CLASS_TO_SQLITE_TYPES.put(Float.class, "REAL");
        CLASS_TO_SQLITE_TYPES.put(float.class, "REAL");
        CLASS_TO_SQLITE_TYPES.put(Boolean.class, "INTEGER");
        CLASS_TO_SQLITE_TYPES.put(boolean.class, "INTEGER");
        CLASS_TO_SQLITE_TYPES.put(Date.class, "TEXT");
    }

    public static Map<Class<?>, String> getClassToSqliteTypes() {
        return new HashMap<>(CLASS_TO_SQLITE_TYPES);
    }
}
