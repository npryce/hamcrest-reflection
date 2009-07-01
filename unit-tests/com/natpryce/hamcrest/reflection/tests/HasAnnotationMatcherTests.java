package com.natpryce.hamcrest.reflection.tests;

import static com.natpryce.hamcrest.reflection.HasAnnotationMatcher.hasAnnotation;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class HasAnnotationMatcherTests {
    public static @interface AnnotationWithArgument {
        int value();
    }
    
    public static @interface UnusedAnnotation {}
    
    public static class Annotated {
        @Deprecated
        @AnnotationWithArgument(1)
        public void annotatedMethod() {
        }
    }
    
    @Test
    public void matchesAnnotatedMembers() throws Exception {
        Method method = Annotated.class.getMethod("annotatedMethod");
        
        assertThat("has annotation", 
                method, hasAnnotation(Deprecated.class));
        assertThat("does not have unused annotation",
                method, not(hasAnnotation(UnusedAnnotation.class)));
    }
    

    @Test
    public void matchesAnnotatedMembersWithProperties() throws Exception {
        Method method = Annotated.class.getMethod("annotatedMethod");
        
        assertThat(method, hasAnnotation(AnnotationWithArgument.class, withValue(equalTo(1))));
    }
    
    public Matcher<AnnotationWithArgument> withValue(final Matcher<? super Integer> valueMatcher) {
        return new TypeSafeMatcher<AnnotationWithArgument>() {
            @Override
            protected boolean matchesSafely(AnnotationWithArgument item) {
                return valueMatcher.matches(item.value());
            }

            public void describeTo(Description description) {
                // TODO Auto-generated method stub
            }
            
            @Override
            protected void describeMismatchSafely(AnnotationWithArgument item, Description mismatchDescription) {
            }
        };
    }
}
