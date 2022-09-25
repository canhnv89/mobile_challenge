package com.example.mobilechallenge

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.lifecycle.ViewModelProvider
import com.example.mobilechallenge.databinding.ActivityMainBinding
import com.example.mobilechallenge.utils.formatter.CardNumFormatter
import com.example.mobilechallenge.utils.formatter.CvvFormatter
import com.example.mobilechallenge.utils.formatter.ExpiryDateFormatter
import com.example.mobilechallenge.utils.formatter.getPureNumber
import com.example.mobilechallenge.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cardNumFormatter: CardNumFormatter
    private lateinit var expiryDateFormatter: ExpiryDateFormatter
    private lateinit var cvvFormatter: CvvFormatter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        cardNumFormatter = CardNumFormatter(binding.cardNumberEditText)
        expiryDateFormatter = ExpiryDateFormatter(binding.cardDateEditText)
        cvvFormatter = CvvFormatter(binding.cardCVCEditText)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.payButton.setOnClickListener {
            val cardNum = binding.cardNumberEditText.getPureNumber()
            val expiryDate = binding.cardDateEditText.text.toString()
            val cvv = binding.cardCVCEditText.text.toString()
            viewModel.updateCardInfo(cardNum, expiryDate, cvv)
        }
    }
}