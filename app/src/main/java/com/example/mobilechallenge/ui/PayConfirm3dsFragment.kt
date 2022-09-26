package com.example.mobilechallenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mobilechallenge.R
import com.example.mobilechallenge.databinding.FragmentPayConfirm3dsBinding
import com.example.mobilechallenge.payment.PaymentHelper
import com.example.mobilechallenge.viewmodel.MainViewModel

/***
 * A fragment to show 3DS confirmation layout to user
 */
class PayConfirm3dsFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentPayConfirm3dsBinding

    companion object {
        private const val TAG = "PayConfirm3dsActivity"

        @JvmStatic
        fun newInstance() =
            PayConfirm3dsFragment()
    }

    private fun loadConfirmUrl() {
        val payConfirmUrl = viewModel.getPayConfirmUrl()
        Log.d(TAG, "result Url: $payConfirmUrl")
        if (payConfirmUrl == null) {
            Log.e(TAG, "url is null")
        } else {
            binding.webView.loadUrl(payConfirmUrl)
        }
    }

    private fun processResult(url: String?): Boolean {
        if (url != null) {
            if (url.contains(PaymentHelper.FAILURE_URL)) {
                Log.d(TAG, "processResult: FAILURE")
                findNavController().navigate(R.id.action_payConfirm3dsFragment_to_failureFragment)
                return true
            } else if (url.contains(PaymentHelper.SUCCESS_URL)) {
                Log.d(TAG, "processResult: SUCCESS")
                findNavController().navigate(R.id.action_payConfirm3dsFragment_to_successFragment)
                return true
            }
        }
        return false //Allow the webview to process the url
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayConfirm3dsBinding.inflate(inflater)
        lifecycleScope.launchWhenCreated {
            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

            //Initialize the WebView before loading the url
            val settings: WebSettings = binding.webView.settings
            settings.javaScriptEnabled = true
            binding.webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    processResult(url)
                    return false
                }
            }
            loadConfirmUrl()
        }

        return binding.root
    }
}