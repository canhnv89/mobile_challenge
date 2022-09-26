package com.example.mobilechallenge.utils.formatter

import android.text.Editable
import android.widget.EditText

/**
 * Formatter for Card number EditText. This will format the inputted number basing on
 * the prefix card number (VISA, MASTER, AMEX). To support more card types, define its
 * prefix and pattern
 */
class CardNumFormatter(editText: EditText) : TextFormatter(editText) {

    companion object {
        const val VISA = "4"
        const val MASTER = "5"
        const val AMEX_1 = "34"
        const val AMEX_2 = "37"
        //Add more card type here

        val MASTER_VISA_PATTERN =
            TextPattern(16, intArrayOf(4, 4, 4, 4)) //Max 16 digits, format 1234-5678-9123-4567
        val AMEX_PATTERN =
            TextPattern(15, intArrayOf(4, 6, 5)) //15 digit, format 1234-567891-23456
        val UNKNOWN_PATTERN =
            TextPattern(16, intArrayOf(16))
        //Add more cart pattern here
    }

    override fun format(s: Editable?) {
        s?.let {
            if (s.isEmpty()) {
                current = s.toString()
            } else {
                val textPattern = if (s.indexOf(VISA) == 0 || s.indexOf(MASTER) == 0) {
                    MASTER_VISA_PATTERN
                } else if (s.indexOf(AMEX_1) == 0 || s.indexOf(AMEX_2) == 0) {
                    AMEX_PATTERN
                } else {
                    UNKNOWN_PATTERN
                }
                format(s, textPattern)
            }
        }
    }

    override fun isValid(s: Editable?): Boolean {
        if (s.isNullOrEmpty()) return false
        return CardVerifier.isValidCardNum(s.getPureNumber())
    }
}