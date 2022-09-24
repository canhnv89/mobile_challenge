package com.example.mobilechallenge

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.mobilechallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val CARD_NUMBER_TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000

    private val CARD_NUMBER_TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4

    private val CARD_NUMBER_DIVIDER_MODULO =
        5 // means divider position is every 5th symbol beginning with 1

    private val CARD_NUMBER_DIVIDER_POSITION =
        CARD_NUMBER_DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0

    private val CARD_NUMBER_DIVIDER = '-'

    private val CARD_DATE_TOTAL_SYMBOLS = 5 // size of pattern MM/YY

    private val CARD_DATE_TOTAL_DIGITS = 4 // max numbers of digits in pattern: MM + YY

    private val CARD_DATE_DIVIDER_MODULO =
        3 // means divider position is every 3rd symbol beginning with 1

    private val CARD_DATE_DIVIDER_POSITION =
        CARD_DATE_DIVIDER_MODULO - 1 // means divider position is every 2nd symbol beginning with 0

    private val CARD_DATE_DIVIDER = '/'

    private val CARD_CVC_TOTAL_SYMBOLS = 3

    private lateinit var binding: ActivityMainBinding
    private var current = ""
    companion object {
        private val nonDigits = Regex("[^\\d]")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.cardNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (s.toString() != current) {
                        val userInput = s.toString().replace(nonDigits,"")
                        if (userInput.length <= 16) {
                            current = userInput.chunked(4).joinToString(" ")
                            s.filters = arrayOfNulls<InputFilter>(0)
                        }
                        s.replace(0, s.length, current, 0, current.length)
                    }
                }

            }

        })
    }

    private fun onCardNumberTextChanged(s: Editable) {
        if (!isInputCorrect(
                s,
                CARD_NUMBER_TOTAL_SYMBOLS,
                CARD_NUMBER_DIVIDER_MODULO,
                CARD_NUMBER_DIVIDER
            )
        ) {
            s.replace(
                0,
                s.length,
                concatString(
                    getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS),
                    CARD_NUMBER_DIVIDER_POSITION,
                    CARD_NUMBER_DIVIDER
                )
            )
        }
    }

    private fun onCardDateTextChanged(s: Editable) {
        if (!isInputCorrect(
                s,
                CARD_DATE_TOTAL_SYMBOLS,
                CARD_DATE_DIVIDER_MODULO,
                CARD_DATE_DIVIDER
            )
        ) {
            s.replace(
                0,
                s.length,
                concatString(
                    getDigitArray(s, CARD_DATE_TOTAL_DIGITS),
                    CARD_DATE_DIVIDER_POSITION,
                    CARD_DATE_DIVIDER
                )
            )
        }
    }

    private fun onCardCVCTextChanged(s: Editable) {
        if (s.length > CARD_CVC_TOTAL_SYMBOLS) {
            s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length)
        }
    }

    private fun isInputCorrect(
        s: Editable,
        size: Int,
        dividerPosition: Int,
        divider: Char
    ): Boolean {
        var isCorrect = s.length <= size
        for (i in 0 until s.length) {
            isCorrect = if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect and (divider == s[i])
            } else {
                isCorrect and Character.isDigit(s[i])
            }
        }
        return isCorrect
    }

    private fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String? {
        val formatted = StringBuilder()
        for (i in digits.indices) {
            if (digits[i] != '0') {
                formatted.append(digits[i])
                if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                    formatted.append(divider)
                }
            }
        }
        return formatted.toString()
    }

    private fun getDigitArray(s: Editable, size: Int): CharArray {
        val digits = CharArray(size)
        var index = 0
        var i = 0
        while (i < s.length && index < size) {
            val current = s[i]
            if (Character.isDigit(current)) {
                digits[index] = current
                index++
            }
            i++
        }
        return digits
    }
}