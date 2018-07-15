package com.example.android.bakingapp;

import android.support.test.runner.AndroidJUnit4;
import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.android.bakingapp.ui.DetailFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * This class test the Fragments in isolation. For that purpose has been used the helper library
 * FragmentTestRule "https://github.com/21Buttons/FragmentTestRule".
 */

@RunWith(AndroidJUnit4.class)
public class ButtonIsShowWithIngredientsText {
    @Rule public FragmentTestRule<DebugActivity, DetailFragment> mFragmentFragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, DetailFragment.class);

    @Test
    public void btn_isShow_withText_ingredients() {
        onView(withId(R.id.btn_ingredients)).check(matches(isDisplayed()));

        onView(withId(R.id.btn_ingredients)).check(matches(withText(R.string.ingredients_btn)));
    }

}
