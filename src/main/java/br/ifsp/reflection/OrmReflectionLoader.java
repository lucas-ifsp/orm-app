package br.ifsp.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrmReflectionLoader implements ReflectionLoader {
    private static OrmReflectionLoader instance;
    private final ClassLoader classLoader;

    private final Set<Class<?>> loadedClasses = new LinkedHashSet<>();

    private OrmReflectionLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Set<Class<?>> getLoadedClasses() {
        return Set.copyOf(loadedClasses);
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return loadedClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return loadedClasses.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> parentType) {
        return loadedClasses.stream()
                .filter(c -> !parentType.equals(c))
                .filter(parentType::isAssignableFrom)
                .filter(c -> !c.isInterface())
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .map(c -> (Class<T>) c.asSubclass(parentType))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public <T> List<T> createInstancesOf(Class<T> parentType) {
        return getSubTypesOf(parentType).stream()
                .map(OrmReflectionLoader::<T>instantiate)
                .toList();
    }

    private static <T> T instantiate(Class<? extends T> clazz) {
        try {
            Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException exception
        ) {
            throw new RuntimeException(exception);
        }
    }

    public static OrmReflectionLoader fromCurrentClasspath() {
        if (instance != null) return instance;

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        instance = new OrmReflectionLoader(contextClassLoader);
        return instance;
    }

    public OrmReflectionLoader scanClasspath(String basePackage) {
        String path = basePackage.replace(".", "/");
        String classpath = System.getProperty("java.class.path");

        Arrays.stream(classpath.split(File.pathSeparator))
                .map(Path::of)
                .filter(Files::isDirectory)
                .map(entry -> entry.resolve(path))
                .filter(Files::exists)
                .forEach(dir -> scanDirectory(dir, basePackage));

        return this;
    }

    private void scanDirectory(Path packageDirectory, String basePackage) {
        try (Stream<Path> stream = Files.walk(packageDirectory)) {
            stream.filter(path -> path.toString().endsWith(".class"))
                    .map(classPath -> OrmReflectionLoader.toClassName(packageDirectory, classPath, basePackage))
                    .forEach(this::loadClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadClass(String className) {
        if (className.startsWith("$")) return;
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            loadedClasses.add(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toClassName(Path packageDirectory, Path classPath, String basePackage) {
        Path relativePath = packageDirectory.relativize(classPath);
        return basePackage + "." +
                relativePath.toString()
                        .replace(File.separatorChar, '.')
                        .replace(".class", "");
    }
}
