package com.natpryce.hamcrest.reflection.tests;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * A Matcher checking whether some items occur in a {@link List} before some other items. The list may contain more items than the checked ones.
 * 
 * @author Markus KARG (markus@headcrashing.eu)
 * 
 * @param <T>
 *            Type of list items.
 */
abstract class ItemsBeforeMatcher<T> extends TypeSafeMatcher<List<T>> {
	private final List<T> leadingItems;
	private final List<T> trailingItems;

	public ItemsBeforeMatcher(final List<T> leadingItems, final List<T> trailingItems) {
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

	@Override
	public final void describeTo(final Description description) {
		description.appendValueList("has items [", ", ", "]", this.leadingItems).appendValueList(" before items [", ", ", "]", this.trailingItems);
	}

	static final class Builder<T> {
		@SafeVarargs
		public static final <T> ItemsBeforeMatcher.Builder<T> hasItems(final T... leadingItems) {
			return new ItemsBeforeMatcher.Builder<T>(leadingItems);
		}

		@SafeVarargs
		public final ItemsBeforeMatcher<T> beforeItems(final T... trailingItems) {
			return new ItemsBeforeMatcher<T>(asList(this.leadingItems), asList(trailingItems)) {
				// Overriding 'abstract' restriction. This builder is the sole class which may invoke matcher's constructor.
			};
		}

		private final T[] leadingItems;

		@SafeVarargs
		private Builder(final T... leadingItems) {
			this.leadingItems = leadingItems;
		}
	}
}