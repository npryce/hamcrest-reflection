package com.natpryce.hamcrest.reflection;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ModifierMatcher extends TypeSafeDiagnosingMatcher<Member> {
    private final int modifiers;

    public ModifierMatcher(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    protected boolean matchesSafely(Member item, Description mismatchDescription) {
        if (allModifiersAreSetOn(item)) {
            return true;
        }
        else {
            mismatchDescription.appendText("was ").appendText(Modifier.toString(item.getModifiers()));
            return false;
        }
    }

    private boolean allModifiersAreSetOn(Member item) {
        return (item.getModifiers() & modifiers) == modifiers;
    }
    
    public void describeTo(Description description) {
        description.appendText("is ").appendText(Modifier.toString(modifiers));
    }
    
    @Factory
    public static ModifierMatcher withModifiers(int modifiers) {
        return new ModifierMatcher(modifiers);
    }
    
    @Factory
    public static ModifierMatcher isPublic() {
        return new ModifierMatcher(Modifier.PUBLIC);
    }

    @Factory
    public static ModifierMatcher isAbstract() {
        return new ModifierMatcher(Modifier.ABSTRACT);
    }

    @Factory
    public static ModifierMatcher isStatic() {
        return new ModifierMatcher(Modifier.STATIC);
    }

    @Factory
    public static ModifierMatcher isFinal() {
        return new ModifierMatcher(Modifier.FINAL);
    }
    
    @Factory
    public static ModifierMatcher isTransient() {
        return new ModifierMatcher(Modifier.TRANSIENT);
    }
}
