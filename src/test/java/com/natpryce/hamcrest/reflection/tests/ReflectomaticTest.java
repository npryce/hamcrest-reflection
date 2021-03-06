package com.natpryce.hamcrest.reflection.tests;

import static com.natpryce.hamcrest.reflection.MemberNameMatcher.withName;
import static com.natpryce.hamcrest.reflection.ModifierMatcher.withModifiers;
import static com.natpryce.hamcrest.reflection.Reflectomatic.constructorsOf;
import static com.natpryce.hamcrest.reflection.Reflectomatic.fieldsOf;
import static com.natpryce.hamcrest.reflection.Reflectomatic.methodsOf;
import static com.natpryce.hamcrest.reflection.tests.ItemsBeforeMatcher.Builder.hasItems;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.natpryce.hamcrest.reflection.Reflectomatic;

public class ReflectomaticTest {
	public static class BaseClass {
		int fieldA = 10;

		public void m1() {
		}

		public void n1() {
		}

		public class InnnerClassInBaseClass {
		}

		static class StaticInnerclassInBaseClass {
		}
	}

	public static class DerivedClass extends BaseClass {
		int fieldB = 20;
		int anotherField = 30;

		public void m2() {
		}

		public DerivedClass() {
		}

		protected DerivedClass(final int n) {
			this.anotherField = n;
		}

		public class InnnerClassInDerivedClass {
		}

		static class StaticInnerclassInDerivedClass {
		}
	}

	@Test
	public void listsMatchingFieldsInReverseInheritanceOrder() throws Exception {
		assertThat(fieldsOf(DerivedClass.class, withName(startsWith("field"))),
				equalTo(asList(DerivedClass.class.getDeclaredField("fieldB"), BaseClass.class.getDeclaredField("fieldA"))));
	}

	@Test
	public void listsMatchingMethodsInReverseInheritanceOrder() throws Exception {
		assertThat(methodsOf(DerivedClass.class, withName(startsWith("m"))),
				equalTo(asList(DerivedClass.class.getDeclaredMethod("m2"), BaseClass.class.getDeclaredMethod("m1"))));
	}

	@Test
	public void listsMatchingNestedClassesInReverseInheritanceOrder() throws Exception {
		assertThat(
				Reflectomatic.classesIn(DerivedClass.class, anything()),
				hasItems(DerivedClass.InnnerClassInDerivedClass.class, DerivedClass.StaticInnerclassInDerivedClass.class).beforeItems(
						BaseClass.InnnerClassInBaseClass.class, BaseClass.StaticInnerclassInBaseClass.class));
	}

	@Test
	public void listsMatchingConstructors() throws Exception {
		assertThat(constructorsOf(DerivedClass.class, withModifiers(PUBLIC)), equalTo(asList(DerivedClass.class.getConstructors())));
	}

	@Test
	public void canCopyFieldsBetweenObjects() {
		final DerivedClass from = new DerivedClass();
		from.fieldA = 10;
		from.fieldB = 20;
		from.anotherField = 30;

		final BaseClass to = new BaseClass();
		to.fieldA = 9999;

		Reflectomatic.copyFieldsFromTo(from, to);
		assertThat(to.fieldA, equalTo(from.fieldA));
	}
}
