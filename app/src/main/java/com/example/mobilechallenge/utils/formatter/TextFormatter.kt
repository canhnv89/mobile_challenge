package com.example.mobilechallenge.utils.formatter

import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.VisibleForTesting

fun EditText.getPureNumber(): String {
    return TextFormatter.getPureNumber(text.toString())
}

fun Editable.getPureNumber(): String {
    return TextFormatter.getPureNumber(toString())
}

abstract class TextFormatter(private var editText: EditText) {

    var current = " "
    var maxLength = 0

    companion object {
        const val VALID_COLOR = Color.BLACK
        const val ERROR_COLOR = Color.RED
        private val nonDigits: Regex
            get() = Regex("[^\\d]")

        fun getPureNumber(s: String): String {
            return s.replace(nonDigits, "")
        }
    }

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                format(s)
                if (s != null && s.getPureNumber().length == maxLength) {
                    setColorByValidity()
                } else {
                    editText.setTextColor(VALID_COLOR)
                }
            }
        })
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                setColorByValidity()
            } else {
                if (editText.getPureNumber().length < maxLength) {
                    editText.setTextColor(VALID_COLOR)
                }
            }
        }
    }

    abstract fun format(s: Editable?)
    protected abstract fun isValid(s: Editable?): Boolean

    fun isValid(): Boolean {
        return isValid(editText.text)
    }

    fun setColorByValidity() {
        val color = if (isValid(editText.text)) VALID_COLOR else ERROR_COLOR
        editText.setTextColor(color)
    }

    @VisibleForTesting
    fun getValidColor() = VALID_COLOR

    @VisibleForTesting
    fun getErrorColor() = ERROR_COLOR

    protected fun format(s: Editable?, textPattern: TextPattern?) {
        s?.let {
            if (s.toString() != current) {
                val userInput = getPureNumber(s.toString())
                if (userInput.isEmpty() || textPattern == null) {
                    current = userInput
                    return
                } else {
                    //Format card number as defined in the pattern
                    if (userInput.length <= textPattern.length) {
                        current = splitTextByPattern(userInput, textPattern)
                        s.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s.replace(0, s.length, current, 0, current.length)
                }
            }
        }
        maxLength = textPattern?.length ?: 0
    }

    private fun splitTextByPattern(input: String, textPattern: TextPattern): String {
        val sb = StringBuilder()
        var from = 0
        for (i in textPattern.pattern.indices) {
            if (input.length > from) {
                val to = Math.min(from + textPattern.pattern[i], input.length)
                if (sb.isNotEmpty()) {
                    sb.append(textPattern.separator)
                }
                sb.append(input.substring(from, to))
                from += textPattern.pattern[i]
            } else {
                break
            }
        }
        return sb.toString()
    }
}