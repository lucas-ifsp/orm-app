package br.ifsp.example;

import br.ifsp.orm.ConnectionFactory;
import br.ifsp.orm.OrmEntity;
import br.ifsp.orm.mappers.TypeMapper;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseBuilder {
    private static final String ENTITIES_PACKAGE = "br.ifsp.example";

    public static void main(String[] args) throws SQLException {
        DatabaseBuilder.tableFromOrmEntity();
    }

    private static void tableFromOrmEntity() throws SQLException {
        Reflections reflections = new Reflections(ENTITIES_PACKAGE);
        Set<Class<?>> ormEntities = reflections.getTypesAnnotatedWith(OrmEntity.class);
        ormEntities.stream().map(DatabaseBuilder::generateTable).forEach(t -> {
            System.out.println(t);
            createTable(t);
        });
    }

    private static String generateColumn(Field field, OrmEntity.SGBD sgbd) {
        return String.format("\t%s %s", field.getName(), TypeMapper.fromSgbd(field.getType(), sgbd));
    }

    private static String generateTable(Class<?> type) {
        final String tableName = type.getSimpleName().toUpperCase();
        final OrmEntity annotation = type.getAnnotation(OrmEntity.class);

        final Field[] fields = type.getDeclaredFields();
        String columns = Arrays.stream(fields)
                .map(f -> generateColumn(f, annotation.value()))
                .collect(Collectors.joining(",\n"));

        return String.format("CREATE TABLE IF NOT EXISTS %s (\n%s\n)", tableName, columns);
    }

    private static void createTable(String tableSql) {
        try {
            final var stmt = ConnectionFactory.getStatement();
            stmt.execute(tableSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
