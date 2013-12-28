package com.natpryce.hamcrest.reflection;

import static org.hamcrest.Matchers.anything;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class HasAnnotationMatcher<T extends Annotation> extends TypeSafeDiagnosingMatcher<AnnotatedElement> {
	private final Class<T> annotationType;
	private final Matcher<? super T> annotationMatcher;

	public HasAnnotationMatcher(final Class<T> annotationType, final Matcher<? super T> annotationMatcher) {
		this.annotationType = annotationType;
		this.annotationMatcher = annotationMatcher;
	}

	@Override
	protected boolean matchesSafely(final AnnotatedElement item, final Description mismatchDescription) {
		final T annotation = item.getAnnotation(this.annotationType);
		if (annotation == null) {
			mismatchDescription.appendText("does not have annotation ").appendText(this.annotationType.getName());
			return false;
		}

		if (!this.annotationMatcher.matches(annotation)) {
			this.annotationMatcher.describeMismatch(annotation, mismatchDescription);
			return false;
		}

		return true;
	}

	@Override
	public void describeTo(final Description description) {
		// Intentionally left blank.
	}

	public static Matcher<AnnotatedElement> hasAnnotation(final Class<? extends Annotation> annotationType) {
		return hasAnnotation(annotationType, anything(""));
	}

	public static <T extends Annotation> Matcher<AnnotatedElement> hasAnnotation(final Class<T> annotationType, final Matcher<? super T> annotationMatcher) {
		return new HasAnnotationMatcher<T>(annotationType, annotationMatcher);
	}
}
