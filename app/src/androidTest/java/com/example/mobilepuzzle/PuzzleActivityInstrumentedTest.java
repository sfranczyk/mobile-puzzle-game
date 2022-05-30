package com.example.mobilepuzzle;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PuzzleActivityInstrumentedTest {
    @Rule
    public ActivityScenarioRule<PuzzleActivity> puzzleActivityRule = new ActivityScenarioRule<>(
            PuzzleActivity.class);

    @Test
    public void editTextTest() {
        puzzleActivityRule.getScenario().onActivity(f -> {
            assertEquals(f.pieces.size(), 12);
        });
    }
}