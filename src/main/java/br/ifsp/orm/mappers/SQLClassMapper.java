package br.ifsp.orm.mappers;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class SQLClassMapper {
    private static final Map<Class<?>, Integer> CLASS_TO_SQL_TYPES = new HashMap<>();

    private SQLClassMapper() {}

    static {
        CLASS_TO_SQL_TYPES.put(String.class, Types.VARCHAR);
        CLASS_TO_SQL_TYPES.put(Integer.class, Types.INTEGER);
        CLASS_TO_SQL_TYPES.put(int.class, Types.INTEGER);
        CLASS_TO_SQL_TYPES.put(Long.class, Types.BIGINT);
        CLASS_TO_SQL_TYPES.put(long.class, Types.BIGINT);
        CLASS_TO_SQL_TYPES.put(Double.class, Types.DOUBLE);
        CLASS_TO_SQL_TYPES.put(double.class, Types.DOUBLE);
        CLASS_TO_SQL_TYPES.put(Float.class, Types.REAL);
        CLASS_TO_SQL_TYPES.put(float.class, Types.REAL);
        CLASS_TO_SQL_TYPES.put(Boolean.class, Types.BOOLEAN);
        CLASS_TO_SQL_TYPES.put(boolean.class, Types.BOOLEAN);
        CLASS_TO_SQL_TYPES.put(java.sql.Date.class, Types.DATE);
        CLASS_TO_SQL_TYPES.put(java.sql.Time.class, Types.TIME);
        CLASS_TO_SQL_TYPES.put(java.sql.Timestamp.class, Types.TIMESTAMP);
        CLASS_TO_SQL_TYPES.put(java.util.Date.class, Types.TIMESTAMP);
        CLASS_TO_SQL_TYPES.put(BigDecimal.class, Types.DECIMAL);
        CLASS_TO_SQL_TYPES.put(byte[].class, Types.VARBINARY);
    }

    public static Map<Class<?>, Integer> getClassToSqlTypes() {
        return new HashMap<>(CLASS_TO_SQL_TYPES);
    }
}
