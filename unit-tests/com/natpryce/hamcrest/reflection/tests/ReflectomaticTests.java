package com.natpryce.hamcrest.reflection.tests;

import static com.natpryce.hamcrest.reflection.MemberNameMatcher.withName;
import static com.natpryce.hamcrest.reflection.ModifierMatcher.withModifiers;
import static com.natpryce.hamcrest.reflection.Reflectomatic.constructorsOf;
import static com.natpryce.hamcrest.reflection.Reflectomatic.fieldsOf;
import static com.natpryce.hamcrest.reflection.Reflectomatic.methodsOf;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ReflectomaticTests {
    public static class BaseClass {
        int fieldA = 10;
        
        public void m1() {}
        public void n1() {}
    }
    
    public static class DerivedClass extends BaseClass {
        int fieldB = 20;
        int anotherField = 30;
        
        public void m2() {}
        
        public DerivedClass() {}
        protected DerivedClass(int n) {anotherField = n;}
    }
    
    @Test
    public void listsMatchingFieldsInReverseInheritanceOrder() throws Exception {
        assertThat(
                fieldsOf(DerivedClass.class, withName(startsWith("field"))), 
                equalTo(asList(DerivedClass.class.getDeclaredField("fieldB"), BaseClass.class.getDeclaredField("fieldA"))));
    }

    @Test
    public void listsMatchingMethodsReverseInheritanceOrder() throws Exception {
        assertThat(
                methodsOf(DerivedClass.class, withName(startsWith("m"))), 
                equalTo(asList(DerivedClass.class.getDeclaredMethod("m2"), BaseClass.class.getDeclaredMethod("m1"))));
    }
    
    @Test
    public void listsMatchingConstructors() throws Exception {
        assertThat(constructorsOf(DerivedClass.class, withModifiers(PUBLIC)),
                equalTo(asList(DerivedClass.class.getConstructors())));
    }
}
