package com.mahersoua.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mahersoua.bakingapp.adapters.RecipeAdapter;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Description;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    public static final String MAIN_ERROR_MESSAGE = "Green Tea";
    private IdlingResource mIdlingResource;
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.mahersoua.bakingapp", appContext.getPackageName());
    }

    @Test
    public void startApplicationTest() {
        onView(withId(R.id.connectionErrorTv)).check(matches(withText(mActivityTestRule.getActivity().getString(R.string.access_internet_error))));
    }

    @Test
    public void checkArrayLength() {
        assertTrue(mActivityTestRule.getActivity().getList().size() > 0);
    }

    @Test
    public void selectedItemFromAdapter() {
        onView(ViewMatchers.withId(R.id.mainList))
                .perform(RecyclerViewActions.scrollToPosition(0));
    }

    private static Matcher<RecipeAdapter.RecipeHolder> isInTheMiddle() {
        return new TypeSafeMatcher<RecipeAdapter.RecipeHolder>() {
            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeHolder customHolder) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item in the middle");
            }
        };
    }
}
