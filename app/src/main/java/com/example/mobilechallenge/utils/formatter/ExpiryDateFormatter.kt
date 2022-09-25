package com.example.mobilechallenge.utils.formatter

import android.text.Editable
import android.widget.EditText

class ExpiryDateFormatter(editText: EditText) : TextFormatter(editText) {
    companion object {
        val textPattern = TextPattern(4, intArrayOf(2, 2), '/')
    }

    override fun format(s: Editable?) {
        format(s, textPattern)
    }

    override fun isValid(s: Editable?): Boolean {
        return CardVerifier.isValidExpiryDate(s.toString())
    }
}
