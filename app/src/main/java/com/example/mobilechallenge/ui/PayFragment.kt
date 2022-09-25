package com.example.mobilechallenge.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilechallenge.R
import com.example.mobilechallenge.api.ApiStatus
import com.example.mobilechallenge.databinding.FragmentPayBinding
import com.example.mobilechallenge.utils.formatter.CardNumFormatter
import com.example.mobilechallenge.utils.formatter.CvvFormatter
import com.example.mobilechallenge.utils.formatter.ExpiryDateFormatter
import com.example.mobilechallenge.utils.formatter.getPureNumber
import com.example.mobilechallenge.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class PayFragment : Fragment() {
    private lateinit var binding: FragmentPayBinding
    private lateinit var cardNumFormatter: CardNumFormatter
    private lateinit var expiryDateFormatter: ExpiryDateFormatter
    private lateinit var cvvFormatter: CvvFormatter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayBinding.inflate(inflater)
        cardNumFormatter = CardNumFormatter(binding.cardNumberEditText)
        expiryDateFormatter = ExpiryDateFormatter(binding.cardDateEditText)
        cvvFormatter = CvvFormatter(binding.cardCVCEditText)
        lifecycleScope.launchWhenCreated {
            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            binding.viewModel = viewModel
            binding.lifecycleOwner = requireActivity()
            binding.payButton.setOnClickListener {
                onPayClicked()
            }
        }

        return binding.root
    }

    private fun onPayClicked() {
        val cardNum = binding.cardNumberEditText.getPureNumber()
        val expiryDate = binding.cardDateEditText.text.toString()
        val cvv = binding.cardCVCEditText.text.toString()
        viewModel.updateCardInfo(cardNum, expiryDate, cvv)
        if (viewModel.getCardInfo()?.isValid() == true) {
            lifecycleScope.launch {
                val result = viewModel.submitPay(requireActivity())
                if (result.status == ApiStatus.SUCCESS && result.data != null) {
                    viewModel.loadConfirmFragment()
                } else {
                    showErrorDialog(result.message)
                }
            }
        }
    }

    private fun showErrorDialog(message: String?) {
        AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.pay_error_message, message)).show()
    }

    companion object {
        private const val TAG = "PayFragment"

        @JvmStatic
        fun newInstance() =
            PayFragment()
    }
}