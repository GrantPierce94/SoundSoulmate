package com.example.as1;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import screens.SeedSetter;

@RunWith(AndroidJUnit4.class)
public class SeedSetterTest {

    @Rule
    public ActivityTestRule<SeedSetter> activityRule =
            new ActivityTestRule<>(SeedSetter.class);

    @Test
    public void testOpenMusicSwipe() {
        onView(withId(R.id.musicSwipeSend)).perform(click());

        onView(withId(R.id.cardImage)).check(matches(isDisplayed()));
    }

}
