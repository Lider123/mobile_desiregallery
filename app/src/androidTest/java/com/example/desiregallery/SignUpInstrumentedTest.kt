package com.example.desiregallery

import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.desiregallery.ui.activities.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.widget.LinearLayout
import org.hamcrest.Matchers.*
import org.junit.Before

/**
 *
 * @project DesireGallery
 * @author babaetskv on 10.04.19
 */

@RunWith(AndroidJUnit4::class)
class SignUpInstrumentedTest {
    private val inputCorrect = "Input123"
    private val inputsWrong = arrayOf("", " ", "in put", " input ", "in?put")
    private val inputFieldIds = ArrayList<Int>()

    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(SignUpActivity::class.java)

    @Before
    fun getInputFieldsIds() {
        val inputContainer = mActivityRule.activity.findViewById(R.id.sign_up_input_container) as LinearLayout
        for (i in 0 until inputContainer.childCount)
            inputFieldIds.add(inputContainer.getChildAt(i).id)
    }

    @Test
    fun inputsAreInvalid() {
        for (i in 0 until inputFieldIds.size) { // select i-th field to be invalid
            for (input in inputsWrong) {
                for (field in inputFieldIds) { // fill all fields
                    if (field == inputFieldIds[i])
                        Espresso.onView(withId(field)).perform(typeText(input)).perform(closeSoftKeyboard())
                    else
                        Espresso.onView(withId(field)).perform(typeText(inputCorrect)).perform(closeSoftKeyboard())
                }
                Espresso.onView(withId(R.id.sign_up_button)).check(matches(not(isEnabled())))
                clearInputs()
            }
        }
    }

    @Test
    fun passwordsAreNotEqual() {
        val inputConfirm = inputCorrect.reversed()
        for (field in inputFieldIds) { // fill all fields
            if (field != R.id.sign_up_input_confirm)
                Espresso.onView(withId(field)).perform(typeText(inputCorrect)).perform(closeSoftKeyboard())
            else
                Espresso.onView(withId(field)).perform(typeText(inputConfirm)).perform(closeSoftKeyboard())
        }
        Espresso.onView(withId(R.id.sign_up_button)).check(matches(isEnabled())).perform(click())

        onView(withText(R.string.non_equal_passwords)).inRoot(withDecorView(not(`is`(mActivityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
        clearInputs()
    }

    @Test
    fun inputsAreValid() {
        for (field in inputFieldIds) // fill all fields
            Espresso.onView(withId(field)).perform(typeText(inputCorrect)).perform(closeSoftKeyboard())
        Espresso.onView(withId(R.id.sign_up_button)).check(matches(isEnabled()))
        clearInputs()
    }

    private fun clearInputs() {
        for (field in inputFieldIds)
            Espresso.onView(withId(field)).perform(clearText())
    }
}