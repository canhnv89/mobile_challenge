package com.example.mobilechallenge.model

import com.example.mobilechallenge.utils.formatter.CardVerifier
import java.util.*

/**
 * Class to store card information such as: Card number, expiry date & CVV
 */
data class CardInfo(var cardNum: String, var expiryDate: String, var cvv: String) {
    /**
     * Return if all card info is valid
     */
    fun isValid(): Boolean {
        return CardVerifier.isValidCardNum(cardNum)
                && CardVerifier.isValidExpiryDate(expiryDate)
                && CardVerifier.isCvvValid(cvv)
    }

    /**
     * Return expiry month in 2 digit2
     * i.e 01, 12
     */
    fun getExpiryMonth(): String {
        val parts = expiryDate.split("/")
        if (parts.isNotEmpty()) return parts[0]
        return ""
    }

    /**
     * Return expiry year in 4 digits
     * i.e 2022, 2025
     */
    fun getExpiryYear(): String {
        val parts = expiryDate.split("/")
        if (parts.size > 1) {
            val currentDecade = Calendar.getInstance().get(Calendar.YEAR) / 100
            return "$currentDecade${parts[1]}"
        }
        return ""
    }
}
