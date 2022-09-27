package com.example.mobilechallenge

import android.content.Context
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import com.example.mobilechallenge.utils.formatter.ExpiryDateFormatter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExpiryDateFormatterTest {
    private lateinit var expiryDateEditText: EditText
    private lateinit var expiryDateFormatter: ExpiryDateFormatter
    private lateinit var context: Context
    private val invalidDateMissingLength = "123"
    private val invalidDateMonth= "1304"
    private val validDateButExpired = "1212"
    private val validDate = "1235"
    private val validDateFormatted = "12/35"
    private val invalidDates = listOf(invalidDateMissingLength, invalidDateMonth, validDateButExpired)

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        expiryDateEditText = EditText(context)
        expiryDateFormatter = ExpiryDateFormatter(expiryDateEditText)
    }

    @Test
    fun expiry_date_formatter_color_test() {
        //Invalid date
        invalidDates.forEach { date ->
            expiryDateEditText.setText(date)
            assertEquals(false, expiryDateFormatter.isValid(expiryDateEditText.text))
            //Error color
            expiryDateFormatter.setColorByValidity()
            assertEquals(expiryDateFormatter.getErrorColor(), expiryDateEditText.currentTextColor)
        }


        //Valid color for valid card
        expiryDateEditText.setText(validDate)
        assertEquals(true, expiryDateFormatter.isValid(expiryDateEditText.text))
        expiryDateFormatter.setColorByValidity()
        assertEquals(expiryDateFormatter.getValidColor(), expiryDateEditText.currentTextColor)
    }

    @Test
    fun card_num_formatter_text_test() {
        //Amex formatted text
        expiryDateEditText.setText(validDate)
        assertEquals(validDateFormatted, expiryDateEditText.text.toString())
    }

}