package com.example.mobilechallenge.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mobilechallenge.R
import com.example.mobilechallenge.api.ApiStatus
import com.example.mobilechallenge.databinding.FragmentPayBinding
import com.example.mobilechallenge.utils.formatter.CardNumFormatter
import com.example.mobilechallenge.utils.formatter.CvvFormatter
import com.example.mobilechallenge.utils.formatter.ExpiryDateFormatter
import com.example.mobilechallenge.utils.formatter.getPureNumber
import com.example.mobilechallenge.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * Fragment to input card info from user and submit a PayRequest via Server API
 */
class PayFragment : Fragment() {
    private lateinit var binding: FragmentPayBinding
    private lateinit var cardNumFormatter: CardNumFormatter
    private lateinit var expiryDateFormatter: ExpiryDateFormatter
    private lateinit var cvvFormatter: CvvFormatter
    private lateinit var viewModel: MainViewModel
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
        callback.isEnabled = true
    }

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
            cardNumFormatter.getCardTypeLiveData().observe(viewLifecycleOwner) {
                viewModel.setCardType(it)
            }
        }

        return binding.root
    }

    private fun onPayClicked() {
        cardNumFormatter.setColorByValidity()
        expiryDateFormatter.setColorByValidity()
        cvvFormatter.setColorByValidity()
        val cardNum = binding.cardNumberEditText.getPureNumber()
        val expiryDate = binding.cardDateEditText.text.toString()
        val cvv = binding.cardCVCEditText.text.toString()
        //Update card with inputted info
        viewModel.updateCardInfo(cardNum, expiryDate, cvv)
        //If card is valid, submit the payment
        if (viewModel.getCardInfo()?.isValid() == true) {
            lifecycleScope.launch {
                val result = viewModel.submitPay(requireActivity())
                if (result.status == ApiStatus.SUCCESS && result.data != null) {
                    //Success path
                    findNavController().navigate(R.id.action_payFragment_to_payConfirm3dsFragment)
                } else {
                    //Error path
                    showErrorDialog(result.message)
                }
            }
        }

    }

    private fun showErrorDialog(message: String?) {
        AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.pay_error_message, message))
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
            }.show()
    }
}