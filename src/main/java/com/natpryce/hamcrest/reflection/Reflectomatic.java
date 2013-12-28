package com.natpryce.hamcrest.reflection;

import static com.natpryce.hamcrest.reflection.ModifierMatcher.withModifiers;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.STATIC;
import static org.hamcrest.Matchers.not;

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
		@Override
		public Field[] featuresOf(final Class<?> fromClass) {
			return fromClass.getDeclaredFields();
		}
	};

	public static FeatureType<Method> METHODS = new FeatureType<Method>() {
		@Override
		public Method[] featuresOf(final Class<?> fromClass) {
			return fromClass.getDeclaredMethods();
		}
	};

	public static FeatureType<Class<?>> CLASSES = new FeatureType<Class<?>>() {
		@Override
		public Class<?>[] featuresOf(final Class<?> fromClass) {
			return fromClass.getDeclaredClasses();
		}
	};

	private static <T> List<T> all(final Class<?> type, final FeatureType<T> featureType, final Matcher<? super T> criteria) {
		final List<T> matchingFeatures = new ArrayList<T>();

		for (Class<?> t = type; t != null; t = t.getSuperclass())
			collectMatchingFeatures(featureType.featuresOf(t), criteria, matchingFeatures);

		return matchingFeatures;
	}

	private static <T> void collectMatchingFeatures(final T[] features, final Matcher<? super T> criteria, final List<T> collection) {
		for (final T feature : features)
			if (criteria.matches(feature))
				collection.add(feature);
	}

	public static List<Field> fieldsOf(final Class<?> type, final Matcher<? super Field> criteria) {
		return all(type, FIELDS, criteria);
	}

	public static List<Method> methodsOf(final Class<?> type, final Matcher<? super Method> criteria) {
		return all(type, METHODS, criteria);
	}

	public static List<Constructor<?>> constructorsOf(final Class<?> type, final Matcher<? super Constructor<?>> criteria) {
		final List<Constructor<?>> matchingConstructors = new ArrayList<Constructor<?>>();
		collectMatchingFeatures(type.getDeclaredConstructors(), criteria, matchingConstructors);
		return matchingConstructors;
	}

	public static List<Class<?>> classesIn(final Class<?> type, final Matcher<? super Class<?>> criteria) {
		return all(type, CLASSES, criteria);
	}

	public static void copyFieldsFromTo(final Object from, final Object to) {
		for (final Field field : fieldsOf(to.getClass(), not(withModifiers(FINAL | STATIC))))
			try {
				field.setAccessible(true);
				field.set(to, field.get(from));
			} catch (final IllegalAccessException e) {
				throw new IllegalStateException("cannot access field that has been made accessible", e);
			}
	}
}
