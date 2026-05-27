package br.ifsp.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public interface ReflectionLoader {
    Set<Class<?>> getLoadedClasses();
    Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation);
    Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation);
    <T> Set<Class<? extends T>> getSubTypesOf(Class<T> parentType);
    <T>List<T> createInstancesOf(Class<T> parentType);
}
