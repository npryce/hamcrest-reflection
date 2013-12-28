/**
 * 
 */
package com.natpryce.hamcrest.reflection;

import java.lang.reflect.Member;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class MemberNameMatcher extends TypeSafeDiagnosingMatcher<Member> {
	private final Matcher<String> nameMatcher;

	public MemberNameMatcher(final Matcher<String> nameMatcher) {
		this.nameMatcher = nameMatcher;
	}

	@Override
	protected boolean matchesSafely(final Member item, final Description mismatchDescription) {
		final String name = item.getName();
		if (this.nameMatcher.matches(name))
			return true;
		else {
			mismatchDescription.appendText("name was ").appendValue(name);
			return false;
		}
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("with name").appendDescriptionOf(this.nameMatcher);
	}

	@Factory
	public static Matcher<Member> withName(final Matcher<String> nameMatcher) {
		return new MemberNameMatcher(nameMatcher);
	}
}