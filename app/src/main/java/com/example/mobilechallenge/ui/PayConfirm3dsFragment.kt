package com.example.mobilechallenge.ui

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.mobilechallenge.constant.Constants
import com.example.mobilechallenge.databinding.FragmentPayConfirm3dsBinding
import com.example.mobilechallenge.payment.PaymentHelper
import com.example.mobilechallenge.viewmodel.MainViewModel

class PayConfirm3dsFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentPayConfirm3dsBinding

    companion object {
        private const val TAG = "PayConfirm3dsActivity"

        @JvmStatic
        fun newInstance() =
            PayConfirm3dsFragment()
    }

    private fun loadUrl() {
        val url = viewModel.getPayConfirmUrl()
        Log.d(TAG, "result Url: $url")
        if (url == null) {
            Log.e(TAG, "url is null")
        } else {
            binding.webView.loadUrl(url)
        }
    }

    private fun processResult(url: String?): Boolean {
        if (url != null) {
            val returnIntent = Intent()
            if (url.contains(PaymentHelper.FAILURE_URL)) {
                Log.d(TAG, "shouldOverrideUrlLoading: FAILURE")
                returnIntent.putExtra(Constants.EXTRA_RESULT_DATA, false)
                viewModel.loadFailureFragment()
                return true
            } else if (url.contains(PaymentHelper.SUCCESS_URL)) {
                Log.d(TAG, "shouldOverrideUrlLoading: SUCCESS")
                returnIntent.putExtra(Constants.EXTRA_RESULT_DATA, true)
                viewModel.loadSuccessFragment()
                return true
            }
        }
        return false //Allow the webview to process the url
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayConfirm3dsBinding.inflate(inflater)
        lifecycleScope.launchWhenCreated {
            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

            val settings: WebSettings = binding.webView.settings
            settings.javaScriptEnabled = true
            binding.webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Log.d(TAG, "shouldOverrideUrlLoading: $url")
                    processResult(url)
                    return false
                }
            }
            loadUrl()
        }

        return binding.root
    }
}