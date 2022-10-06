package com.example.mobilechallenge

import android.content.Context
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import com.example.mobilechallenge.utils.formatter.CvvFormatter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CvvFormatterTest {
    private lateinit var cvvEditText: EditText
    private lateinit var cvvFormatter: CvvFormatter
    private lateinit var context: Context
    private val invalidCvvMissingLength = "12"
    private val validCvv = "123"


    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        cvvEditText = EditText(context)
        cvvFormatter = CvvFormatter(cvvEditText)
    }

    @Test
    fun cvv_formatter_color_test() {
        //Invalid date
        cvvEditText.setText(invalidCvvMissingLength)
        assertEquals(false, cvvFormatter.isValid(cvvEditText.text))
        //Error color
        cvvFormatter.setColorByValidity()
        assertEquals(cvvFormatter.getErrorColor(), cvvEditText.currentTextColor)


        //Valid color for valid card
        cvvEditText.setText(validCvv)
        assertEquals(true, cvvFormatter.isValid(cvvEditText.text))
        cvvFormatter.setColorByValidity()
        assertEquals(cvvFormatter.getValidColor(), cvvEditText.currentTextColor)
    }
}