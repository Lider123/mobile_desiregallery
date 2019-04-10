package com.example.desiregallery

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.desiregallery.ui.activities.SignUpActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed

/**
 *
 * @project DesireGallery
 * @author babaetskv on 10.04.19
 */

@RunWith(AndroidJUnit4::class)
class SignUpInstrumentedTest {
    private val inputCorrect = "input"
    private val inputsWrong = arrayOf("", " ")

    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(SignUpActivity::class.java)

    @Test
    fun loginIsInvalid() {
        for (input in inputsWrong) {
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_login)).perform(typeText(input)).perform(
                closeSoftKeyboard()
            )
            Espresso.onView(withId(R.id.sign_up_input_password)).perform(typeText(inputCorrect)).perform(
                closeSoftKeyboard()
            )
            Espresso.onView(withId(R.id.sign_up_input_confirm)).perform(typeText(inputCorrect)).perform(
                closeSoftKeyboard()
            )

            Espresso.onView(withId(R.id.sign_up_button)).check(matches(not(isEnabled()))
            )

            Espresso.onView(withId(R.id.sign_up_input_login)).perform(clearText())
            Espresso.onView(withId(R.id.sign_up_input_password)).perform(clearText())
            Espresso.onView(withId(R.id.sign_up_input_confirm)).perform(clearText())
        }
    }

    @Test
    fun passwordIsInvalid() {
        for (input in inputsWrong) {
            Espresso.onView(withId(R.id.sign_up_input_login)).perform(typeText(inputCorrect)).perform(
                closeSoftKeyboard()
            )
            Espresso.onView(withId(R.id.sign_up_input_password)).perform(typeText(input)).perform(closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_confirm)).perform(ViewActions.typeText(inputCorrect)).perform(
                ViewActions.closeSoftKeyboard()
            )
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_button)).check(
                ViewAssertions.matches(
                    Matchers.not(
                        ViewMatchers.isEnabled()
                    )
                )
            )
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_login)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_password)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_confirm)).perform(ViewActions.clearText())
        }
    }

    @Test
    fun confirmIsInvalid() {
        for (input in inputsWrong) {
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_login)).perform(ViewActions.typeText(inputCorrect)).perform(
                ViewActions.closeSoftKeyboard()
            )
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_password)).perform(ViewActions.typeText(inputCorrect)).perform(
                ViewActions.closeSoftKeyboard()
            )
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_confirm)).perform(ViewActions.typeText(input)).perform(
                ViewActions.closeSoftKeyboard()
            )
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_button)).check(
                ViewAssertions.matches(
                    Matchers.not(
                        ViewMatchers.isEnabled()
                    )
                )
            )
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_login)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_password)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_confirm)).perform(ViewActions.clearText())
        }
    }

    @Test
    fun passwordsAreNotEqual() {
        val inputConfirm = "password"
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_login)).perform(ViewActions.typeText(inputCorrect)).perform(
            ViewActions.closeSoftKeyboard()
        )
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_password)).perform(ViewActions.typeText(inputCorrect)).perform(
            ViewActions.closeSoftKeyboard()
        )
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_confirm)).perform(ViewActions.typeText(inputConfirm)).perform(
            ViewActions.closeSoftKeyboard()
        )
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_button)).check(
            matches(
                ViewMatchers.isEnabled()
            )
        ).perform(click())

        onView(withText(R.string.non_equal_passwords)).inRoot(withDecorView(not(`is`(mActivityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_login)).perform(ViewActions.clearText())
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_password)).perform(ViewActions.clearText())
        Espresso.onView(ViewMatchers.withId(R.id.sign_up_input_confirm)).perform(ViewActions.clearText())
    }
}