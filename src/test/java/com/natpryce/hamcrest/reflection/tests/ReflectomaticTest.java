package com.natpryce.hamcrest.reflection.tests;

import static com.natpryce.hamcrest.reflection.MemberNameMatcher.withName;
import static com.natpryce.hamcrest.reflection.ModifierMatcher.withModifiers;
import static com.natpryce.hamcrest.reflection.Reflectomatic.constructorsOf;
import static com.natpryce.hamcrest.reflection.Reflectomatic.fieldsOf;
import static com.natpryce.hamcrest.reflection.Reflectomatic.methodsOf;
import static com.natpryce.hamcrest.reflection.tests.ReflectomaticTest.ItemsBeforeMatcher.ItemsFollowMatcherBuilder.hasItems;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import com.natpryce.hamcrest.reflection.Reflectomatic;

public class ReflectomaticTest {
    public static class BaseClass {
        int fieldA = 10;
        
        public void m1() {}
        public void n1() {}
        
        public class InnnerClassInBaseClass{}
        static class StaticInnerclassInBaseClass{} 
    }
    
    public static class DerivedClass extends BaseClass {
        int fieldB = 20;
        int anotherField = 30;
        
        public void m2() {}
        
        public DerivedClass() {}
        protected DerivedClass(int n) {anotherField = n;}
        
        public class InnnerClassInDerivedClass{}
        static class StaticInnerclassInDerivedClass{} 
    }
    
    @Test
    public void listsMatchingFieldsInReverseInheritanceOrder() throws Exception {
        assertThat(
                fieldsOf(DerivedClass.class, withName(startsWith("field"))), 
                equalTo(asList(DerivedClass.class.getDeclaredField("fieldB"), BaseClass.class.getDeclaredField("fieldA"))));
    }

    @Test
    public void listsMatchingMethodsInReverseInheritanceOrder() throws Exception {
        assertThat(
                methodsOf(DerivedClass.class, withName(startsWith("m"))), 
                equalTo(asList(DerivedClass.class.getDeclaredMethod("m2"), BaseClass.class.getDeclaredMethod("m1"))));
    }

    @Test
    public void listsMatchingNestedClassesInReverseInheritanceOrder() throws Exception {
        assertThat(
                Reflectomatic.classesIn(DerivedClass.class, anything()),
                hasItems(DerivedClass.InnnerClassInDerivedClass.class, DerivedClass.StaticInnerclassInDerivedClass.class)
                .beforeItems(BaseClass.InnnerClassInBaseClass.class, BaseClass.StaticInnerclassInBaseClass.class));
    }

    @Test
    public void listsMatchingConstructors() throws Exception {
        assertThat(constructorsOf(DerivedClass.class, withModifiers(PUBLIC)),
                equalTo(asList(DerivedClass.class.getConstructors())));
    }
    
    @Test
    public void canCopyFieldsBetweenObjects() {
    	DerivedClass from = new DerivedClass();
    	from.fieldA = 10;
    	from.fieldB = 20;
    	from.anotherField = 30;
    	
    	BaseClass to = new BaseClass();
    	to.fieldA = 9999;
    	
    	Reflectomatic.copyFieldsFromTo(from, to);
    	assertThat(to.fieldA, equalTo(from.fieldA));
    }
    
	/**
	 * A Matcher checking whether some items occur in a {@code List} before some other items. The list may contain more items than the checked ones.
	 * 
	 * @author Markus KARG (markus@headcrashing.eu)
	 * 
	 * @param <T>
	 *            Type of list items.
	 */
	static final class ItemsBeforeMatcher<T> extends TypeSafeMatcher<List<T>> {
		private final List<T> leadingItems;
		private final List<T> trailingItems;

		private ItemsBeforeMatcher(final List<T> leadingItems, final List<T> trailingItems) {
			this.leadingItems = leadingItems;
			this.trailingItems = trailingItems;
		}

		@Override
		public final boolean matchesSafely(final List<T> list) {
			return this.lastLeadingItemIsBeforeFirstTrailingItemIn(list);
		}

		private final boolean lastLeadingItemIsBeforeFirstTrailingItemIn(final List<T> list) {
			return this.lastLeadingIndexIn(list) < this.firstTrailingIndexIn(list);
		}

		private final int lastLeadingIndexIn(final List<T> list) {
			return lastIndex(list, this.leadingItems);
		}

		private final int firstTrailingIndexIn(final List<T> list) {
			return firstIndex(list, this.trailingItems);
		}

		private static final <T> int firstIndex(final List<T> list, final List<T> checkedItems) {
			int i = MAX_VALUE;
			for (final T item : checkedItems)
				i = min(i, list.indexOf(item));
			return i;
		}

		private static final <T> int lastIndex(final List<T> list, final List<T> checkedItems) {
			int i = MIN_VALUE;
			for (final T item : checkedItems)
				i = max(i, list.lastIndexOf(item));
			return i;
		}

		public final void describeTo(final Description description) {
			description.appendValueList("has items [", ", ", "]", this.leadingItems).appendValueList(" before items [", ", ", "]", this.trailingItems);
		}

		static final class ItemsFollowMatcherBuilder<T> {
			@SafeVarargs
			public static final <T> ItemsFollowMatcherBuilder<T> hasItems(final T... leadingItems) {
				return new ItemsFollowMatcherBuilder<T>(leadingItems);
			}

			@SafeVarargs
			public final ItemsBeforeMatcher<T> beforeItems(final T... trailingItems) {
				return new ItemsBeforeMatcher<T>(asList(this.leadingItems), asList(trailingItems));
			}

			private final T[] leadingItems;

			@SafeVarargs
			private ItemsFollowMatcherBuilder(final T... leadingItems) {
				this.leadingItems = leadingItems;
			}
		}
	}

	@Test
	public final void shouldAssertThatTrailingItemsFollowLeadingItems() {
		// given
		final List<Character> someList = asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');

		// when
		final Character[] ofFirstGroup = new Character[] { 'B', 'D' };
		final Character[] ofSecondGroup = new Character[] { 'E', 'G' };

		// then
		assertThat(someList, hasItems(ofFirstGroup).beforeItems(ofSecondGroup));
	}
}
