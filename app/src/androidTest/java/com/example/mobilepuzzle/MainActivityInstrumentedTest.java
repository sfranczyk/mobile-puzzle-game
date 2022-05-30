package com.example.mobilepuzzle;

import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Test
    public void testNavigationToInGameScreen() {

        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        FragmentScenario<MenuFragment> titleScenario = FragmentScenario.launchInContainer(MenuFragment.class);

        titleScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.main_nav);

            Navigation.setViewNavController(fragment.requireView(), navController);
        });

        onView(ViewMatchers.withId(R.id.btnNewGame)).perform(ViewActions.click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.selectPictureFragment);
    }
}