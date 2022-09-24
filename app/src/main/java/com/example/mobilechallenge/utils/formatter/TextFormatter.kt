package com.example.mobilechallenge.utils.formatter

import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

abstract class TextFormatter(val editText: EditText) {
    val nonDigits: Regex
        get() = Regex("[^\\d]")
    var current = " "
    var maxLength = 0

    companion object {
        const val VALID_COLOR = Color.BLACK
        const val ERROR_COLOR = Color.RED
    }

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                format(s)
                if (s != null && getPureNumber(s).length == maxLength) {
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
                if (getPureNumber(editText.text).length < maxLength) {
                    editText.setTextColor(VALID_COLOR)
                }
            }
        }
    }

    abstract fun format(s: Editable?)
    abstract fun isValid(s: Editable?): Boolean

    private fun setColorByValidity() {
        val color = if (isValid(editText.text)) VALID_COLOR else ERROR_COLOR
        editText.setTextColor(color)
    }

    protected fun format(s: Editable?, textPattern: TextPattern?) {
        s?.let {
            if (s.toString() != current) {
                val userInput = getPureNumber(s)
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

    fun splitTextByPattern(input: String, textPattern: TextPattern): String {
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

    protected fun getPureNumber(s: Editable): String {
        return s.toString().replace(nonDigits, "")
    }
}