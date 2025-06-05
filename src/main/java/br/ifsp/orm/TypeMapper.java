package br.ifsp.orm;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

    private static final Map<Class<?>, Integer> CLASS_TO_SQL_TYPE_MAP = new HashMap<>();

    static {
        CLASS_TO_SQL_TYPE_MAP.put(String.class, Types.VARCHAR);
        CLASS_TO_SQL_TYPE_MAP.put(Integer.class, Types.INTEGER);
        CLASS_TO_SQL_TYPE_MAP.put(int.class, Types.INTEGER);
        CLASS_TO_SQL_TYPE_MAP.put(Long.class, Types.BIGINT);
        CLASS_TO_SQL_TYPE_MAP.put(long.class, Types.BIGINT);
        CLASS_TO_SQL_TYPE_MAP.put(Double.class, Types.DOUBLE);
        CLASS_TO_SQL_TYPE_MAP.put(double.class, Types.DOUBLE);
        CLASS_TO_SQL_TYPE_MAP.put(Float.class, Types.REAL);
        CLASS_TO_SQL_TYPE_MAP.put(float.class, Types.REAL);
        CLASS_TO_SQL_TYPE_MAP.put(Boolean.class, Types.BOOLEAN);
        CLASS_TO_SQL_TYPE_MAP.put(boolean.class, Types.BOOLEAN);
        CLASS_TO_SQL_TYPE_MAP.put(java.sql.Date.class, Types.DATE);
        CLASS_TO_SQL_TYPE_MAP.put(java.sql.Time.class, Types.TIME);
        CLASS_TO_SQL_TYPE_MAP.put(java.sql.Timestamp.class, Types.TIMESTAMP);
        CLASS_TO_SQL_TYPE_MAP.put(java.util.Date.class, Types.TIMESTAMP);
        CLASS_TO_SQL_TYPE_MAP.put(BigDecimal.class, Types.DECIMAL);
        CLASS_TO_SQL_TYPE_MAP.put(byte[].class, Types.VARBINARY);
    }

    public static int toSql(Object obj) {
        if (obj == null) {
            return Types.NULL;
        }

        Class<?> keyClass;
        if (obj instanceof Class) {
            keyClass = (Class<?>) obj;
        } else {
            keyClass = obj.getClass();
        }

        return CLASS_TO_SQL_TYPE_MAP.getOrDefault(keyClass, Types.JAVA_OBJECT);
    }
}