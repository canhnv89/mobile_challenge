package com.example.mobilechallenge.utils.formatter

import android.text.Editable
import android.widget.EditText
import java.util.*

class ExpiryDateFormatter(editText: EditText) : TextFormatter(editText) {
    companion object {
        val textPattern = TextPattern(4, intArrayOf(2, 2), '/')
    }

    override fun format(s: Editable?) {
        format(s, textPattern)
    }

    override fun isValid(s: Editable?): Boolean {
        if (s.isNullOrEmpty()) return false
        val parts = s.split(textPattern.separator)
        if (parts.size < textPattern.pattern.size) return false
        val month = Integer.parseInt(parts[0])
        val year = Integer.parseInt(parts[1])
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
        return month in 1..12 && (year > currentYear || (year == currentYear && currentMonth <= month))
    }
}
