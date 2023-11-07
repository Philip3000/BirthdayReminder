package com.example.birthdayreminder

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.viewModels
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.birthdayreminder.models.PersonViewModel
import com.example.birthdayreminder.repository.PersonsRepository
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import kotlin.concurrent.thread
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class FirstFragmentTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testA() {
        Thread.sleep(3000)

        FirebaseAuth.getInstance().signOut()
        onView(withId(R.id.edit_email)).perform(typeText("example@example.com14"))
        onView(withId(R.id.edit_password)).perform(typeText("password"))
        onView(withId(R.id.register_button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.title)).check(matches(withText("Birthdays")))
        FirebaseAuth.getInstance().signOut()

    }

    @Test
    fun testB() {

        Thread.sleep(2000)
        onView(withId(R.id.edit_email)).perform(typeText("example@example.com14"))
        onView(withId(R.id.edit_password)).perform(typeText("password"))
        onView(withId(R.id.login_button)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.title)).check(matches(withText("Birthdays")))
    }

}