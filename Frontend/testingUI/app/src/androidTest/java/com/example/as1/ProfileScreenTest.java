package com.example.as1;
import android.content.Intent;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import screens.ProfileScreen;
import screens.ProfileSettings;
import screens.FriendInteractions;
import screens.MusicSwipe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ProfileScreenTest {
    @Rule
    public ActivityTestRule<ProfileScreen> activityRule =
            new ActivityTestRule<>(ProfileScreen.class);

    @Test
    public void testOpenProfileSettings() {
        onView(withId(R.id.editProfile)).perform(click());

        onView(withId(R.id.editBio)).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenFriendInteractions() {
        // Click the friends button
        onView(withId(R.id.friends)).perform(click());

        // Verify that the FriendInteractions activity is opened
        onView(withId(R.id.friendList)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavBarDiscoverIntent() {
        // Click the Discover menu item in the bottom navigation bar
        onView(withId(R.id.menu_discover)).perform(click());

        // Verify that the MusicSwipe activity is opened
        onView(withId(R.id.menu_discover)).check(matches(isDisplayed()));
    }

}
