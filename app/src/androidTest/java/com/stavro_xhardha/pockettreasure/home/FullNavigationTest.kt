package com.stavro_xhardha.pockettreasure.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FullNavigationTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
        print("Testing finished")
    }

    @Test
    fun navigationMenus_ShouldBeAsRequested() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        onView(withText(R.string.names_of_god)).check(matches(isDisplayed()))
        onView(withText(R.string.quran)).check(matches(isDisplayed()))
        onView(withText(R.string.tasbeeh)).check(matches(isDisplayed()))
        onView(withText(R.string.gallery)).check(matches(isDisplayed()))
        onView(withText(R.string.news)).check(matches(isDisplayed()))
        onView(withText(R.string.action_settings)).check(matches(isDisplayed()))

    }

    @Test
    fun navigation_shoudlSendMeToNamesFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        onView(withText(R.string.names_of_god)).perform(click())

        onView(withText("Names of Allah")).check(matches(isDisplayed()))

        onView(withId(R.id.drawer_layout)).check(matches(DrawerMatchers.isClosed()))
    }
}