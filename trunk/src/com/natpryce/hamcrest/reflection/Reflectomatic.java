package com.natpryce.hamcrest.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class Reflectomatic {
    public interface FeatureType<T> {
        T[] featuresOf(Class<?> fromClass);
    }
    
    public static FeatureType<Field> FIELDS = new FeatureType<Field>() {
        public Field[] featuresOf(Class<?> fromClass) {
            return fromClass.getDeclaredFields();
        }
    };

    public static FeatureType<Method> METHODS = new FeatureType<Method>() {
        public Method[] featuresOf(Class<?> fromClass) {
            return fromClass.getDeclaredMethods();
        }
    };
    
    private static <T> List<T> all(Class<?> type, FeatureType<T> featureType, Matcher<? super T> criteria) {
        List<T> matchingFeatures = new ArrayList<T>();
        
        for (Class<?> t = type; t != null; t = t.getSuperclass()) {
            collectMatchingFeatures(featureType.featuresOf(t), criteria, matchingFeatures);
        }
        
        return matchingFeatures;
    }
    
    private static <T> void collectMatchingFeatures(T[] features, Matcher<? super T> criteria, List<T> collection) {
        for (T feature : features) {
            if (criteria.matches(feature)) {
                collection.add(feature);
            }
        }
    }
    
    public static List<Field> fieldsOf(Class<?> type, Matcher<? super Field> criteria) {
        return all(type, FIELDS, criteria);
    }
    
    public static List<Method> methodsOf(Class<?> type, Matcher<? super Method> criteria) {
        return all(type, METHODS, criteria);
    }
    
    public static List<Constructor<?>> constructorsOf(Class<?> type, Matcher<? super Constructor<?>> criteria) {
        List<Constructor<?>> matchingConstructors = new ArrayList<Constructor<?>>();
        collectMatchingFeatures(type.getDeclaredConstructors(), criteria, matchingConstructors);
        return matchingConstructors;
    }
}
