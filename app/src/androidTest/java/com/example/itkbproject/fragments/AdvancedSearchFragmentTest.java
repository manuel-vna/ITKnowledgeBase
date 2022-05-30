package com.example.itkbproject.fragments;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.itkbproject.R;
import com.example.itkbproject.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AdvancedSearchFragmentTest {


    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void test_several_search_scenarios(){
        onView(withId(R.id.MainFragmentContextSearch)).perform(click());

        onView(withId(R.id.ContextSearchSpinnerSections)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Description"))).perform(click());
        onView(withId(R.id.ContextSearchDescriptionEditText)).perform(typeText("Test"));
        onView(withId(R.id.ContextSearchButtonSearch)).perform(click());
        onView(withId(R.id.ContextSearchButtonClear)).perform(click());

        onView(withId(R.id.ContextSearchSpinnerSections)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Category"))).perform(click());
        onView(withId(R.id.ContextSearchCategoryEditText)).perform(typeText("Java"));
        onView(withId(R.id.ContextSearchButtonSearch)).perform(click());

        onView(withId(R.id.ContextSearchButtonToHome)).perform(click());
        onView(withId(R.id.MainFragmentKeywordSearch)).perform(typeText("git"));
        onView(withId(R.id.MainSearchButtonSearch)).perform(click());

    }


}