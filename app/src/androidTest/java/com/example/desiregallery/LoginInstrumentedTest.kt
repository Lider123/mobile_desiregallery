package com.example.desiregallery

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.desiregallery.ui.activities.LoginActivity
import org.junit.runner.RunWith
import com.example.desiregallery.ui.activities.SignupActivity
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun beforeTesting() {
        Intents.init()
    }

    @Test
    fun inputValidation_isCorrect() {
        val inputCorrect = "input"
        val inputsWrong = arrayOf("", " ")

        for (input in inputsWrong) {
            checkInput(input, inputCorrect, correct = false)
            checkInput(inputCorrect, input, correct = false)
        }
        checkInput(inputCorrect, inputCorrect, correct = true)
    }

    private fun checkInput(login: String, password: String, correct: Boolean) {
        Espresso.onView(withId(R.id.input_login)).perform(typeText(login)).perform(closeSoftKeyboard())
        Espresso.onView(withId(R.id.input_password)).perform(typeText(password)).perform(closeSoftKeyboard())
        if (correct)
            Espresso.onView(withId(R.id.button_login)).check(matches(isEnabled()))
        else
            Espresso.onView(withId(R.id.button_login)).check(matches(not(isEnabled())))

        Espresso.onView(withId(R.id.input_login)).perform(clearText())
        Espresso.onView(withId(R.id.input_password)).perform(clearText())
    }

    @Test
    fun startSignUpActivity_isCorrect() {
        Espresso.onView(withId(R.id.link_signup)).perform(click())
        intended(hasComponent(SignupActivity::class.java.name))
    }

    @After
    fun afterTesting() {
        Intents.release()
    }
}