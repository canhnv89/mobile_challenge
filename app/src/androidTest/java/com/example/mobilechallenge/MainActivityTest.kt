package com.example.mobilechallenge

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mobilechallenge.model.CardInfo
import com.example.mobilechallenge.ui.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.core.IsNot.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var appContext: Context
    private lateinit var expiredCard: CardInfo
    private lateinit var validCardInfo: CardInfo

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        expiredCard = CardInfo("4484070000035519","02/22","123")
        validCardInfo = CardInfo("4484070000035519","12/28","123")
    }

    @Test
    fun pay_button_test() {
        assertEquals("com.example.mobilechallenge", appContext.packageName)
        launchActivity<MainActivity>().use {
            //Input card num
            onView(withId(R.id.cardNumberEditText)).perform(typeText(expiredCard.cardNum))
            clickPayButton()
            assertErrorShown()
            //Input expired date
            onView(withId(R.id.cardDateEditText)).perform(typeText(expiredCard.expiryDate))
            //Input CVV
            onView(withId(R.id.cardCVCEditText)).perform(typeText(expiredCard.cvv))
            clickPayButton()
            assertErrorShown()
            //Correct expiry date
            onView(withId(R.id.cardDateEditText)).perform(setTextInTextView(validCardInfo.expiryDate))
            clickPayButton()
            assertErrorHidden()

            //Check 3DS fragment
            Thread.sleep(3000)
            onView(withId(R.id.webView)).check(matches(isDisplayed()))
        }
    }

    private fun clickPayButton() {
        onView(withId(R.id.payButton)).perform(click())
    }

    private fun assertErrorShown() {
        //Error is shown
        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
    }

    private fun assertErrorHidden() {
        //Error is shown
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
    }

    fun setTextInTextView(value: String?): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(EditText::class.java))
            }

            override fun perform(uiController: UiController?, view: View) {
                (view as EditText).setText(value)
            }

            override fun getDescription(): String {
                return "replace text"
            }
        }
    }
}