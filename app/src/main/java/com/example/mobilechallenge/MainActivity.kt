package com.example.mobilechallenge

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.example.mobilechallenge.databinding.ActivityMainBinding
import com.example.mobilechallenge.utils.formatter.CardNumFormatter
import com.example.mobilechallenge.utils.formatter.CvvFormatter
import com.example.mobilechallenge.utils.formatter.ExpiryDateFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cardNumFormatter: CardNumFormatter
    private lateinit var expiryDateFormatter: ExpiryDateFormatter
    private lateinit var cvvFormatter: CvvFormatter

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        cardNumFormatter = CardNumFormatter(binding.cardNumberEditText)
        expiryDateFormatter = ExpiryDateFormatter(binding.cardDateEditText)
        cvvFormatter = CvvFormatter(binding.cardCVCEditText)
    }
}