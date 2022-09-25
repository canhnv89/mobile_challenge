package com.example.mobilechallenge.utils.formatter

import android.text.Editable
import android.widget.EditText

class CardNumFormatter(editText: EditText) : TextFormatter(editText) {

    companion object {
        const val MASTER_VISA = '4'
        const val AMEX = '3'
    }

    private val patternMap = mapOf(
        MASTER_VISA to TextPattern(16, intArrayOf(4, 4, 4, 4), ' '),
        AMEX to TextPattern(15, intArrayOf(4, 6, 5), ' ')
    )

    override fun format(s: Editable?) {
        s?.let {
            if (s.isEmpty()) {
                current = s.toString()
            } else {
                val textPattern = patternMap[s[0]] ?: patternMap[MASTER_VISA]
                format(s, textPattern)
            }
        }
    }

    override fun isValid(s: Editable?): Boolean {
        if (s.isNullOrEmpty()) return false
        if (patternMap[s[0]] == null) return false
        return CardVerifier.isValidCardNum(s.getPureNumber())
    }
}