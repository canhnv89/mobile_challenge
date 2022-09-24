package com.example.mobilechallenge.utils

class CardVerifier {
    companion object {
        // Return true if the card number is valid
        fun isValid(cardNumber: Long): Boolean {
            return (getSize(cardNumber) in 13..16 && (prefixMatch(cardNumber, 4)
                    || prefixMatch(cardNumber, 5) || prefixMatch(cardNumber, 37)
                    || prefixMatch(cardNumber, 6))
                    && (sumDoubleEven(cardNumber) + sumOdd(cardNumber)) % 10 == 0)
        }

        // Get the result from Step 2
        private fun sumDoubleEven(cnumber: Long): Int {
            var sum = 0
            val num = cnumber.toString() + ""
            var i = getSize(cnumber) - 2
            while (i >= 0) {
                sum += getDigit((num[i].toString() + "").toInt() * 2)
                i -= 2
            }
            return sum
        }

        // Return this cnumber if it is a single digit, otherwise,
        // return the sum of the two digits
        private fun getDigit(cnumber: Int): Int {
            return if (cnumber < 9) cnumber else cnumber / 10 + cnumber % 10
        }

        // Return sum of odd-place digits in cnumber
        private fun sumOdd(cnumber: Long): Int {
            var sum = 0
            val num = cnumber.toString() + ""
            var i = getSize(cnumber) - 1
            while (i >= 0) {
                sum += (num[i].toString() + "").toInt()
                i -= 2
            }
            return sum
        }

        // Return true if the digit d is a prefix for cnumber
        private fun prefixMatch(cnumber: Long, d: Int): Boolean {
            return getPrefix(cnumber, getSize(d.toLong())) == d.toLong()
        }

        // Return the number of digits in d
        private fun getSize(d: Long): Int {
            val num = d.toString() + ""
            return num.length
        }

        // Return the first k number of digits from
        // number. If the number of digits in number
        // is less than k, return number.
        private fun getPrefix(cnumber: Long, k: Int): Long {
            if (getSize(cnumber) > k) {
                val num = cnumber.toString() + ""
                return num.substring(0, k).toLong()
            }
            return cnumber
        }
    }
}