package com.example.mobilechallenge.model

import com.example.mobilechallenge.utils.formatter.CardVerifier

data class CardInfo(var cardNum: String, var expiryDate: String, var cvv: String) {
    fun isValid(): Boolean {
        return CardVerifier.isValidCardNum(cardNum)
                && CardVerifier.isValidExpiryDate(expiryDate)
                && CardVerifier.isCvvValid(cvv)
    }
}
