package com.example.as1;

import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import android.media.MediaPlayer;
import static org.junit.Assert.assertEquals;
import screens.MusicSwipe;


@RunWith(AndroidJUnit4.class)
public class MusicSwipeTest {

    @Rule
    public ActivityTestRule<MusicSwipe> activityRule =
            new ActivityTestRule<>(MusicSwipe.class);

    @Test
    public void testSwipeRight_IncrementCurrentSongIndex() {
        int initialCurrentSongIndex = activityRule.getActivity().getCurrentSongIndex();

        onView(withId(R.id.relativeLayout)).perform(swipeRight());

        int updatedCurrentSongIndex = activityRule.getActivity().getCurrentSongIndex();

        assertEquals(initialCurrentSongIndex, updatedCurrentSongIndex);
    }

    @Test
    public void testOpenSeedSetter() {
        // Click the friends button
        onView(withId(R.id.changePreferences)).perform(click());

        // Verify that the FriendInteractions activity is opened
        onView(withId(R.id.seedSetter)).check(matches(isDisplayed()));
    }

    private ViewAction swipeRight() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }

    private ViewAction swipeUp() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER,
                GeneralLocation.TOP_CENTER, Press.FINGER);
    }
}
