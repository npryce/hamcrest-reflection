package com.natpryce.hamcrest.reflection.tests;

import static com.natpryce.hamcrest.reflection.tests.ItemsBeforeMatcher.Builder.hasItems;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

/**
 * Unit test for {@link ItemsBeforeMatcher}.
 * 
 * @author Markus KARG (markus@headcrashing.eu)
 */
public final class ItemsBeforeMatcherTest {
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
