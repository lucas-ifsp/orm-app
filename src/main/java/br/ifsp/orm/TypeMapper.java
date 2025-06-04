package br.ifsp.orm;

import java.sql.Date;
import java.sql.Types;

public class TypeMapper {
    public static int toSql(Object obj) {
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
}
