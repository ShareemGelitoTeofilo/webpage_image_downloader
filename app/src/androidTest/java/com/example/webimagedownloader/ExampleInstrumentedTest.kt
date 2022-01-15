package com.example.webimagedownloader

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.webimagedownloader.main.MainActivity
import com.example.webimagedownloader.webscanning.WebsiteScanningActivity


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import android.os.Bundle
import android.view.View
import androidx.test.InstrumentationRegistry.getTargetContext

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.webscanning.ScanningHandler
import org.hamcrest.Matcher


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(WebsiteScanningActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.globalrelaywebpageimagedownloader", appContext.packageName)
    }

    @Test
    fun testScanButton() {
        activityRule.scenario.onActivity {
            it.setScanningHandler(object : ScanningHandler {
                override fun onEnd() {
                }
            })
        }
        onView(withId(R.id.btnWebsiteScan)).perform(click())
        onView(isRoot()).perform(waitFor(500))
        onView(withText(getResourceString(R.string.scanning))).check(matches(isDisplayed()))
    }

    @Test
    fun testScanCompleted() {
        onView(withId(R.id.btnWebsiteScan)).perform(click())
        onView(isRoot()).perform(waitFor(2000))
        onView(withText(getResourceString(R.string.done_scanning))).check(matches(isDisplayed()))
    }

    private fun getResourceString(id: Int): String? {
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        return targetContext.resources.getString(id)


    }

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}