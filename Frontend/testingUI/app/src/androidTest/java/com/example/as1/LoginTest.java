package com.example.as1;
import android.content.Intent;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import screens.LoginScreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<LoginScreen> activityRule =
            new ActivityTestRule<>(LoginScreen.class);

    @Test
    public void testUIElementsDisplayed() {
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
        onView(withId(R.id.signUpGuest)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidLogin() {
        onView(withId(R.id.username)).perform(typeText("invalidUsername"));
        onView(withId(R.id.password)).perform(typeText("invalidPassword"));

        onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void testGuestUserLogin() {
        onView(withId(R.id.signUpGuest)).perform(click());

        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));

        onView(withId(R.id.username)).perform(typeText("guestUsername"));
        onView(withId(R.id.password)).perform(typeText("guestPassword"));

        onView(withId(R.id.loginButton)).perform(click());

    }

}

