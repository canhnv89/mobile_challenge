package com.example.mobilechallenge.model

import com.example.mobilechallenge.utils.formatter.CardVerifier
import java.util.*

data class CardInfo(var cardNum: String, var expiryDate: String, var cvv: String) {
    fun isValid(): Boolean {
        return CardVerifier.isValidCardNum(cardNum)
                && CardVerifier.isValidExpiryDate(expiryDate)
                && CardVerifier.isCvvValid(cvv)
    }

    fun getExpiryMonth(): String {
        val parts = expiryDate.split("/")
        if (parts.isNotEmpty()) return parts[0]
        return ""
    }

    fun getExpiryYear(): String {
        val parts = expiryDate.split("/")
        if (parts.size > 1) {
            val currentDecade = Calendar.getInstance().get(Calendar.YEAR) / 100
            return "$currentDecade${parts[1]}"
        }
        return ""
    }
}
