package com.example.mobilechallenge

import android.content.Context
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.mobilechallenge.model.CardType
import com.example.mobilechallenge.utils.formatter.CardNumFormatter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CardNumFormatterTest {
    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var cardNumEditText: EditText
    private lateinit var cardNumFormatter: CardNumFormatter
    private lateinit var context: Context
    private val invalidCardNum = "123"
    private val validVisaCardNum = "4484070000035519"
    private val validMasterCardNum = "5352151570003404"
    private val amexCardNum = "375215157000340"
    private val validVisaCardNumFormatted = "4484 0700 0003 5519"
    private val validMasterCardNumFormatted = "5352 1515 7000 3404"
    private val amexCardNumFormatted = "3752 151570 00340"

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        cardNumEditText = EditText(context)
        cardNumFormatter = CardNumFormatter(cardNumEditText)
    }

    @Test
    fun card_num_formatter_color_test() {
        //Invalid card
        cardNumEditText.setText(invalidCardNum)
        assertEquals(false, cardNumFormatter.isValid(cardNumEditText.text))
        assertEquals(cardNumFormatter.getValidColor(), cardNumEditText.currentTextColor)

        //Error color
        cardNumFormatter.setColorByValidity()
        assertEquals(cardNumFormatter.getErrorColor(), cardNumEditText.currentTextColor)


        //Valid color for valid card
        cardNumEditText.setText(validVisaCardNum)
        assertEquals(true, cardNumFormatter.isValid(cardNumEditText.text))
        cardNumFormatter.setColorByValidity()
        assertEquals(cardNumFormatter.getValidColor(), cardNumEditText.currentTextColor)
    }

    @Test
    fun card_num_formatter_text_test() {
        //Not formatted text
        cardNumEditText.setText(invalidCardNum)
        assertEquals(invalidCardNum, cardNumEditText.text.toString())

        //Visa formatted text
        cardNumEditText.setText(validVisaCardNum)
        assertEquals(validVisaCardNumFormatted, cardNumEditText.text.toString())
        assertEquals(CardType.VISA, cardNumFormatter.getCardTypeLiveData().value)

        //Master formatted text
        cardNumEditText.setText(validMasterCardNum)
        assertEquals(validMasterCardNumFormatted, cardNumEditText.text.toString())
        assertEquals(CardType.MASTER, cardNumFormatter.getCardTypeLiveData().value)

        //Amex formatted text
        cardNumEditText.setText(amexCardNum)
        assertEquals(amexCardNumFormatted, cardNumEditText.text.toString())
        assertEquals(CardType.AMEX, cardNumFormatter.getCardTypeLiveData().value)
    }

}