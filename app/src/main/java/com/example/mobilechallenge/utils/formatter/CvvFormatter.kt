package com.example.mobilechallenge.utils.formatter

import android.text.Editable
import android.widget.EditText

class CvvFormatter(editText: EditText) : TextFormatter(editText) {
    companion object {
        val textPattern = TextPattern(3, intArrayOf(3))
    }

    override fun format(s: Editable?) {
        format(s, textPattern)
    }

    override fun isValid(s: Editable?): Boolean {
        return CardVerifier.isCvvValid(s.toString())
    }
}