package br.ifsp.example;

import br.ifsp.orm.annotations.Column;
import br.ifsp.orm.annotations.OrmEntity;
import br.ifsp.orm.mappers.TypeMapper;
import br.ifsp.orm.persistence.ConnectionFactory;
import br.ifsp.reflection.OrmReflectionLoader;
import br.ifsp.reflection.ReflectionLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseBuilder {
    public static void main(String[] args) throws IOException {
        DatabaseBuilder.createTableFromOrmEntities();
    }

    private static void createTableFromOrmEntities() throws IOException {
        String packageName = DatabaseBuilder.class.getPackageName();
        ReflectionLoader loader = OrmReflectionLoader
                .fromCurrentClasspath()
                .scanClasspath(packageName);

        Set<Class<?>> ormEntities = loader.getTypesAnnotatedWith(OrmEntity.class);
        ormEntities.stream()
                .map(DatabaseBuilder::generateTable)
                .forEach(DatabaseBuilder::createTable);
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

    private static String generateColumn(Field field, OrmEntity.SGBD sgbd) {
        String columnTemplate = "\t%s %s";
        String mappedType = TypeMapper.fromSgbd(field.getType(), sgbd);

        if (!field.isAnnotationPresent(Column.class))
            return columnTemplate.formatted(field.getName(), mappedType);

        String columnName = field.getAnnotation(Column.class).value();
        return columnTemplate.formatted(columnName, mappedType);
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
