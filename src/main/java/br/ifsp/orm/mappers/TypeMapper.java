package br.ifsp.orm.mappers;

import br.ifsp.orm.OrmEntity;

import java.sql.Types;

public class TypeMapper {
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

        return SQLClassMapper.getClassToSqlTypes().getOrDefault(keyClass, Types.JAVA_OBJECT);

    }

    public static String fromSgbd(Class<?> clazz, OrmEntity.SGBD sgbd) {
        return switch (sgbd) {
            case POSTGRES -> PostgresClassMapper.getClassToPostgresTypes().getOrDefault(clazz, "INTEGER");
            case SQLITE -> SQLiteClassMapper.getClassToSqliteTypes().getOrDefault(clazz, "NULL");
        };
    }
}
