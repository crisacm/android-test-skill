package com.github.crisacm.testskills.ui.main

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.crisacm.testskills.R
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testA_writeInput() {
        val filter = "Ervin"
        onView(withId(R.id.input_search)).perform(typeText(filter))
        onView(withId(R.id.input_search)).check(matches(withText(filter)))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(1)))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<UsersAdapter.ViewHolder>(
                    0,
                    CustomActions.clickItemWithId(R.id.button_posts)
                )
            )
    }

    object CustomActions {
        fun clickItemWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): org.hamcrest.Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Action Description"
                }

                override fun perform(uiController: UiController, view: View) {
                    val v = view.findViewById(id) as View
                    v.performClick()
                }
            }
        }
    }
}