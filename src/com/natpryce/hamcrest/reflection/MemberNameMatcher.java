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
    
    public MemberNameMatcher(Matcher<String> nameMatcher) {
        this.nameMatcher = nameMatcher;
    }

    @Override
    protected boolean matchesSafely(Member item, Description mismatchDescription) {
        String name = item.getName();
        if (nameMatcher.matches(name)) {
            return true;
        }
        else {
            mismatchDescription.appendText("name was ").appendValue(name);
            return false;
        }
    }

    public void describeTo(Description description) {
        description.appendText("with name").appendDescriptionOf(nameMatcher);
    }

    @Factory
    public static Matcher<Member> withName(final Matcher<String> nameMatcher) {
        return new MemberNameMatcher(nameMatcher);
    }
}