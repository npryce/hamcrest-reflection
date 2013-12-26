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
    
    public HasAnnotationMatcher(Class<T> annotationType, Matcher<? super T> annotationMatcher) {
        this.annotationType = annotationType;
        this.annotationMatcher = annotationMatcher;
    }

    @Override
    protected boolean matchesSafely(AnnotatedElement item, Description mismatchDescription) {
        T annotation = item.getAnnotation(annotationType);
        if (annotation == null) {
            mismatchDescription
                .appendText("does not have annotation ")
                .appendText(annotationType.getName());
            return false;
        }
        
        if (!annotationMatcher.matches(annotation)) {
            annotationMatcher.describeMismatch(annotation, mismatchDescription);
            return false;
        }
        
        return true;
    }

    public void describeTo(Description description) {
        
    }

    public static Matcher<AnnotatedElement> hasAnnotation(Class<? extends Annotation> annotationType) {
        return hasAnnotation(annotationType, anything(""));
    }
    
    public static <T extends Annotation> Matcher<AnnotatedElement> hasAnnotation(Class<T> annotationType, Matcher<? super T> annotationMatcher) {
        return new HasAnnotationMatcher<T>(annotationType, annotationMatcher);
    }
}
