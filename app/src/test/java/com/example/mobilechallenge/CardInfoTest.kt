package com.example.mobilechallenge

import com.example.mobilechallenge.model.CardInfo
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class CardInfoTest {
    private lateinit var invalidCardInfo: CardInfo
    private lateinit var emptyExpiryDate: CardInfo
    private lateinit var emptyCvv: CardInfo
    private lateinit var expiredCard: CardInfo
    private lateinit var validCardInfo: CardInfo

    @Before
    fun init() {
        invalidCardInfo = CardInfo("123","1222","123")
        emptyExpiryDate = CardInfo("4484070000035519","","123")
        emptyCvv = CardInfo("4484070000035519","12/22","")
        expiredCard = CardInfo("4484070000035519","02/22","")
        validCardInfo = CardInfo("4484070000035519","12/22","123")
    }

    @Test
    fun expiry_date_get_month_test() {
        //Invalid date
        assertEquals("", invalidCardInfo.getExpiryMonth())
        assertEquals("", emptyExpiryDate.getExpiryMonth())

        //Valid date
        assertEquals("12", validCardInfo.getExpiryMonth())
    }

    @Test
    fun expiry_date_get_year_test() {
        //Invalid date
        assertEquals("", invalidCardInfo.getExpiryYear())
        assertEquals("", emptyExpiryDate.getExpiryYear())

        //Valid date
        assertEquals("2022", validCardInfo.getExpiryYear())
    }

    @Test
    fun card_validity_test() {
        //Invalid card
        assertEquals(false, invalidCardInfo.isValid())
        assertEquals(false, emptyExpiryDate.isValid())
        assertEquals(false, emptyCvv.isValid())
        assertEquals(false, expiredCard.isValid())

        //Valid card
        assertEquals(true, validCardInfo.isValid())
    }
}