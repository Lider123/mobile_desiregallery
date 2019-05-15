package com.example.desiregallery

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.desiregallery.ui.activities.LoginActivity
import org.junit.runner.RunWith
import com.example.desiregallery.ui.activities.SignUpActivity
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
        Espresso.onView(withId(R.id.link_sign_up)).perform(click())
        intended(hasComponent(SignUpActivity::class.java.name))
    }

    @After
    fun afterTesting() {
        Intents.release()
    }
}